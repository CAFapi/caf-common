/*
 * Copyright 2015-2018 Micro Focus or one of its affiliates.
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
package com.hpe.caf.cipher.jasypt;

import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.Cipher;
import com.hpe.caf.api.CipherException;
import com.hpe.caf.api.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.mockito.Mockito;

public class JasyptCipherTest
{
    private static final String PASS = "test123";

    @Test
    public void jasyptStringTest()
        throws ConfigurationException, CipherException
    {
        BootstrapConfiguration boot = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(boot.getConfiguration(JasyptCipher.CONFIG_SECURITY_PASS)).thenReturn(PASS);
        Cipher sp = new JasyptCipher(boot);
        String testString = "test456";
        Assert.assertEquals(testString, sp.decrypt(sp.encrypt(testString)));
    }

    @Test(expectedExceptions = CipherException.class)
    public void jasyptExceptionTest()
        throws ConfigurationException, CipherException
    {
        BootstrapConfiguration boot = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(boot.getConfiguration(JasyptCipher.CONFIG_SECURITY_PASS)).thenThrow(ConfigurationException.class);
        Cipher sp = new JasyptCipher(boot);
        String testString = "test456";
        sp.decrypt(sp.encrypt(testString));
    }
}
