package NSE_Package;

import java.sql.*;

public class Demo {

    public static void main(String[] args) throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/priyanka";
        String username = "root";
        String password = "admin";

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sqlCE;

            PreparedStatement statementCE;

            sqlCE ="select * from closece where close=?";
            statementCE = connection.prepareStatement(sqlCE);
            statementCE.setInt(1,65);

            ResultSet rs = statementCE.executeQuery();
            System.out.println("close equals 65 ");
            while(rs.next()) {
                System.out.print("Comp key: "+rs.getString("compkey")+", ");
                System.out.print("Close: "+rs.getString("close"));
                System.out.println();
            }

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
        System.out.println("Uploaded successfully");
    }
}
