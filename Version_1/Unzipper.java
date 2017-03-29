package Version_1;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
 
public class Unzipper {
  
    private static final int BUFFER_SIZE = 4096;

    //Apply unzip method to all files in a given directory
    public static void unzipAll(String dirPath, String destPath, String fileType) {
        File dir = new File(dirPath);
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File file: fileList) {

                String filePath = file.getAbsolutePath();
                String destPath2;
                if (file.getName().endsWith(".jar")) {
                    destPath2 = destPath + File.separator + "Classes";
                } else {
                    destPath2 = destPath + File.separator + file.getName();
                }

                try {
                    if (file.isDirectory()) { //Recall method on directory
                        unzipAll(filePath, destPath2, fileType);
                    }
                    else {
                        System.out.println("Unzipping " + filePath);
                        unzip(filePath, destPath2, fileType);
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
 
    private static void unzip(String zipFilePath, String destPath, String fileType) throws IOException {
        File destDir = new File(destPath);
        //Create output directory if it does not exists
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();

        while (entry != null) {
            String filePath = destPath + File.separator + entry.getName();
            //Get file same as fileType
            if (filePath.endsWith(fileType)) {
                  extractFile(zipIn, filePath);
            }

            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        File file = new File(filePath).getParentFile();

        //Create file path if it does not exists
        if (!file.exists()) {
            file.mkdirs();
        }

        //Get content
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

}