package com.avr.mediastreamingserver.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.avr.mediastreamingserver.Constants.Constants;
import com.avr.mediastreamingserver.Model.FileLocAndHashRecord;

@Service
public class MediaStore {

    Map<String, String> hashMap;
    List<FileLocAndHashRecord> fileLocAndHashRecords;
    MessageDigest messageDigest;

    public MediaStore() throws NoSuchAlgorithmException {
        this.hashMap = new HashMap<>();
        this.fileLocAndHashRecords = new ArrayList<>();
        messageDigest = MessageDigest.getInstance(Constants.HASH_ALGORITHM_MD5);
    }

    
    // returns the hash and inserts into the map
    public String insertInMapAndRecordList(String fileLoc) throws UnsupportedEncodingException{
        byte[] byteArray = fileLoc.getBytes(Constants.ENCODING_TYPE_UTF8);
        String readableDigestedMsg = digestToReadableString(messageDigest.digest(byteArray));
        hashMap.put(readableDigestedMsg, fileLoc);
        fileLocAndHashRecords.add(new FileLocAndHashRecord(fileLoc, readableDigestedMsg, fileLoc.substring(fileLoc.lastIndexOf("/") + 1)));
        return readableDigestedMsg;
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

    public List<FileLocAndHashRecord> getFileLocAndHashRecords() {
        return this.fileLocAndHashRecords;
    }
}
