package pex3;


/**
 * Demonstration of the most basic JDBC/MySQL connection.
 * 
 * @author Randy Bower
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCMySQLDemo
{
  public static void main( String args[] )
  {
    Connection con;
    try
    {
      con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery( "select * from film_text" );
      
      while( rs.next() )
      {
        System.out.println( rs.getString( "title" ) );
      }
      
      rs.close();
      stmt.close();
      con.close();
    }
    catch( SQLException e )
    {
      e.printStackTrace();
    }
  }
}
