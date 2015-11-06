#!/bin/bash

if [ -n "$MESOS_SANDBOX" ] && [ -n "$SSL_CA_CRT" ]
then
    if ! [ -e $MESOS_SANDBOX/$SSL_CA_CRT ]
    then
        echo "CA Certificate at '$MESOS_SANDBOX/$SSL_CA_CRT' not found"
        exit 1
    fi

    cd /usr/lib/ssl/certs/java
    keytool -noprompt -keystore cacerts -storepass changeit -importcert -alias caf-ssl-ca-cert -file $MESOS_SANDBOX/$SSL_CA_CRT

else
    echo "Not installing CA Certificate for Java"
fi

