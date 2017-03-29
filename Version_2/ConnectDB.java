package Version_2;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ConnectDB {

    public static Connection getDBConnection() { 
      try {
        // Step 1: "Load" the JDBC driver
        Class.forName("com.mysql.jdbc.Driver"); 

        // Step 2: Establish the connection to the database 
        String serverName = "localhost";
        String databaseName = "clone_detection";
        String url = "jdbc:mysql://" + serverName + "/" + databaseName + "?autoReconnect=true&useSSL=false"; 

        String username = "root";
        String password = "rootPassword";
        Connection connection = null;
        connection = DriverManager.getConnection(url,username,password); 
        return connection; 
      }
      catch (Exception e) {
        System.err.println(e); 
      }
      return null;
    }

    public static void insertIntoTable(Connection connection,String tableName,HashMap<String,String> data) {
      String updateStart = "INSERT INTO " + tableName + " VALUES ";
      String updateString = updateStart;
      int count = 0;
      for (Map.Entry<String,String> entry : data.entrySet()) {
        String classPath = entry.getKey();
        String hashString = entry.getValue();
        System.out.println("Ready to insert " + classPath);
        
        updateString = updateString + "('" + classPath + "','" + hashString + "')";
        count++;

        if ((count != 0 && count%1000 == 0) || count == data.size()) {
          try {
            Statement command = connection.createStatement();
            command.executeUpdate(updateString);
          }
          catch (Exception e) {
            System.err.println(e);
          }
          updateString = updateStart;
        }
        else {
          updateString = updateString + ",";
        }
      }
    }

}