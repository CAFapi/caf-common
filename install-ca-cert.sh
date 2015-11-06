#!/bin/bash

if [ -n "$MESOS_SANDBOX" ] && [ -n "$SSL_CA_CRT" ]
then
    if ! [ -e $MESOS_SANDBOX/$SSL_CA_CRT ]
    then
        echo "CA Certificate at '$MESOS_SANDBOX/$SSL_CA_CRT' not found"
        exit 1
    fi

    cp $MESOS_SANDBOX/$SSL_CA_CRT /usr/local/share/ca-certificates/ssl-ca-cert.crt
    update-ca-certificates
else
    echo "Not installing CA Certificate @ /usr/local/share/ca-certificates"
fi