package com.licoforen.parentalcontrollauncher.Helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {

	public static String getHash(String s) {
	    try {
	        MessageDigest digest = java.security.MessageDigest
	                .getInstance("SHA-512");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        StringBuilder hexString = new StringBuilder();
	        for (byte aMessageDigest : messageDigest) {
	            String h = Integer.toHexString(0xFF & aMessageDigest);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	    }
	    return "";
	}
	
}
