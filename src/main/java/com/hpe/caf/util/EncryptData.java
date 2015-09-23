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

        CipherProvider factory = ComponentLoader.getService(CipherProvider.class);
        BootstrapConfiguration bc = new SystemBootstrapConfiguration();
        Cipher sp = factory.getCipher(bc);
        System.out.println(sp.encrypt(args[0]));
    }
}
