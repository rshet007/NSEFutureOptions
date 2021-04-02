package NSE_Package;

import java.sql.*;

public class DBToPECSVFile {
    

    public static void downloadPEDiff(String outfile)  throws SQLException {
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

            String sqlDndPEDiff;
            Statement stmtDndPEDiff = connection.createStatement();
							  
            sqlDndPEDiff="select 'SymbolStrikeprOptiontypExpirydt', 'open', 'close', 'difference', 'contracts', 'valinlakh', 'openint', 'chgin'\n" +
                    "union all\n" +
                    "select pediff.compkey, pediff.open, pediff.close, pediff.diff, pediff.contracts, pediff.valinlakh, pediff.openint, pediff.chgin from pediff \n" +
                    "into outfile '"+outfile+".csv'\n" +
                    "\tFIELDS TERMINATED BY ','\n" +
                    "\tOPTIONALLY ENCLOSED BY '\"'\n" +
                    "\tESCAPED BY ''\n" +
                    "\tLINES TERMINATED BY '\\n'";
			System.out.println(sqlDndPEDiff);								 
            ResultSet rsDndPE = stmtDndPEDiff.executeQuery(sqlDndPEDiff);
            System.out.println("Downloaded PEDiff File");
            rsDndPE.close();

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
