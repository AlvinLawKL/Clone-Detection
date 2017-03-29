package Version_5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DexToJar {

	//Path to use dex2jar program
	static String d2jLocation = "/Users/Alvin/Desktop/Clone Detection/dex2jar-2.0/d2j-dex2jar.sh";

	//Apply toJar method to all .dex files in a given directory
	public static void toJarAll(String dirPath) {
		File dir = new File(dirPath);
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File file: fileList) {
            	String filePath = file.getAbsolutePath();
            	if (file.isDirectory()) {
            		toJarAll(filePath);
            	}
                else if (filePath.endsWith(".dex")) {
                    toJar(filePath);
                }
            }
        }
	}

	private static void toJar(String dexFilePath) {
		System.out.println("dex2jar: " + dexFilePath);
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