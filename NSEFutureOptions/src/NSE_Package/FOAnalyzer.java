package NSE_Package;

import java.sql.*;

public class FOAnalyzer {
    public static void createTables(String startDate, String endDate) {
        dropTables();
        ceTables(startDate,endDate);
        peTables(startDate,endDate);
        updateDiffTables(); //update and display
    }

    private static void dropTables() {
        String jdbcURL = "jdbc:mysql://localhost:3306/priyanka";
        String username = "root";
        String password = "admin";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);
            Statement stmtDropCloseCE = connection.createStatement();
            Statement stmtDropOpenCE = connection.createStatement();
            Statement stmtDropCEDiff = connection.createStatement();
            Statement stmtDropClosePE = connection.createStatement();
            Statement stmtDropOpenPE = connection.createStatement();
            Statement stmtDropPEDiff = connection.createStatement();

            String sqlDropCloseCE;
            String sqlDropOpenCE;
            String sqlDropCEDiff;
            String sqlDropClosePE;
            String sqlDropOpenPE;
            String sqlDropPEDiff;

            sqlDropCloseCE="drop table IF EXISTS closeCE";
            stmtDropCloseCE.executeUpdate(sqlDropCloseCE);

            sqlDropOpenCE="drop table IF EXISTS openCE";
            stmtDropOpenCE.executeUpdate(sqlDropOpenCE);

            sqlDropCEDiff="drop table IF EXISTS CEDiff";
            stmtDropCEDiff.executeUpdate(sqlDropCEDiff);

            sqlDropClosePE="drop table IF EXISTS closePE";
            stmtDropClosePE.executeUpdate(sqlDropClosePE);

            sqlDropOpenPE="drop table IF EXISTS openPE";
            stmtDropOpenPE.executeUpdate(sqlDropOpenPE);

            sqlDropPEDiff="drop table IF EXISTS PEDiff";
            stmtDropPEDiff.executeUpdate(sqlDropPEDiff);

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

    private static void ceTables(String startDate, String endDate) {
        String jdbcURL = "jdbc:mysql://localhost:3306/priyanka";
        String username = "root";
        String password = "admin";
        Connection connection = null;
        int batchSize = 20, count=1;

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sqlCloseCE;
            String sqlOpenCE;
            String sqlCEDiff;

            PreparedStatement stmtCloseCE;
            PreparedStatement stmtOpenCE;
            PreparedStatement stmtCEDiff;

            sqlCloseCE ="create table IF NOT EXISTS closeCE\n" +
                    "\tselect fotable.compkey, max(fotable.close) as close, fotable.contracts, fotable.valinlakh, fotable.openint, fotable.chgin from fotable\n" +
                    "\twhere fotable.txdate>=? and fotable.txdate<=? and fotable.optiontyp = 'CE'\n" +
                    "\tgroup by fotable.compkey";
            stmtCloseCE = connection.prepareStatement(sqlCloseCE);
            stmtCloseCE.setString(1,startDate);
            stmtCloseCE.setString(2,endDate);
            stmtCloseCE.addBatch();
            count++;
            if (count % batchSize == 0)
                stmtCloseCE.executeBatch();
            stmtCloseCE.executeBatch();

            sqlOpenCE ="create table IF NOT EXISTS openCE\n" +
                    "\tselect fotable.compkey, close as open, fotable.contracts, fotable.valinlakh, fotable.openint, fotable.chgin from fotable\n" +
                    "\twhere fotable.txdate=? and fotable.optiontyp = 'CE'";
            stmtOpenCE = connection.prepareStatement(sqlOpenCE);
            stmtOpenCE.setString(1,startDate);
            stmtOpenCE.addBatch();
            count++;
            if (count % batchSize == 0)
                stmtOpenCE.executeBatch();
            stmtOpenCE.executeBatch();

            sqlCEDiff ="create table CEdiff\n" +
                    "\tselect A.compkey, A.open, B.close, 0 as diff, A.contracts, A.valinlakh, A.openint, A.chgin from opence as A, closece as B\n" +
                    "\twhere A.compkey=B.compkey";
            stmtCEDiff = connection.prepareStatement(sqlCEDiff);
            stmtCEDiff.addBatch();
            count++;
            if (count % batchSize == 0)
                stmtCEDiff.executeBatch();
            stmtCEDiff.executeBatch();

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
        System.out.println("closeCE created successfully");
    }

    private static void peTables(String startDate, String endDate) {
        String jdbcURL = "jdbc:mysql://localhost:3306/priyanka";
        String username = "root";
        String password = "admin";
        Connection connection = null;
        int batchSize = 20, count=1;

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sqlClosePE;
            String sqlOpenPE;
            String sqlPEDiff;

            PreparedStatement stmtClosePE;
            PreparedStatement stmtOpenPE;
            PreparedStatement stmtPEDiff;

            sqlClosePE ="create table IF NOT EXISTS closePE\n" +
                    "\tselect fotable.compkey, min(fotable.close) as close, fotable.contracts, fotable.valinlakh, fotable.openint, fotable.chgin from fotable\n" +
                    "\twhere fotable.txdate>=? and fotable.txdate<=? and fotable.optiontyp = 'PE'\n" +
                    "\tgroup by fotable.compkey";
            stmtClosePE = connection.prepareStatement(sqlClosePE);
            stmtClosePE.setString(1,startDate);
            stmtClosePE.setString(2,endDate);
            stmtClosePE.addBatch();
            count++;
            if (count % batchSize == 0)
                stmtClosePE.executeBatch();
            stmtClosePE.executeBatch();

            sqlOpenPE ="create table IF NOT EXISTS openPE\n" +
                    "\tselect fotable.compkey, close as open, fotable.contracts, fotable.valinlakh, fotable.openint, fotable.chgin from fotable\n" +
                    "\twhere fotable.txdate=? and fotable.optiontyp = 'PE'";
            stmtOpenPE = connection.prepareStatement(sqlOpenPE);
            stmtOpenPE.setString(1,startDate);
            stmtOpenPE.addBatch();
            count++;
            if (count % batchSize == 0)
                stmtOpenPE.executeBatch();
            stmtOpenPE.executeBatch();

            sqlPEDiff ="create table PEdiff\n" +
                    "\tselect A.compkey, A.open, B.close, 0 as diff, A.contracts, A.valinlakh, A.openint, A.chgin from openpe as A, closepe as B\n" +
                    "\twhere A.compkey=B.compkey;";
            stmtPEDiff = connection.prepareStatement(sqlPEDiff);
            stmtPEDiff.addBatch();
            count++;
            if (count % batchSize == 0)
                stmtPEDiff.executeBatch();
            stmtPEDiff.executeBatch();

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
        System.out.println("closePE created successfully");
    }

    private static void updateDiffTables() {
        String jdbcURL = "jdbc:mysql://localhost:3306/priyanka";
        String username = "root";
        String password = "admin";
        Connection connection = null;
        int batchSize = 20, count=1;

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sqlUpdCEDiff, sqlDispCEDiff;
            String sqlUpdPEDiff, sqlDispPEDiff;

            PreparedStatement stmtUpdCEDiff;
            Statement stmtDispCEDiff = connection.createStatement();
            PreparedStatement stmtUpdPEDiff;
            Statement stmtDispPEDiff = connection.createStatement();

            sqlUpdCEDiff ="update CEdiff\n" +
                    "\tset diff=close-open";
            stmtUpdCEDiff = connection.prepareStatement(sqlUpdCEDiff);
            stmtUpdCEDiff.addBatch();
            count++;
            if (count % batchSize == 0)
                stmtUpdCEDiff.executeBatch();
            stmtUpdCEDiff.executeBatch();

            sqlDispCEDiff = "select * from CEdiff order by diff desc";
            ResultSet rsCE = stmtDispCEDiff.executeQuery(sqlDispCEDiff);
            while(rsCE.next()){
                String compkey=rsCE.getString("compkey");
                int open  = rsCE.getInt("open");
                int close  = rsCE.getInt("close");
                int diff  = rsCE.getInt("diff");

                System.out.print("compkey: " + compkey);
                System.out.print(", open: " + open);
                System.out.print(", close: " + close);
                System.out.println(", Last: " + diff);
            }
            System.out.println("Executed display CEDiff");
            rsCE.close();

            sqlUpdPEDiff ="update PEdiff\n" +
                    "\tset diff=close-open";
            stmtUpdPEDiff = connection.prepareStatement(sqlUpdPEDiff);
            stmtUpdPEDiff.addBatch();
            count++;
            if (count % batchSize == 0)
                stmtUpdPEDiff.executeBatch();
            stmtUpdPEDiff.executeBatch();

            sqlDispPEDiff = "select * from PEdiff order by diff desc";
            ResultSet rsPE = stmtDispPEDiff.executeQuery(sqlDispPEDiff);
            while(rsPE.next()){
                String compkey=rsPE.getString("compkey");
                int open  = rsPE.getInt("open");
                int close  = rsPE.getInt("close");
                int diff  = rsPE.getInt("diff");

                System.out.print("compkey: " + compkey);
                System.out.print(", open: " + open);
                System.out.print(", close: " + close);
                System.out.println(", Last: " + diff);
            }
            System.out.println("Executed display PEDiff");
            rsPE.close();

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
        System.out.println("Diff tables updated successfully");

    }



}
