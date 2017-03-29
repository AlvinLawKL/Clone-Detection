package Measure_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DexToJarMeasure {

	//Path to use dex2jar program
	static String d2jLocation = "/Users/Alvin/Desktop/Clone Detection/dex2jar-2.0/d2j-dex2jar.sh";

	//Apply toJar method to all .dex files in a given directory
	public static ArrayList<ArrayList<Double>> toJarAll(String dirPath) {
        ArrayList<ArrayList<Double>> table = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> size = new ArrayList<Double>();
        ArrayList<Double> t2 = new ArrayList<Double>();
		File dir = new File(dirPath);
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File file: fileList) {
            	String filePath = file.getAbsolutePath();
            	if (file.isDirectory()) {
                    ArrayList<ArrayList<Double>> table2 = toJarAll(filePath);
                    size.addAll(table2.get(0));
                    t2.addAll(table2.get(1));
            	}
                else if (filePath.endsWith(".dex")) {
                    System.out.println("Dex2jar: " + filePath);
                    double fileSizeInMB = (double)file.length()/(1024*1024);
                    size.add(fileSizeInMB);
                    long startTime = System.nanoTime();
                    toJar(filePath);
					long endTime = System.nanoTime();
					long runTime = endTime - startTime;
					double runTimeInSec = (double)runTime/1000000000;
					t2.add(runTimeInSec);
                }
            }
        }
        table.add(size);
        table.add(t2);
        return table;
	}

	private static void toJar(String dexFilePath) {
		try {
			List<String> cmdList = new ArrayList<String>();
			cmdList.add(d2jLocation);
			cmdList.add(dexFilePath);

			ProcessBuilder pb = new ProcessBuilder(cmdList);
			File outputDir = new File(dexFilePath).getParentFile();
			pb.directory(outputDir);
			Process p = pb.start();
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}