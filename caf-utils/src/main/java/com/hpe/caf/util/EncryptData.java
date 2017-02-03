/*
 * Copyright 2015-2017 Hewlett Packard Enterprise Development LP.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.caf.util;


import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.Cipher;
import com.hpe.caf.api.CipherProvider;
import com.hpe.caf.config.system.SystemBootstrapConfiguration;


/**
 * Utility app for using a ServiceProvider to encrypt some data. Typically used for
 * generated encrypted passwords to put in configuration files. The desired SecurityProvider
 * must be present on the classpath.
 *
 * Usage: java -cp "*" com.hpe.caf.util.EncryptData data
 */
public final class EncryptData
{
    private EncryptData() { }


    public static void main(final String[] args)
            throws Exception
    {
        if ( args.length < 1 ) {
            System.err.println("Usage: java -cp * com.hpe.caf.util.EncryptData data");
            System.exit(1);
        }

        CipherProvider factory = ModuleLoader.getService(CipherProvider.class);
        BootstrapConfiguration bc = new SystemBootstrapConfiguration();
        Cipher sp = factory.getCipher(bc);
        System.out.println(sp.encrypt(args[0]));
    }
}
