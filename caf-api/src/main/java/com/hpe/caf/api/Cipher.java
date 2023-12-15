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
package com.hpe.caf.api;

/**
 * Provides a method of encrypting and decrypting string data, such as passwords.
 */
public interface Cipher
{
    /**
     * Decrypt a string.
     *
     * @param input the data to decrypt
     * @return the decrypted data
     * @throws CipherException if the decryption fails
     */
    String decrypt(String input)
        throws CipherException;

    /**
     * Encrypt a string.
     *
     * @param input the data to encrypt
     * @return the encrypted data
     * @throws CipherException if the encryption fails
     */
    String encrypt(String input)
        throws CipherException;
}
