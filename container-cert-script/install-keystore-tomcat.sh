#!/bin/bash
#
# Copyright 2015-2017 Hewlett Packard Enterprise Development LP.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

if [ -n "${SSL_KEYSTORE_PASSWORD}" ]
then
    if [ -e /usr/local/tomcat/conf/server.xml ]; then
        echo "Setting keystore password in /usr/local/tomcat/conf/server.xml"
        sed -i 's/\(keystorePass\)="[^"]*"/\1='\"${SSL_KEYSTORE_PASSWORD}\"'/' /usr/local/tomcat/conf/server.xml
    elif [ -e /usr/share/tomcat/conf/server.xml ]; then
        echo "Setting keystore password in /usr/share/tomcat/conf/server.xml"
        sed -i 's/\(keystorePass\)="[^"]*"/\1='\"${SSL_KEYSTORE_PASSWORD}\"'/' /usr/share/tomcat/conf/server.xml
    else
        echo "WARNING: Couldn't locate Tomcat server.xml file"
    fi
else
    echo "Not setting keystore password"
fi

if [ -n "${SSL_KEYSTORE}" ]
then
    if ! [ -e $MESOS_SANDBOX/$SSL_KEYSTORE ]
    then
        echo "Keystore at '$MESOS_SANDBOX/$SSL_KEYSTORE' not found"
        exit 1
    fi
    cp -v $MESOS_SANDBOX/$SSL_KEYSTORE /etc/ssl/certs/server.p12
else
    echo "No keystore to copy to /etc/ssl/certs/server.p12"
fi
