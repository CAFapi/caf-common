/*
 * Copyright 2015-2017 EntIT Software LLC, a Micro Focus company.
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
package com.github.cafapi.util;

import com.github.cafapi.BootstrapConfiguration;
import com.github.cafapi.Cipher;
import com.github.cafapi.CipherProvider;
import com.github.cafapi.config.system.SystemBootstrapConfiguration;

/**
 * Utility app for using a ServiceProvider to encrypt some data. Typically used for generated encrypted passwords to put in configuration
 * files. The desired SecurityProvider must be present on the classpath.
 *
 * Usage: java -cp "*" com.github.cafapi.util.EncryptData data
 */
public final class EncryptData
{
    private EncryptData()
    {
    }

    public static void main(final String[] args)
        throws Exception
    {
        if (args.length < 1) {
            System.err.println("Usage: java -cp * com.github.cafapi.util.EncryptData data");
            System.exit(1);
        }

        CipherProvider factory = ModuleLoader.getService(CipherProvider.class);
        BootstrapConfiguration bc = new SystemBootstrapConfiguration();
        Cipher sp = factory.getCipher(bc);
        System.out.println(sp.encrypt(args[0]));
    }
}
