package database;
import java.sql.*;
import models.Customer;

/*
* import the package   ==> java.sql
* load and register the driver  ==> com.mysql.jdbc.Driver
* create CONNECTION ==> Driver.getConnection
* create a statement  ==>
* execute a query
* process the results
* close statement and connection
* */

public class Database {

    private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String URL = "jdbc:mysql://localhost:3306/test";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "mysql";
    private static Connection con = null;

    //singleton
    private static Database db = null;

    private Database() throws SQLException, ClassNotFoundException {
        setConnection();
    }

    public static Database getInstance() throws SQLException, ClassNotFoundException {
        if(db == null){
            db = new Database();
        }
        return db;
    }

    public Connection getConnection(){
        return con;
    }

    private void setConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        this.con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
