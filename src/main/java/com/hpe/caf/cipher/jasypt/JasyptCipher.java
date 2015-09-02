package com.hpe.caf.cipher.jasypt;


import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.Cipher;
import com.hpe.caf.api.CipherException;
import com.hpe.caf.api.ConfigurationException;
import org.jasypt.util.text.BasicTextEncryptor;


/**
 * Implementation of a SecurityProvider that uses Jasypt to provide basic
 * text encryption/decryption capabilities. The strong encryptor is not used
 * to avoid licensing/export issues.
 */
public class JasyptCipher extends Cipher
{
    /**
     * The keyword used to encrypt and decrypt data.
     */
    public static final String CONFIG_SECURITY_PASS = "cipher.pass";
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
        super(bootstrap);
        try {
            codec.setPassword(bootstrap.getConfiguration(CONFIG_SECURITY_PASS));
        } catch (ConfigurationException e) {
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
