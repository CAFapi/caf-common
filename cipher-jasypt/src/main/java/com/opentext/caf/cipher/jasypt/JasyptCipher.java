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
package com.opentext.caf.cipher.jasypt;

import com.opentext.caf.api.BootstrapConfiguration;
import com.opentext.caf.api.Cipher;
import com.opentext.caf.api.CipherException;
import com.opentext.caf.api.ConfigurationException;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Implementation of a SecurityProvider that uses Jasypt to provide basic text encryption/decryption capabilities. The strong encryptor is
 * not used to avoid licensing/export issues.
 */
public class JasyptCipher implements Cipher
{
    /**
     * The keyword used to encrypt and decrypt data.
     */
    public static final String CONFIG_SECURITY_PASS = "CAF_CIPHER_PASS";

    /**
     * This is PBE with MD5 and DES encryption.
     */
    private final BasicTextEncryptor codec = new BasicTextEncryptor();

    /**
     * {@inheritDoc}
     *
     * The cipher.pass variable must be present in the bootstrap configuration for this provider to init.
     */
    public JasyptCipher(final BootstrapConfiguration bootstrap)
        throws CipherException
    {
        try {
            codec.setPassword(bootstrap.getConfiguration(CONFIG_SECURITY_PASS));
        } catch (final ConfigurationException e) {
            throw new CipherException("Configuration " + CONFIG_SECURITY_PASS + " not set", e);
        }
    }

    @Override
    public String decrypt(final String input)
    {
        return codec.decrypt(input);
    }

    @Override
    public String encrypt(final String input)
    {
        return codec.encrypt(input);
    }
}
