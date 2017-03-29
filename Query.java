import Version_4.*;
import java.sql.*;

public class Query {

    public static void main(String[] args) throws Exception {
        ConnectDB connectDB = new ConnectDB();
		Connection connect = connectDB.getDBConnection();

        // Get number of repeated hash value cases
        String getTotalQuery = "SELECT COUNT(*) COUNT, SUM(COUNT) SUM FROM (SELECT HASH, COUNT(*) COUNT FROM code_hash_improved GROUP BY HASH HAVING COUNT(*) > 1) AS cases";
        ResultSet getTotal = connectDB.runQuery(connect,getTotalQuery);
        getTotal.next();
        int totalCases = getTotal.getInt("COUNT");
        int totalFiles = getTotal.getInt("SUM");
        // Find hash values with more than one occurence
        String groupHashQuery = "SELECT HASH, COUNT(*) COUNT FROM code_hash_improved GROUP BY HASH HAVING COUNT(*) > 1";
        ResultSet groupByHash = connectDB.runQuery(connect,groupHashQuery);
        // Get file paths having the same hash values
        int rowNum = 0;
        String getPathsQuery = "SELECT PATH,HASH FROM code_hash_improved WHERE HASH IN (";
        while(groupByHash.next()) {
            rowNum++;
            System.out.println("Number of cases of code clone: " + rowNum + "/" + totalCases);
            String hashString = groupByHash.getString("HASH");
            getPathsQuery = getPathsQuery + "'" + hashString + "',";
        }
        getPathsQuery = getPathsQuery.substring(0,getPathsQuery.length()-1) + ")";
        System.out.println("Getting file paths of code clone...");
        ResultSet filePaths = connectDB.runQuery(connect,getPathsQuery);
        
        connect.close();
    }
}