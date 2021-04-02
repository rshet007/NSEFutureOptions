package NSE_Package;

import java.sql.SQLException;

public class MainFOAnalyzer {
    public static void main(String[] args) throws SQLException {
        //FOAnalyzer foAnalyzer=new FOAnalyzer();
        FOAnalyzer.createTables("2021-02-12","2021-02-17");
    }
}
