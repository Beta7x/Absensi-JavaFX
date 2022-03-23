package Utils;

import java.sql.*;

public class Connections {
    Connections conn = null;
    public static Connection conDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/absensi", "root", "sudo./root");
            return con;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Connection : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
