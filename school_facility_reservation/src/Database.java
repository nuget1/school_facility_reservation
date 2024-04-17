import java.sql.*;

public class Database {
    public static Connection connect = null;

    public static Connection getConnection(){
        try{
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservations", "root", "");
        } catch (SQLException e) {
            System.out.println("Database Connection Failed.");
            System.out.println();
        }
        return connect;
    }

}
