package com.hp.caf.api;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;


/**
 * Provides a method of encrypting and decrypting string data, such as passwords.
 */
public abstract class Cipher
{
    /**
     * Initialise the cipher provider.
     * @param bootstrap the bootstrap configuration provider, used for initialising the encryption/decryption mechanism
     */
    public Cipher(final BootstrapConfiguration bootstrap)
    {
        Objects.requireNonNull(bootstrap);
    }


    /**
     * Decrypt a string.
     * @param input the data to decrypt
     * @return the decrypted data
     * @throws CipherException if the decryption fails
     */
    public abstract String decrypt(final String input)
        throws CipherException;


    /**
     * Encrypt a string.
     * @param input the data to encrypt
     * @return the encrypted data
     * @throws CipherException if the encryption fails
     */
    public abstract String encrypt(final String input)
        throws CipherException;


    /**
     * Parse a Java class, decrypting all String fields that are marked @Encrypted, assuming they have the appropriate
     * getters and setters.
     * @param input the Java class to parse
     * @param <T> the type of the Java class
     * @return the parsed class
     * @throws CipherException if decryption fails
     */
    public final <T> T decrypt(final T input)
            throws CipherException
    {
        Objects.requireNonNull(input);
        for ( Field f : input.getClass().getDeclaredFields() ) {
            if ( f.getType().equals(String.class) && f.isAnnotationPresent(Encrypted.class) ) {
                try {
                    Method getter = new PropertyDescriptor(f.getName(), input.getClass()).getReadMethod();
                    Method setter = new PropertyDescriptor(f.getName(), input.getClass()).getWriteMethod();
                    if ( getter != null && setter != null ) {
                        setter.invoke(input, decrypt((String) getter.invoke(input)));
                    }
                } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                    throw new CipherException("Failed to decrypt class fields", e);
                }
            }
        }
        return input;
    }
}
