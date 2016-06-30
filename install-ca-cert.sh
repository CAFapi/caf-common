#!/bin/bash

copy_certs() {
    IFS=',' read -a caFiles <<< "$SSL_CA_CRT"

    for caFile in "${caFiles[@]}"
    do
        if ! [ -e $MESOS_SANDBOX/$caFile ]
        then
            echo "CA Certificate at '$MESOS_SANDBOX/$caFile' not found"
            echo "Aborting further system CA certificate load attempts."
            exit 1
        fi

        echo "Installing CA Certificate on $1"
        cp -v $MESOS_SANDBOX/$caFile $2/$caFile.crt
    done
}

if [ -n "$MESOS_SANDBOX" ] && [ -n "$SSL_CA_CRT" ]
then
    if ! [ -e $MESOS_SANDBOX/$SSL_CA_CRT ]
    then
        echo "CA Certificate at '$MESOS_SANDBOX/$SSL_CA_CRT' not found"
        exit 1
    fi

    # Determine OS version
    if [ -e /usr/sbin/update-ca-certificates ]; then
        copy_certs "Debian" /usr/local/share/ca-certificates
        update-ca-certificates
    elif [ -e /usr/bin/update-ca-trust ]; then
        copy_certs "CentOS" /etc/pki/ca-trust/source/anchors
        update-ca-trust
    else
        echo "Not installing CA Certificate. Unsupported OS."
    fi

else
    echo "Not installing CA Certificate."
fi
