
package config;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class connection {
    
    private static connection instance;
    private static final String URL = "jdbc:mysql://localhost:3307/ghads";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    private Connection conn;
    
    private connection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//الربط مع الدرايفر
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static connection getInstance(){
        if(instance == null)
            instance = new connection();
        return instance;
    }
    
    public synchronized Connection getConnection() throws SQLException{ //يعني اذا ميثود مسكت الكونيكشن بتقدرش ميثود تانية تمسكه يعني ميثود وحدة بنفس الوقت
        if(conn == null || conn.isClosed()){
             conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }
    
    public synchronized void closeConnection() throws SQLException{
        if(conn != null)
           if(!conn.isClosed())
              conn.close();
    }
}
