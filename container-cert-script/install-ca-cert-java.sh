#!/bin/bash
#
# Copyright 2015-2017 Hewlett Packard Enterprise Development LP.
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

JAVA_KEYSTORE_PASSWORD=${JAVA_KEYSTORE_PASSWORD:-changeit}

import_java_cert() {
    echo "Importing CA cert into Java Keystore on $1"
    keytool -noprompt -keystore $2 -storepass $JAVA_KEYSTORE_PASSWORD -importcert -alias caf-ssl-ca-cert-$4 -file $3
}

import_java_certs() {
    IFS=',' read -a caFiles <<< "$SSL_CA_CRT"

    index=0
    for caFile in "${caFiles[@]}"
    do
        if ! [ -e $MESOS_SANDBOX/$caFile ]
        then
            echo "CA Certificate at '$MESOS_SANDBOX/$caFile' not found"
            echo "Aborting further Java CA certificate load attempts."
            exit 1
        fi

        import_java_cert $1 $2 $MESOS_SANDBOX/$caFile $index
	    (( index++ ))
        echo "CA Certificate '$caFile' added to cacerts"
    done
}

if [ -n "$MESOS_SANDBOX" ] && [ -n "$SSL_CA_CRT" ]
then
    # Determine OS version
    if [ -e /usr/lib/ssl/certs/java/cacerts ]; then
        import_java_certs "Debian" /usr/lib/ssl/certs/java/cacerts
    elif [ -e /etc/pki/java/cacerts ]; then
        import_java_certs "CentOS" /etc/pki/java/cacerts
    else
        echo "Not installing CA Certificate for Java. Unsupported OS."
    fi
else
    echo "Not installing CA Certificate for Java"
fi
