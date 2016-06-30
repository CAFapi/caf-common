#!/bin/bash

if [ -n "$MESOS_SANDBOX" ] && [ -n "$SSL_CA_CRT" ]
then
    if ! [ -e $MESOS_SANDBOX/$SSL_CA_CRT ]
    then
        echo "CA Certificate at '$MESOS_SANDBOX/$SSL_CA_CRT' not found"
        exit 1
    fi

    # Determine OS version
    if [ -e /usr/sbin/update-ca-certificates ]; then
        echo "Installing CA Certificate on Debian"
        cp -v $MESOS_SANDBOX/$SSL_CA_CRT /usr/local/share/ca-certificates/ssl-ca-cert.crt
        update-ca-certificates
    elif [ -e /usr/bin/update-ca-trust ]; then
        echo "Installing CA Certificate on CentOS"
        cp -v $MESOS_SANDBOX/$SSL_CA_CRT /etc/pki/ca-trust/source/anchors/ssl-ca-cert.crt
        update-ca-trust
    else
        echo "Not installing CA Certificate. Unsupported OS."
    fi

else
    echo "Not installing CA Certificate."
fi
