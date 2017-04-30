package Version_5;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class ConnectDB {
    public static Connection getDBConnection(String database) { 
      try {
        // "Load" the JDBC driver
        Class.forName("org.postgresql.Driver"); 

        // Establish the connection to the database 
        String serverName = "localhost";
        String databaseName = database;
        String url = "jdbc:postgresql://" + serverName + "/" + databaseName + "?autoReconnect=true&useSSL=false"; 

        String username = "postgres";
        String password = "pw4DB";
        Connection connection = null;
        connection = DriverManager.getConnection(url,username,password); 
        return connection; 
      }
      catch (Exception e) {
        System.err.println(e); 
      }
      return null;
    }

    public static void insertIntoTable(Connection connection,String tableName,HashMap<String,ArrayList<String>> data) {
      String updateStart = "INSERT INTO " + tableName + " VALUES ";
      String updateString = updateStart;
      int count = 0;
      int total = data.size();
      for (Map.Entry<String,ArrayList<String>> entry : data.entrySet()) {
        String classPath = entry.getKey();
        ArrayList<String> info = entry.getValue();
        
        updateString = updateString + "('" + classPath + "','" + info.get(0) + "','" + info.get(1) + "')";
        count++;

        if ((count != 0 && count%1000 == 0) || count == total) {
          try {
            System.out.println("Inserting data up to: " + count + "/" + total);
            Statement command = connection.createStatement();
            command.executeUpdate(updateString);
            updateString = updateStart;
          }
          catch (Exception e) {
            System.err.println(e);
          }
        }
        else {
          updateString = updateString + ",";
        }
      }
    }
}