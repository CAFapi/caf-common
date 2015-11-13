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
        cp -v $MESOS_SANDBOX/$SSL_CA_CRT /usr/local/share/ca-certificates/ssl-ca-cert.crt
        update-ca-certificates
    else
        cp -v $MESOS_SANDBOX/$SSL_CA_CRT /etc/pki/ca-trust/source/anchors/ssl-ca-cert.crt
        update-ca-trust
    fi

else
    echo "Not installing CA Certificate @ /usr/local/share/ca-certificates"
fi