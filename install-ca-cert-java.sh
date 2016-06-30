#!/bin/bash

storePass="changeit"
caKeystorePath="/usr/lib/ssl/certs/java"

if [ -n "$MESOS_SANDBOX" ] && [ -n "$SSL_CA_CRT" ]
then

    cd $caKeystorePath
    
    IFS=',' read -a caFiles <<< "$SSL_CA_CRT"

    for caFile in "${caFiles[@]}"
    do
        if ! [ -e $MESOS_SANDBOX/$caFile ]
        then
            echo "CA Certificate at '$MESOS_SANDBOX/$caFile' not found"
            echo "Aborting further Java CA certificate load attempts."
            exit 1
        fi
        
        keytool -noprompt -keystore cacerts -storepass $storePass -importcert -alias $caFile -file $MESOS_SANDBOX/$caFile
        echo "CA Certificate '$caFile' added to cacerts"
    done

else
    echo "Not installing CA Certificate(s) for Java"
fi
