package NSE_Package;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBToOICSVFile {


    public static void downloadOISum(String outfile) throws SQLException {
    	System.out.println("Will download to : "+outfile);
    	outfile = outfile.replace("\\","//");
    	System.out.println(outfile);
        String jdbcURL = "jdbc:mysql://localhost:3306/priyanka";
        String username = "root";
        String password = "admin";

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sqlDndOISum;
            Statement stmtDndOISum = connection.createStatement();
            System.out.println("Starting");
            sqlDndOISum="select 'txdate', 'symbol', 'optiontyp', 'OIsum'\r\n"
            		+ "	union all\r\n"
            		+ "	select openIntSum.txdate, openIntSum.symbol, openIntSum.optiontyp, openIntSum.OIsum  from openIntSum \r\n"
            		+ "		into outfile '"+outfile+".csv'\r\n"
            		+ "		FIELDS TERMINATED BY ','\r\n"
            		+ "		OPTIONALLY ENCLOSED BY '\"'\r\n"
            		+ "		ESCAPED BY ''\r\n"
            		+ "		LINES TERMINATED BY '\\n';";
            System.out.println(sqlDndOISum);
            ResultSet rsDndOI = stmtDndOISum.executeQuery(sqlDndOISum);
            System.out.println("Downloaded OISum File");
            rsDndOI.close();
            
            connection.commit();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
