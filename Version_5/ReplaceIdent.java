 package Version_5;

import ANTLR.*;
import org.antlr.v4.runtime.*;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Object;

public class ReplaceIdent {

  static int identifer = 100;

  public static String replaceIdentifier(String string) {
    string = string.replaceAll("[$]",""); //Remove "$" symbol
    string = string.replaceAll("[.]",""); //Remove "."
    CharStream input = new ANTLRInputStream(string);
    JavaLexer lexer = new JavaLexer(input);
    List<String> identList = new ArrayList<String>();
    for (Token token = lexer.nextToken(); token.getType() != Token.EOF; token = lexer.nextToken()) {
      System.out.println(token);
      if (token.getType() == identifer && !identList.contains(token.getText())) {
        identList.add(token.getText());
      }
    }

    for (String ident : identList) {
      String lookFor =  "\\b" + ident + "\\b"; //Replace exact match
      string = string.replaceAll(lookFor, "rp");
    }

    return string;
  }

  public static void main (String[] args) {
    replaceIdentifier("int x;");
  }
}