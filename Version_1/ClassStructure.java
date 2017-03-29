package Version_1;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class ClassStructure {

	//Path to use javap program
	static String javapLocation = "/Library/Java/JavaVirtualMachines/jdk1.8.0_101.jdk/Contents/Home/bin/javap";

	public static void getClassStructureAll(String dirPath) {
		File dir = new File(dirPath);
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File file: fileList) {
                String filePath = file.getAbsolutePath();

                if (file.isDirectory()) { //Recall method on directory
                    getClassStructureAll(filePath);
                }
                else if (filePath.endsWith(".class")){
					getClassStructure(filePath);
				}
            }
        }
	}

	private static void getClassStructure(String filePath) {
		try {
			List<String> cmdList = new ArrayList<String>();
			cmdList.add(javapLocation);
			cmdList.add(filePath);

			ProcessBuilder pb = new ProcessBuilder(cmdList);
			Process p = pb.start();
			p.waitFor();
			InputStream is = p.getInputStream();

			ReplaceIdent ri = new ReplaceIdent();
			ri.replaceIdentifier(asString(is), filePath);
			
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
				System.out.println(line);
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