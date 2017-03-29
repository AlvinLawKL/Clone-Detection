package Version_3;

import ANTLR.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.lang.Class;

public class ClassStructure {

	//Path to use javap program, use to get class structure from .class file
	static String javapLocation = "/Library/Java/JavaVirtualMachines/jdk1.8.0_101.jdk/Contents/Home/bin/javap";

	public static ArrayList<String> getApp(String path,String appPathOnly) {
		// path -- path of folder to apply this method
		// appPathOnly(Y/N) -- "N" to get the class structures

		ArrayList<String> appLists = new ArrayList<String>();
		File dir = new File(path);
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File file: fileList) {
                String filePath = file.getAbsolutePath();

                if (filePath.endsWith("Classes")) {
					String outputPath = file.getParent();
					appLists.add(outputPath);
					
					if (appPathOnly == "N") {
						ArrayList<String> classLists = getClassPath(filePath);
						System.out.println("Getting class structures " + outputPath);
						getClassStructure(outputPath, classLists);
					}
					
                }
				else if (file.isDirectory()) {
					ArrayList<String> appLists2 = getApp(filePath,appPathOnly);
					appLists.addAll(appLists2);
				}
            }
        }
        return appLists;
	}

	public static ArrayList<String> getClassPath(String path) {
		ArrayList<String> classLists = new ArrayList<String>();
		File dir = new File(path);
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File file: fileList) {
                String filePath = file.getAbsolutePath();

                if (file.isDirectory()) {
					ArrayList<String> classLists2 = getClassPath(filePath);
					classLists.addAll(classLists2);
                }
                else if (filePath.endsWith(".class")){
					classLists.add(filePath);
				}
            }
        }
        return classLists;
	}

	public static void getClassStructure(String path, ArrayList<String> classLists) {
		try {
			String outputPath = "";
			int i = 1;

			List<String> cmdList = new ArrayList<String>();
			cmdList.add(javapLocation);
			for (int num = 0; num < classLists.size(); num++) {
				cmdList.add(classLists.get(num));

				if ((num != 0 && num % 1000 == 0) | num == classLists.size()-1) {
					ProcessBuilder pb = new ProcessBuilder(cmdList);
					outputPath = path + "/classStructure" + i + ".txt";
					i++;

					File file = new File(outputPath);
					if (!file.exists()) {
						file.createNewFile();
					}
					
					pb.redirectOutput(file);
					Process p = pb.start();
					p.waitFor();

					cmdList.clear();
					cmdList.add(javapLocation);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}