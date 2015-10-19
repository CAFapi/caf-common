package com.hpe.caf.api;


/**
 * Provides a method of encrypting and decrypting string data, such as passwords.
 * @since 9.0
 */
public interface Cipher
{
    /**
     * Decrypt a string.
     * @param input the data to decrypt
     * @return the decrypted data
     * @throws CipherException if the decryption fails
     */
    String decrypt(String input)
        throws CipherException;


    /**
     * Encrypt a string.
     * @param input the data to encrypt
     * @return the encrypted data
     * @throws CipherException if the encryption fails
     */
    String encrypt(String input)
        throws CipherException;
}
