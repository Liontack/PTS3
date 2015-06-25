package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.User;
import model.UserManagement;

public class DatabaseMediator implements StorageMediator {

    private Connection conn;

    public Set<User> load() {
    	System.out.println("DatabaseMediator loading");
    	
    	Set<User> users = new HashSet<>();
    	
        try {
            initConnection();
            
            // Get users
            String query = "SELECT * FROM MY_USER";
            Statement s = conn.createStatement();
            
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");// encrypted password
                int mrp0 = rs.getInt("mrp0");
                int mrp1 = rs.getInt("mrp1");
                int mrp2 = rs.getInt("mrp2");
                int mrp3 = rs.getInt("mrp3");
                int mrp4 = rs.getInt("mrp4");
                int[] mostRecentPoints = new int[]{ mrp0, mrp1, mrp2, mrp3, mrp4 };
                
                users.add(new User(id, username, password, false, mostRecentPoints));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
                    
        closeConnection();
        
        return users;
    }

    public void save() {
    	System.out.println("DatabaseMediator saving");
    	
        try{
            initConnection();
            
            /*// Create table
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
            */
            
            // Delete all users in table
            String queryx = "DELETE FROM MY_USER";
	        PreparedStatement sx = conn.prepareStatement(queryx);
	        sx.execute();
            
            // Save the users
            for(User user : UserManagement.getUsers()){
                int id = user.getID();
                String username = user.getUsername();
                String password = user.getPassword();
                int[] mostRecentPoints = user.getMostRecentPoints();
            	
                String query = "INSERT INTO MY_USER(id, username, password, mrp0, mrp1, mrp2, mrp3, mrp4)"
                             + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement s = conn.prepareStatement(query);
                s.setInt(1, id);
                s.setString(2, username);
                s.setString(3, password);
                s.setInt(4, mostRecentPoints[0]);
                s.setInt(5, mostRecentPoints[1]);
                s.setInt(6, mostRecentPoints[2]);
                s.setInt(7, mostRecentPoints[3]);
                s.setInt(8, mostRecentPoints[4]);
                s.execute();
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        closeConnection();
    }
    
    
    
    private void initConnection() throws SQLException {
    	try{
    		Class.forName("org.sqlite.JDBC");
    	}catch(ClassNotFoundException exception){
    		exception.printStackTrace();
    	}
        conn = DriverManager.getConnection("jdbc:sqlite:users.db");
    }

    private void closeConnection() {
        try {
            conn.close();
            conn = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
}
