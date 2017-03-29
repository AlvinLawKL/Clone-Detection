import Measure_1.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileOutputStream;

public class Measure {

	static String dirPath = "/Users/Alvin/Desktop/folder";
	static String destPath = "/Users/Alvin/Desktop/timeMeasure";

	private static void writeTxtFile (ArrayList<ArrayList<Double>> table, String filePath) {
    File file = new File(filePath);
          
    try (FileOutputStream fos = new FileOutputStream(file, false)) {
      if (!file.exists()) {
        file.createNewFile();
      }

	  byte[] title = (String.format("%20s %20s \r\n", "Size", "Time")).getBytes();
	  fos.write(title);

	  ArrayList<Double> size = table.get(0);
	  ArrayList<Double> t1 = table.get(1);
	  for (int i = 0; i < size.size(); i++) {
		  String sizeString = size.get(i).toString();
		  String t1String = t1.get(i).toString();
		  byte[] combineString = (String.format("%20s %20s \r\n", sizeString, t1String)).getBytes();
		  fos.write(combineString);
	  }

      fos.flush();
      fos.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

	public static void main(String[] args) throws Exception {
		//Get all .dex files from APK
		//UnzipperMeasure unzipper = new UnzipperMeasure();
		//ArrayList<ArrayList<Double>> t1Table = unzipper.unzipAll(dirPath, destPath, ".*", ".dex");
		//String t1DestPath = destPath + File.separator + "getDex.txt";
		//writeTxtFile(t1Table,t1DestPath);

		//Convert all .dex fils into .jar files
		//DexToJarMeasure dexToJar = new DexToJarMeasure();
		//ArrayList<ArrayList<Double>> t2Table = dexToJar.toJarAll(destPath);
		//String t2DestPath = destPath + File.separator + "dexToJar.txt";
		//writeTxtFile(t2Table,t2DestPath);

		//Extract all .class files from .jar files
		//ArrayList<ArrayList<Double>> t3Table = unzipper.unzipAll(destPath, destPath, ".jar", ".class");
		//String t3DestPath = destPath + File.separator + "getClasses.txt";
		//writeTxtFile(t3Table,t3DestPath);

		//Get all class structure of .class files
		
		ClassStructureMeasure classStructure = new ClassStructureMeasure();
		long startTime = System.nanoTime();
		ArrayList<String> appLists = classStructure.getApp(destPath);
		long endTime = System.nanoTime();
  		long runTime = endTime - startTime;
		double runTimeInSec = (double)runTime/1000000000;
		for (String test: appLists) {
			System.out.println(test);
		}
		System.out.println("Run time is "+ runTimeInSec);
		
		/*
		ClassStructureMeasure classStructure = new ClassStructureMeasure();
		ArrayList<ArrayList<Double>> t4Table = classStructure.getClassStructureAll(destPath);
		String t4DestPath = destPath + File.separator + "getClassStructureAndReplace.txt";
		writeTxtFile(t4Table,t4DestPath);
		*/

		HashMeasure hash = new HashMeasure();
		hash.hashAll(appLists);
  	}

}