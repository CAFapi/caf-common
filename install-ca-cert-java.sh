#!/bin/bash

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
