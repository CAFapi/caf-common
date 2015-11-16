#!/bin/bash

if [ -n "$MESOS_SANDBOX" ] && [ -n "$SSL_CA_CRT" ]
then
    if ! [ -e $MESOS_SANDBOX/$SSL_CA_CRT ]
    then
        echo "CA Certificate at '$MESOS_SANDBOX/$SSL_CA_CRT' not found"
        exit 1
    fi

    # Determine OS version
    distribution=`awk -F= '/^ID=/{print $2}' /etc/os-release`
    if [ "$distribution" == "debian" ]; then
        keytool -noprompt -keystore /usr/lib/ssl/certs/java/cacerts -storepass changeit -importcert -alias caf-ssl-ca-cert -file $MESOS_SANDBOX/$SSL_CA_CRT
    else
        keytool -noprompt -keystore /etc/pki/java/cacerts -storepass changeit -importcert -alias caf-ssl-ca-cert -file $MESOS_SANDBOX/$SSL_CA_CRT
    fi

else
    echo "Not installing CA Certificate for Java"
fi

