import Version_4.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;

public class Measure {

	static String dirPath = "/Users/Alvin/Desktop/Test/Input";
	static String destPath = "/Users/Alvin/Desktop/Test/Output";
	static String d2jLocation = "/Users/Alvin/Desktop/FYP/Tool/Processor/dex2jar-2.0/d2j-dex2jar.sh";
	static String javapLocation = "/Library/Java/JavaVirtualMachines/jdk1.8.0_101.jdk/Contents/Home/bin/javap";

	public static void main(String[] args) throws Exception {
		long startTime = System.nanoTime();
		
		// Get all .dex files from APKs
		Unzipper unzipper = new Unzipper();
		unzipper.unzipAll(dirPath, destPath, ".*", ".dex");

		// Convert all .dex fils into .jar files using dex2jar
		DexToJar dexToJar = new DexToJar();
		dexToJar.toJarAll(destPath,d2jLocation);

		// Extract all .class files from .jar files
		unzipper.unzipAll(destPath, destPath, ".jar", ".class");

		// Get all class signatures of .class files
		ClassStructure classStructure = new ClassStructure();
		ArrayList<String> appLists = classStructure.getApp(destPath,"N",javapLocation);

		// Hash all class signatures and insert into databse
		Hash hash = new Hash();
		HashMap<String,ArrayList<String>> classesHash = hash.hashAll(appLists);
		
		ConnectDB connectDB = new ConnectDB();
		Connection connect = connectDB.getDBConnection();
		connectDB.insertIntoTable(connect,"time_1",classesHash);
		connect.close();

		long endTime = System.nanoTime();
  		long runTime = endTime - startTime;
		double runTimeInSec = (double)runTime/1000000000;
		System.out.println("Run time is "+ runTimeInSec);
  	}

}