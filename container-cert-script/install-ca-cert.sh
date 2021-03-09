#!/bin/bash
#
# Copyright 2015-2021 Micro Focus or one of its affiliates.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

MESOS_SANDBOX=${SSL_CA_CRT_DIR:-$MESOS_SANDBOX}

copy_certs() {
    IFS=',' read -a caFiles <<< "$SSL_CA_CRT"

    for caFile in "${caFiles[@]}"
    do
        if ! [ -e $MESOS_SANDBOX/$caFile ]
        then
            echo "CA Certificate at '$MESOS_SANDBOX/$caFile' not found"
            echo "Aborting further system CA certificate load attempts."
            exit 1
        fi

        echo "Installing CA Certificate on $1"
        cp -v $MESOS_SANDBOX/$caFile $2/$caFile.crt
    done
}

update_openssl() {
    mkdir -p /tmp/ssl/ca-certificates/openssl
    trust extract --format=openssl-directory --filter=ca-anchors --overwrite /tmp/ssl/ca-certificates/openssl
    cp -R /tmp/ssl/ca-certificates/openssl/* /var/lib/ca-certificates/openssl/
}

update_etc_ssl() {
    etccertsdir="/etc/ssl/certs"
    pemdir="/var/lib/ca-certificates/pem"

    trust extract --purpose=server-auth --filter=ca-anchors --format=pem-directory-hash -f /tmp/ssl/ca-certificates/pem
    cp -R /tmp/ssl/ca-certificates/pem/* "$pemdir"/

    # fix up /etc/ssl/certs if it's not a link pointing to /var/lib/ca-certificates/pem
    if ! [ -L "$etccertsdir" -a "`readlink $etccertsdir`" = "../..$pemdir" ]; then
        echo "Warning: $etccertsdir needs to be a link to ../..$pemdir, fixing" >&2
        if [ -d "$etccertsdir" ]; then
      mv -Tv --backup=numbered "$etccertsdir" "$etccertsdir.old"
        fi
        ln -Tsv --backup=numbered "../..$pemdir" "$etccertsdir"
    fi
}

update_certbundle() {
    cafile="/var/lib/ca-certificates/ca-bundle.pem"
    cadir="/var/lib/ca-certificates/pem"

    for i in "$@"; do
      if [ "$i" = "-f" ]; then
        fresh=1
      elif [ "$i" = "-v" ]; then
        verbose=1
      fi
    done

    if [ -z "$fresh" -a "$cafile" -nt "$cadir" ]; then
      exit 0
    fi
    [ -z "$verbose" ] || echo "creating $cafile ..."
    trust extract --format=pem-bundle --purpose=server-auth --filter=ca-anchors $cafile.tmp
    cat - $cafile.tmp > "$cafile.new" <<EOF
#
# automatically created by $0. Do not edit!
#
# Use of this file is deprecated and should only be used as last
# resort by applications that do not support p11-kit or reading /etc/ssl/certs.
# You should avoid hardcoding any paths in applications anyways though. Use
# functions that know the operating system defaults instead:
#
# - openssl: SSL_CTX_set_default_verify_paths()
# - gnutls: gnutls_certificate_set_x509_system_trust(cred)
#
EOF

    rm -f $cafile.tmp
    mv -f "$cafile.new" "$cafile"
}

if [ -n "$MESOS_SANDBOX" ] && [ -n "$SSL_CA_CRT" ]
then

    # Determine OS version
    if [ -e /usr/sbin/update-ca-certificates ]; then
        DISTRO=$(grep "NAME=\"openSUSE" /etc/os-release)

        if [ -n "$DISTRO" ]; then
            copy_certs "openSUSE" /etc/pki/trust/anchors
        else
            copy_certs "Debian" /usr/local/share/ca-certificates
        fi
        update_openssl
        update_etc_ssl
        update_certbundle
    elif [ -e /usr/bin/update-ca-trust ]; then
        copy_certs "CentOS" /etc/pki/ca-trust/source/anchors
        update-ca-trust
    else
        echo "Not installing CA Certificate. Unsupported OS."
    fi

else
    echo "Not installing CA Certificate."
fi

