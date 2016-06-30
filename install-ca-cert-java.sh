#!/bin/bash

JAVA_KEYSTORE_PASSWORD=${JAVA_KEYSTORE_PASSWORD:-changeit}

import_java_cert() {
    echo "Importing CA cert into Java Keystore on $1";
    keytool -noprompt -keystore $2 -storepass $JAVA_KEYSTORE_PASSWORD -importcert -alias caf-ssl-ca-cert -file $MESOS_SANDBOX/$SSL_CA_CRT
}

if [ -n "$MESOS_SANDBOX" ] && [ -n "$SSL_CA_CRT" ]
then
    if ! [ -e $MESOS_SANDBOX/$SSL_CA_CRT ]
    then
        echo "CA Certificate at '$MESOS_SANDBOX/$SSL_CA_CRT' not found"
        exit 1
    fi

    # Determine OS version
    if [ -e /usr/lib/ssl/certs/java/cacerts ]; then
        import_java_cert "Debian" /usr/lib/ssl/certs/java/cacerts
    elif [ -e /etc/pki/java/cacerts ]; then
        import_java_cert "CentOS" /etc/pki/java/cacerts
    else
        echo "Not installing CA Certificate for Java. Unsupported OS."
    fi

else
    echo "Not installing CA Certificate for Java"
fi
