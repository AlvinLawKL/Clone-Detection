package Measure_1;

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

public class ClassStructureMeasure {

	//Path to use javap program, use to get class structure from .class file
	static String javapLocation = "/Library/Java/JavaVirtualMachines/jdk1.8.0_101.jdk/Contents/Home/bin/javap";

	public static ArrayList<String> getApp(String path) {
		ArrayList<String> appLists = new ArrayList<String>();
		File dir = new File(path);
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File file: fileList) {
                String filePath = file.getAbsolutePath();

                if (filePath.endsWith("Classes")) {
					String outputPath = file.getParent();
					appLists.add(outputPath);
					ArrayList<String> classLists = getClassPath(filePath);
					getClassStructure(outputPath, classLists);
                }
				else if (file.isDirectory()) {
					ArrayList<String> appLists2 = getApp(filePath);
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
			System.out.println(path);
			System.out.println(classLists.size());
			String outputPath = "";
			int i = 1;

			List<String> cmdList = new ArrayList<String>();
			cmdList.add(javapLocation);
			for (int num = 0; num < classLists.size(); num++) {
				cmdList.add(classLists.get(num));

				if ((num != 0 && num % 1000 == 0) | num == classLists.size()-1) {
					ProcessBuilder pb = new ProcessBuilder(cmdList);
					outputPath = path + "/test" + i + ".txt";
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

			//ReplaceIdent ri = new ReplaceIdent();
				
			//ri.replaceIdentifer(asString(is), filePath);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	//Convert Inputstream into String
	private static String asString(InputStream is) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
			}
			String s = sb.toString();
			br.close();
			return s;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}