/*
 * Copyright 2015-2024 Open Text.
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
package com.github.cafapi.common.util.tools;

import com.github.cafapi.common.api.BootstrapConfiguration;
import com.github.cafapi.common.api.Cipher;
import com.github.cafapi.common.api.CipherProvider;
import com.github.cafapi.common.util.moduleloader.ModuleLoader;
import com.github.cafapi.common.bootstrapconfigs.system.SystemBootstrapConfiguration;

/**
 * Utility app for using a ServiceProvider to encrypt some data. Typically used for generated encrypted passwords to put in configuration
 * files. The desired SecurityProvider must be present on the classpath.
 *
 * Usage: java -cp "*" com.github.cafapi.common.util.tools.EncryptData data
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
            System.err.println("Usage: java -cp * com.github.cafapi.common.util.tools.EncryptData data");
            System.exit(1);
        }

        CipherProvider factory = ModuleLoader.getService(CipherProvider.class);
        BootstrapConfiguration bc = new SystemBootstrapConfiguration();
        Cipher sp = factory.getCipher(bc);
        System.out.println(sp.encrypt(args[0]));
    }
}
