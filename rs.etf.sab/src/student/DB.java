/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

/**
 *
 * @author na170044d
 */
import java.sql.*;

public class DB {
       private static final String username="sa";
       private static final String password="123";
       private static final String database="TransportPaketa";
       private static final int port=1433;
       private static final String server="localhost";
       
       private static final String connectionUrl = "jdbc:sqlserver://"+server+":"+port+";databaseName="+database;
       
       private Connection connection;
       
       
       public Connection getConnection()
       {
           return db.connection;
       }
               
               
       private DB(){
           try{
               connection=DriverManager.getConnection(connectionUrl,username,password);
           }
           catch(SQLException ex)
           {
               
           }
       }
       
       private static DB db=null;
       
       public static DB getInstance(){
           if(db==null)
               db=new DB();
           return db;
       }
}
