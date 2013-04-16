package util;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    
    public static String toMD5(String plain) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest;
			try {
				messageDigest = md.digest(plain.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				throw new Exception(e);
			}
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new Exception(e);
        }
    }
    
    public static String fileChecksum(FileInputStream fis) {
        
        MessageDigest md;
        try {
            byte[] dataBytes = new byte[1024];
            md = MessageDigest.getInstance("MD5");
            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            };
            byte[] mdbytes = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        
    }
}
