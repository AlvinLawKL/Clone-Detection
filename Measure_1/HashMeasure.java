package Measure_1;

import java.security.MessageDigest;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

public class HashMeasure {

    static HashMap<String, String> classesHash = new HashMap<String, String>();

    //Look for files in directory to hash, then compare result
    public static void hashAll(ArrayList<String> appLists) {
        ClassStructureMeasure classStructure = new ClassStructureMeasure();

        for (String appPath: appLists) {
            ArrayList<String> classPath = classStructure.getClassPath(appPath);
            File dir = new File(appPath);
            File[] fileList = dir.listFiles();
            if (fileList != null) {
                for (File file: fileList) {
                    String filePath = file.getAbsolutePath();
                    if (file.getName().endsWith(".txt")) {
                        hashSHA256(classPath,filePath);
                    }
                }
            }
        }
        compareHashmap();
    }

    //Hash file and put it into hashmap
    private static void hashSHA256(ArrayList<String> classPath, String filePath) {
        File file = new File(filePath);
        ReplaceIdentMeasure rim = new ReplaceIdentMeasure();
        int num = 0;
        
        try {
            BufferedReader bis = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
			String line;
			while ((line = bis.readLine()) != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
                if (line.charAt(0) == '}') {
                    try {
                        String classStructure = sb.toString();
                        //System.out.println(classPath.get(num));
                        //System.out.println(classStructure);
                        String replacedClassStructure = rim.replaceIdentifier(classStructure);
                        byte[] stringInBytes = replacedClassStructure.getBytes();
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        byte[] hash = md.digest(stringInBytes);
                        classesHash.put(classPath.get(num), toHexString(hash));
                        num++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sb.setLength(0);
                }
			}
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        /*
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(fileInBytes);
            classesHash.put(filePath, toHexString(hash));
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
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
                if (hash1 == System.identityHashCode(filePath2)) {
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