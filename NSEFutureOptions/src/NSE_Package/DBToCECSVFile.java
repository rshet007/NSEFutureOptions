package NSE_Package;

import java.sql.*;

public class DBToCECSVFile {


    public static void downloadCEDiff(String outfile) throws SQLException {
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

            String sqlDndCEDiff;
            Statement stmtDndCEDiff = connection.createStatement();
            System.out.println("Staring");
            sqlDndCEDiff="select 'SymbolStrikeprOptiontypExpirydt', 'open', 'close', 'difference', 'contracts', 'valinlakh', 'openint', 'chgin'\n" +
                    "union all\n" +
                    "select cediff.compkey, cediff.open, cediff.close, cediff.diff, cediff.contracts, cediff.valinlakh, cediff.openint, cediff.chgin from cediff \n" +
                    "into outfile '"+outfile+".csv'\n" +
                    "\tFIELDS TERMINATED BY ','\n" +
                    "\tOPTIONALLY ENCLOSED BY '\"'\n" +
                    "\tESCAPED BY ''\n" +
                    "\tLINES TERMINATED BY '\\n'";
            System.out.println(sqlDndCEDiff);
            ResultSet rsDndCE = stmtDndCEDiff.executeQuery(sqlDndCEDiff);
            System.out.println("Downloaded CEDiff File");
            rsDndCE.close();
            
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
