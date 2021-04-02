package NSE_Package;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class OIAnalyzer {
    public static void createTables(String startDate, String endDate) {
        dropTables();
        OITable(startDate,endDate);
    }

    private static void dropTables() {
        String jdbcURL = "jdbc:mysql://localhost:3306/priyanka";
        String username = "root";
        String password = "admin";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);
            
            Statement stmtDropOISum = connection.createStatement();
            String sqlDropOISum;

            sqlDropOISum="drop table IF EXISTS openIntSum";
            stmtDropOISum.executeUpdate(sqlDropOISum);

            connection.commit();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Previous tables dropped ");
    }

    private static void OITable(String startDate, String endDate) {
        String jdbcURL = "jdbc:mysql://localhost:3306/priyanka";
        String username = "root";
        String password = "admin";
        Connection connection = null;
        int batchSize = 20, count=1;

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sqlOISum;

            PreparedStatement stmtOISum;

            sqlOISum ="create table IF NOT EXISTS openIntSum\r\n"
            		+ "	select fotable.txdate, fotable.symbol, fotable.optiontyp, sum(fotable.openint) as OIsum from fotable \r\n"
            		+ "		where fotable.txdate>=? and fotable.txdate<=? \r\n"
            		+ "        group by fotable.txdate, fotable.symbol,fotable.optiontyp\r\n"
            		+ "        order by fotable.txdate, fotable.symbol, fotable.optiontyp";
            stmtOISum = connection.prepareStatement(sqlOISum);
            stmtOISum.setString(1,startDate);
            stmtOISum.setString(2,endDate);
            stmtOISum.addBatch();
            count++;
            if (count % batchSize == 0)
            	stmtOISum.executeBatch();
            stmtOISum.executeBatch();

            connection.commit();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("OpenIntSum table created successfully");
    }

}
