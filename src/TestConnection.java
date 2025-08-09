import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection{
    public static void main(String[] args) {
        String url =  "jdbc:postgresql://localhost:5432/hospital_simulator";
        String user = "postgres";
        String password = "naoesquece";

        try{
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");
            con.close();
        } catch (SQLException e){
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}