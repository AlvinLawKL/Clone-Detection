import Version_5.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;

public class CloneDetection {

	static String dirPath = "/Users/Alvin/Desktop/Project/Input";
	static String destPath = "/Users/Alvin/Desktop/Project/Output";

	public static void main(String[] args) throws Exception {
		
		
		long startTime = System.nanoTime();
		/*
		// Get all .dex files from APKs
		Unzipper unzipper = new Unzipper();
		unzipper.unzipAll(dirPath, destPath, ".*", ".dex");

		// Convert all .dex fils into .jar files using dex2jar
		DexToJar dexToJar = new DexToJar();
		dexToJar.toJarAll(destPath);

		// Extract all .class files from .jar files
		unzipper.unzipAll(destPath, destPath, ".jar", ".class");
		*/

		// Get all class structure of .class files
		ClassStructure classStructure = new ClassStructure();
		ArrayList<String> appLists = classStructure.getApp(destPath,"Y");

		// Hash all class structures and insert into databse
		Hash hash = new Hash();
		HashMap<String,ArrayList<String>> classesHash = hash.hashAll(appLists);
		
		ConnectDB connectDB = new ConnectDB();
		Connection connect = connectDB.getDBConnection();
		connectDB.insertIntoTable(connect,"code_hash_original",classesHash);
		connect.close();
		

		long endTime = System.nanoTime();
  		long runTime = endTime - startTime;
		double runTimeInSec = (double)runTime/1000000000;
		System.out.println("Time taken " + runTimeInSec);
  	}

}