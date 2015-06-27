 
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcSQLiteConnection {
 
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:users.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
                
                String query = "CREATE TABLE MY_USER(" +
                		"id INT PRIMARY KEY, " +
                		"username TEXT, " +
                		"password TEXT, " +
                		"mrp0 INT, " +
                		"mrp1 INT, " +
                		"mrp2 INT, " +
                		"mrp3 INT, " +
                		"mrp4 INT)";
           PreparedStatement s = conn.prepareStatement(query);
           s.execute();
                
                
                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}