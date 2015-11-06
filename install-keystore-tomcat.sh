#!/bin/bash

if [ -n "${SSL_KEYSTORE_PASSWORD}" ]
then
    echo "Setting keystore password in /usr/local/tomcat/conf/server.xml"
    sed -i 's/\(keystorePass\)="[^"]*"/\1='${SSL_KEYSTORE_PASSWORD}'/' /usr/local/tomcat/conf/server.xml
else
    echo "Not setting keystore password"
fi

if [ -n "${SSL_KEYSTORE}" ]

    if ! [ -e $MESOS_SANDBOX/$SSL_KEYSTORE ]
    then
        echo "Keystore at '$MESOS_SANDBOX/$SSL_KEYSTORE' not found"
        exit 1
    fi

    cp $MESOS_SANDBOX/$SSL_KEYSTORE /etc/ssl/certs/server.p12
then
    echo "No keystore to copy to /etc/ssl/certs/server.p12"
fi
