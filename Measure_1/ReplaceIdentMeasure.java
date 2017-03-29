package Measure_1;

import ANTLR.*;
import org.antlr.v4.runtime.*;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Object;

public class ReplaceIdentMeasure {

  static int identifer = 100;

  public static String replaceIdentifier (String string) {
    CharStream input = new ANTLRInputStream(string);
    JavaLexer lexer = new JavaLexer(input);
    List<String> identList = new ArrayList<String>();
    for (Token token = lexer.nextToken(); token.getType() != Token.EOF; token = lexer.nextToken()) {
      if (token.getType() == identifer && !identList.contains(token.getText())) {
        identList.add(token.getText());
      }
    }

    for (String ident : identList) {
      String lookFor =  "\\b" + ident + "\\b"; //Replace exact match
      string = string.replaceAll(lookFor, "replaced");
    }

    return string;
  }

  //Output String into a text file
  private static void writeTxtFile (String string, String filePath) {
    File file = new File(filePath);
          
    try (FileOutputStream fos = new FileOutputStream(file, false)) {
      if (!file.exists()) {
        file.createNewFile();
      }

      byte[] stringInBytes = string.getBytes();

      fos.write(stringInBytes);
      fos.flush();
      fos.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}