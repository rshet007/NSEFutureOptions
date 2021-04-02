package NSE_Package;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class CsvToDBInserter {
    public static void upload(String startFileName) throws SQLException{
    	uploadOpenData(startFileName);
        //prerequisites();
    	//uploadOpenData(endFileName,2);
        //nonDupsAndDiff();
    }

    @SuppressWarnings("deprecation")
	public static void uploadOpenData(String startFileName) throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/priyanka";
        String username = "root";
        String password = "admin";
        String csvFilePath = startFileName;
        int batchSizeCE = 1, batchSizePE=1, batchSize=1;
        int count=1;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sqlCreate;
            String sqlCE;
            String sqlPE;
            String compkey;
            
            PreparedStatement stmtCreate;
            PreparedStatement statementCE;
            PreparedStatement statementPE;
            
            sqlCreate ="create table IF NOT EXISTS fotable (\r\n"
            		+ "  `slno` int NOT NULL auto_increment,\r\n"
            		+ "  `compkey` varchar(200),\r\n"
            		+ "  `symbol` varchar(45) DEFAULT NULL,\r\n"
            		+ "  `expirydate` date DEFAULT NULL,\r\n"
            		+ "  `strikepr` int DEFAULT NULL,\r\n"
            		+ "  `optiontyp` varchar(10) DEFAULT NULL,\r\n"
            		+ "  `open` int DEFAULT NULL,\r\n"
            		+ "  `high` int DEFAULT NULL,\r\n"
            		+ "  `low` int DEFAULT NULL,\r\n"
            		+ "  `close` int DEFAULT NULL,\r\n"
            		+ "  `settlepr` int DEFAULT NULL,\r\n"
            		+ "  `contracts` int DEFAULT NULL,\r\n"
            		+ "  `valinlakh` int DEFAULT NULL,\r\n"
            		+ "  `openint` int DEFAULT NULL,\r\n"
            		+ "  `chgin` int DEFAULT NULL,\r\n"
            		+ "  `txdate` date DEFAULT NULL,\r\n"
            		+ "  PRIMARY KEY (`slno`)\r\n"
            		+ ");";
            stmtCreate = connection.prepareStatement(sqlCreate);
            stmtCreate.addBatch();
            count++;
            if (count % batchSize == 0)
            	stmtCreate.executeBatch();
            stmtCreate.executeBatch();

            sqlCE ="INSERT INTO fotable (compkey,symbol,expirydate,strikepr,optiontyp,open,high,low,close,settlepr,contracts,valinlakh,openint,chgin,txdate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statementCE = connection.prepareStatement(sqlCE);
            sqlPE ="INSERT INTO fotable (compkey,symbol,expirydate,strikepr,optiontyp,open,high,low,close,settlepr,contracts,valinlakh,openint,chgin,txdate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statementPE = connection.prepareStatement(sqlPE);
            System.out.println("Insert fo prepare stmts for CE and PE done");

            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;

            lineReader.readLine(); // skip header line
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                compkey = data[1]+" "+data[3]+" "+data[4]+" "+data[2];
                
                String strDate=data[2];
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                java.util.Date varDate=null;

                if(data[0].equals("OPTSTK") && data[4].equals("CE") && !(data[5].equals("0"))){
                    statementCE.setString(1,compkey);
                    statementCE.setString(2, data[1]);
                  
                    try {
                    	System.out.println(data[2]);
                        varDate=dateFormat.parse(strDate);
                        java.sql.Date sqlDate = new java.sql.Date(varDate.getTime());
                        System.out.println("Check 1: " + sqlDate + " openint - "+ data[12]+" chgin - "+data[13]);
                        statementCE.setDate(3, sqlDate);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        statementCE.setDate(3, new java.sql.Date(java.util.Date.parse("00-00-0000")));
                    }

                    statementCE.setFloat(4, Float.parseFloat(data[3]));
                    statementCE.setString(5, data[4]);
                    statementCE.setFloat(6, Float.parseFloat(data[5]));
                    statementCE.setFloat(7, Float.parseFloat(data[6]));
                    statementCE.setFloat(8, Float.parseFloat(data[7]));
                    statementCE.setFloat(9, Float.parseFloat(data[8]));
                    statementCE.setFloat(10, Float.parseFloat(data[9]));
                    statementCE.setFloat(11, Float.parseFloat(data[10]));
                    statementCE.setFloat(12, Float.parseFloat(data[11]));
                    statementCE.setFloat(13, Float.parseFloat(data[12]));
                    statementCE.setFloat(14, Float.parseFloat(data[13]));
 
                    //Timestamp sqlTimestampStartOpen = Timestamp.valueOf(data[14]);
                    //statementCE.setTimestamp(15, sqlTimestampStartOpen);
                    try {
                    	strDate=data[14];
                    	varDate=dateFormat.parse(strDate);
                        java.sql.Date sqlDate = new java.sql.Date(varDate.getTime());
                        System.out.println("Check 2: " + sqlDate + " openint - "+ data[12]+" chgin - "+data[13]);
                        statementCE.setDate(15, sqlDate);
                    } catch(Exception e) {
                    	System.out.println(e);
                    	statementCE.setDate(15, new java.sql.Date(java.util.Date.parse("00-00-0000")));
                    }
 
                    //String openprice = data[5];
                    //statementCE.setFloat(4, Float.parseFloat(data[5]));

                    statementCE.addBatch();
                    count++;
                    if (count % batchSizeCE == 0) {
                        statementCE.executeBatch();
                    }
                    System.out.println("Inserted CE data into fotable Table");
                }
                else if(data[0].equals("OPTSTK") && data[4].equals("PE") && !(data[5].equals("0"))){
                    statementPE.setString(1,compkey);
                    statementPE.setString(2, data[1]);

                    //Timestamp sqlTimestampExpiryDate = Timestamp.valueOf(data[2]);
                    //statementPE.setTimestamp(3, sqlTimestampExpiryDate);
                    
                    try {
                    	System.out.println(data[2]);
                        varDate=dateFormat.parse(strDate);
                        java.sql.Date sqlDate = new java.sql.Date(varDate.getTime());
                        System.out.println("Check 3: " + sqlDate + " openint - "+ data[12]+" chgin - "+data[13]);
                        statementPE.setDate(3, sqlDate);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        statementPE.setDate(3, new java.sql.Date(java.util.Date.parse("00-00-0000")));
                    }

                    statementPE.setFloat(4, Float.parseFloat(data[3]));
                    statementPE.setString(5, data[4]);
                    statementPE.setFloat(6, Float.parseFloat(data[5]));
                    statementPE.setFloat(7, Float.parseFloat(data[6]));
                    statementPE.setFloat(8, Float.parseFloat(data[7]));
                    statementPE.setFloat(9, Float.parseFloat(data[8]));
                    statementPE.setFloat(10, Float.parseFloat(data[9]));
                    statementPE.setFloat(11, Float.parseFloat(data[10]));
                    statementPE.setFloat(12, Float.parseFloat(data[11]));
                    statementPE.setFloat(13, Float.parseFloat(data[12]));
                    statementPE.setFloat(14, Float.parseFloat(data[13]));

                    //Timestamp sqlTimestampStartOpen = Timestamp.valueOf(data[14]);
                    //statementPE.setTimestamp(15, sqlTimestampStartOpen);
                    try {
                    	strDate=data[14];
                    	varDate=dateFormat.parse(strDate);
                        java.sql.Date sqlDate = new java.sql.Date(varDate.getTime());
                        System.out.println("Check 4: " + sqlDate + " openint - "+ data[12]+" chgin - "+data[13]);
                        statementPE.setDate(15, sqlDate);
                    } catch(Exception e) {
                    	System.out.println(e);
                    	statementPE.setDate(15, new java.sql.Date(java.util.Date.parse("00-00-0000")));
                    }

                    //String openprice = data[5];
                    //statementCE.setFloat(4, Float.parseFloat(data[5]));

                    statementPE.addBatch();
                    count++;
                    if (count % batchSizePE == 0) {
                        statementPE.executeBatch();
                    }
                    System.out.println("Inserted PE data into fotable Table");
                }
            }
            lineReader.close();

            //execute the remaining queries
            statementCE.executeBatch();
            statementPE.executeBatch();

            connection.commit();
            connection.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println(e);
        }catch (SQLException ex) {
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
