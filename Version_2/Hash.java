package Version_2;

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
import java.sql.*;
import org.apache.commons.io.FileUtils;

public class Hash {

    static HashMap<String, String> classesHash = new HashMap<String, String>();

    //Look for files in directory to hash, then compare result
    public static HashMap<String,String> hashAll(ArrayList<String> appLists) {
        ClassStructure classStructure = new ClassStructure();

        for (String appPath: appLists) {
            ArrayList<String> classPath = classStructure.getClassPath(appPath);
            File dir = new File(appPath);
            File[] fileList = dir.listFiles();
            if (fileList != null) {
                for (File file: fileList) {
                    String filePath = file.getAbsolutePath();
                    if (file.getName().endsWith(".txt")) {
                        System.out.println("Hashing classes in " + filePath);
                        hashSHA256(classPath,filePath);
                    }
                }
            }
        }

        return classesHash;
    }

    //Hash file and put it into hashmap
    private static void hashSHA256(ArrayList<String> classPath, String filePath) {
        File file = new File(filePath);
        ReplaceIdent ri = new ReplaceIdent();
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
                        String replacedClassStructure = ri.replaceIdentifier(classStructure);
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
    public static void compareHashmap(Connection connection,String query,String outputPath) {        
        try {
            Statement command = connection.createStatement();
            ResultSet result = command.executeQuery(query);

            while (result.next()) {
                String filePath = result.getString("File_Path");
                String hexString = result.getString("Hash_String");

                String outputDir = outputPath;
                outputDir = outputDir + "/" + hexString;
                File getFile = new File(filePath);
                File target = new File(outputDir);
                if (!target.exists()) {
                    target.mkdir();
                }
                FileUtils.copyFileToDirectory(getFile,target);
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

}