#!/bin/bash

if [ -n "$MESOS_SANDBOX" ] && [ -n "$SSL_CA_CRT" ]
then
    if ! [ -e $MESOS_SANDBOX/$SSL_CA_CRT ]
    then
        echo "CA Certificate at '$MESOS_SANDBOX/$SSL_CA_CRT' not found"
        exit 1
    fi

    # Determine OS version
    JAVA_KEYSTORE_PASSWORD=${JAVA_KEYSTORE_PASSWORD:-changeit}
    if [ -e /usr/lib/ssl/certs/java/cacerts ]; then
        echo "Importing CA cert into Java Keystore on Debian";
        keytool -noprompt -keystore /usr/lib/ssl/certs/java/cacerts -storepass $JAVA_KEYSTORE_PASSWORD -importcert -alias caf-ssl-ca-cert -file $MESOS_SANDBOX/$SSL_CA_CRT
    else
        if [ -e /etc/pki/java/cacerts ]; then
            echo "Importing CA cert into Java Keystore on CentOS";
            keytool -noprompt -keystore /etc/pki/java/cacerts -storepass $JAVA_KEYSTORE_PASSWORD -importcert -alias caf-ssl-ca-cert -file $MESOS_SANDBOX/$SSL_CA_CRT
        else
            echo "Not installing CA Certificate for Java. Unsupported OS."
        fi
    fi

else
    echo "Not installing CA Certificate for Java"
fi

