#!/bin/bash

#IMPORTANT: This version will only work with Debian based containers.

caKeystorePath="/usr/local/share/ca-certificates"

if [ -n "$MESOS_SANDBOX" ] && [ -n "$SSL_CA_CRT" ]
then

    IFS=',' read -a caFiles <<< "$SSL_CA_CRT"

    for caFile in "${caFiles[@]}"
    do
        if ! [ -e $MESOS_SANDBOX/$caFile ]
        then
            echo "CA Certificate at '$MESOS_SANDBOX/$caFile' not found"
            echo "Aborting further system CA certificate load attempts."
            exit 1
        fi

        cp -v $MESOS_SANDBOX/$caFile $caKeystorePath/$caFile.crt
        update-ca-certificates

        echo "CA Certificate '$caFile' added to system ca bundle"
    done

else
    echo "Not installing CA Certificate(s) to the system bundle"
fi
