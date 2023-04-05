#!/bin/bash
#
# Copyright 2015-2023 Open Text.
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

        update-ca-certificates
    elif [ -e /usr/bin/update-ca-trust ]; then
        copy_certs "CentOS" /etc/pki/ca-trust/source/anchors
        update-ca-trust
    else
        echo "Not installing CA Certificate. Unsupported OS."
    fi

else
    echo "Not installing CA Certificate."
fi

