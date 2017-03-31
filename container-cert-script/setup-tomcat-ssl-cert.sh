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

# If there is a provided tomcat CA certificate
if [ -n "${JOB_SERVICE_KEYSTORE}" ]
then
    # search for and delete the lines in the server.xml with:
    # <!-- setup-tomcat-ssl-cert.sh TLS section start
    # setup-tomcat-ssl-cert.sh TLS section end -->
    echo "Uncommenting SSL connector in /usr/local/tomcat/conf/server.xml"
    sed -i '/setup-tomcat-ssl-cert.sh TLS section start/d' /usr/local/tomcat/conf/server.xml
    sed -i '/setup-tomcat-ssl-cert.sh TLS section end/d' /usr/local/tomcat/conf/server.xml

    # Replace the default keystore with the JOB_SERVICE_KEYSTORE keystore provided
    echo "Replacing default keystore in /usr/local/tomcat/conf/server.xml with provided environment variable JOB_SERVICE_KEYSTORE."
    sed -i "s@keystoreFile=.*@keystoreFile=\"$JOB_SERVICE_KEYSTORE\"@" /usr/local/tomcat/conf/server.xml
else
    echo "Not setting up tomcat SSL connector"
fi

# Replace default password with a user defined password if provided
if [ -z "${JOB_SERVICE_KEYSTOREPASS}" ]
then
    echo "Replacing password in /usr/local/tomcat/conf/server.xml with provided environment variable JOB_SERVICE_KEYSTOREPASS"
    sed -i "s@keystorePass=.*@keystorePass=\"$JOB_SERVICE_KEYSTOREPASS\"@" /usr/local/tomcat/conf/server.xml
fi

# Replace default alias with a user defined alias if provided
if [ -z "${JOB_SERVICE_KEYSTORE_ALIAS}" ]
then
    echo "Replacing keystore alias in /usr/local/tomcat/conf/server.xml with provided environment variable JOB_SERVICE_KEYSTORE_ALIAS"
    sed -i "s@keyAlias=.*@keyAlias=\"$JOB_SERVICE_KEYSTORE_ALIAS\"@" /usr/local/tomcat/conf/server.xml
fi
