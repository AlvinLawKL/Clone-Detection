package Version_4;

import java.security.MessageDigest;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.*;
import org.apache.commons.io.FileUtils;

public class Hash {

    static HashMap<String,ArrayList<String>> classesHash = new HashMap<String,ArrayList<String>>();

    //Look for files in directory to hash, then compare result
    public static HashMap<String,ArrayList<String>> hashAll(ArrayList<String> appLists) {
        ClassStructure classStructure = new ClassStructure();

        for (String appPath: appLists) {
            ArrayList<String> classPath = classStructure.getClassPath(appPath);
            int num = 0;
            int size = classPath.size();
            File dir = new File(appPath);
            File[] fileList = dir.listFiles();
            if (fileList != null) {
                for (File file: fileList) {
                    String filePath = file.getAbsolutePath();
                    if (file.getName().endsWith(".txt")) {
                        System.out.println("Hashing classes in " + filePath);
                        num = hashSHA256(appPath,classPath,filePath,num);
                    }
                }
            }

            if (num != size) {
                System.out.println("Not all class structures are hashed in " + appPath);
                return null;
            }
        }

        return classesHash;
    }

    //Hash file and put it into hashmap
    private static int hashSHA256(String appPath,ArrayList<String> classPath,String filePath,int num) {
        File file = new File(filePath);
        ReplaceIdent ri = new ReplaceIdent();
        
        try {
            BufferedReader bis = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            ArrayList<String> list = new ArrayList<String>();
			String line;
			while ((line = bis.readLine()) != null) {
                if (line.contains("{")) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }
                else if (!line.contains("}")) {
                    list.add(line);
                }

                if (line.charAt(0) == '}') {
                    // Sort within class structure
                    Collections.sort(list,String.CASE_INSENSITIVE_ORDER);
                    Iterator iterator = list.iterator();
                    while (iterator.hasNext()) {
                        sb.append(iterator.next());
                        sb.append(System.lineSeparator());
                    }
                    sb.append(line);

                    try {
                        String classStructure = sb.toString();
                        String replacedClassStructure = ri.replaceIdentifier(classStructure);
                        byte[] stringInBytes = replacedClassStructure.getBytes();
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        byte[] hash = md.digest(stringInBytes);
                        ArrayList<String> data = new ArrayList<String>();
                        data.add(toHexString(hash));
                        data.add(appPath);
                        classesHash.put(classPath.get(num), data);
                        num++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sb.setLength(0);
                    list.clear();
                }
			}
            bis.close();
            return num;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return num;
        } catch (IOException e) {
            e.printStackTrace();
            return num;
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

    public static void storeFiles(ResultSet resultset,String outputPath,int total) {
        try {
            int fileNum = 0;
            while (resultset.next()) {
                fileNum++;
                System.out.println("Getting file: " + fileNum + "/" + total);
                String filePath = resultset.getString("PATH");
                String hexString = resultset.getString("HASH");
                //Create folder to store files with same hash values
                String outputDir = outputPath + "/" + hexString;
                File storeDir = new File(outputDir);
                if (!storeDir.exists()) {
                    storeDir.mkdir();
                }

                File getFile = new File(filePath);
                FileUtils.copyFileToDirectory(getFile,storeDir);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}