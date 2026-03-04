/* Program to demonstrate the database connectin and the fetching of the data from the database     */


import java.sql.*;
public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
            String driver_load="org.mariadb.jdbc.Driver";
            try {
                Class.forName(driver_load);
                System.out.println("-----Driver Loaded Successfully -----");
            }
            catch(ClassNotFoundException e)
            {
                System.out.println("-----Driver Load Issue -----");
            }


            String url="jdbc:mariadb://localhost:3306/jdbc";
            String user="devuser";
            String password="devuser";
            String query="select * from student";


            try {
                Connection conn = DriverManager.getConnection(url,user,password);
                System.out.println("------Connection Successful -------");
                Statement stmt=conn.createStatement();
                ResultSet rs=stmt.executeQuery(query);
                while(rs.next()){
                    int id=rs.getInt("id");
                    String name=rs.getString("name");

                    System.out.print(id +"  ");
                    System.out.print(name);
                    System.out.println();
                }
            }
            catch(Exception e){
                System.out.println("------Something gets Wrong -----");
            }
    }
}