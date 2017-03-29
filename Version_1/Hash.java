package Version_1;

import java.security.MessageDigest;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

public class Hash {

    static HashMap<String, String> classesHash = new HashMap<String, String>();

    //Look for files in directory to hash, then compare result
    public static void hashAll(String dirPath) {
        File dir = new File(dirPath);
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File file: fileList) {
                String filePath = file.getAbsolutePath();
                if (file.getName().endsWith(".txt")) {
                    hashSHA256(filePath);
                }
            }
        }

        compareHashmap();
    }

    //Hash file and put it into hashmap
    private static void hashSHA256(String filePath) {
        File file = new File(filePath);
        int size = (int) file.length();
        byte[] fileInBytes = new byte[size];
        
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(fileInBytes, 0, fileInBytes.length);
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(fileInBytes);
            classesHash.put(filePath, toHexString(hash));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Convert hash value into hex string
    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    //Compare the hash value of each class with other classes at least once
    private static void compareHashmap() {
        for(Map.Entry<String, String> entry1: classesHash.entrySet()) {
            String filePath1 = entry1.getKey();
            int hash1 = System.identityHashCode(filePath1);
            String hexString1 = entry1.getValue();

            for(Map.Entry<String, String> entry2: classesHash.entrySet()) {
                String filePath2 = entry2.getKey();
                if (hash1 > System.identityHashCode(filePath2)) {
                    continue;
                }
                String hexString2 = entry2.getValue();

                if (hexString1 == hexString2) {
                    System.out.println("Ya");
                }
            }
        }
    }

}