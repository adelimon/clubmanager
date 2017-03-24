package com.ajdconsulting.pra.clubmanager.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Convert a string to its MD5 hexadecimal representation.
 *
 * @author adelimon
 * @since 3/19/17, Github issue #86
 */
public class Md5Converter {

    public static String toMd5(String str) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
        digest.update(str.getBytes());
        return new BigInteger(1, digest.digest()).toString(16);
    }
}
