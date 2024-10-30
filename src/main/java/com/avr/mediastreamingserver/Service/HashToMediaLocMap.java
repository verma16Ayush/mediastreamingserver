package com.avr.mediastreamingserver.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.avr.mediastreamingserver.Constants.Constants;

@Service
public class HashToMediaLocMap {

    Map<String, String> hashMap;
    MessageDigest messageDigest;

    public HashToMediaLocMap() throws NoSuchAlgorithmException {
        this.hashMap = new HashMap<>();
        messageDigest = MessageDigest.getInstance(Constants.HASH_ALGORITHM_MD5);
    }

    
    // returns the hash and inserts into the map
    public String insertInMap(String fileLoc) throws UnsupportedEncodingException{
        byte[] byteArray = fileLoc.getBytes(Constants.ENCODING_TYPE_UTF8);
        String digestedMsg = digestToReadableString(messageDigest.digest(byteArray));
        hashMap.put(digestedMsg, fileLoc);
        return digestedMsg;
    }

    public String getFileLocFromHash(String hashString) {
        return hashMap.get(hashString);
    }

    private String digestToReadableString(byte[] digest){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String s = Integer.toHexString(digest[i]);
            while (s.length() < 2) {
                s = "0" + s;
            }
            s = s.substring(s.length() - 2); // we need the last 2 chars
            sb.append(s);
        }
        return sb.toString();
    }
}
