package com.canopus.mw.utils;

import org.jasypt.encryption.*;

public class EncryptionHelper
{
    private StringEncryptor encryptor;
    private static final String ENC_PREFIX = "ENC(";
    private static final String ENC_SUFFIX = ")";
    
    public String encrypt(final String plain) {
        final String encrypted = this.getEncryptor().encrypt(plain);
        return "ENC(" + encrypted + ")";
    }
    
    public String decrypt(String encrypted) {
        String decrypted = null;
        if (encrypted.startsWith("ENC(")) {
            encrypted = encrypted.substring(4, encrypted.length() - 1);
        }
        decrypted = this.getEncryptor().decrypt(encrypted);
        return decrypted;
    }
    
    public boolean isEncrypted(final String pwd) {
        return pwd.startsWith("ENC(");
    }
    
    public StringEncryptor getEncryptor() {
        return this.encryptor;
    }
    
    public void setEncryptor(final StringEncryptor encryptor) {
        this.encryptor = encryptor;
    }
}
