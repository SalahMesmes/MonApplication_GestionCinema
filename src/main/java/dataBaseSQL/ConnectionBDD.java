package dataBaseSQL;

import java.sql.Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBDD {

    public static Connection ConnectionBDD(){

        Connection conn = null;

        try {
            //conn = DriverManager.getConnection("jdbc:mysql://51.178.86.117:8087/dario_3", "dario", "dab3oeP-");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/monapplication", "root", "");
        } catch (SQLException ex) {
            //Handle any errors
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
        }

        return conn;

    }

}

