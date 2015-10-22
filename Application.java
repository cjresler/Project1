import java.util.*;
import java.sql.*;

public class Application{

  public String client_email = "";
  public String client_password = "";

  private String m_userName;
  private String m_password;
  private String m_url = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
  private String m_driverName = "oracle.jdbc.driver.OracleDriver";

  public static void main(String args[]) {
    Application app = new Application();
    Scanner in = new Scanner(System.in);

    app.m_userName = args[0];
    app.m_password = args[1];

    System.out.println("Pick an option:");
    System.out.println("1 - Registered User");
    System.out.println("2 - Not a Registered User");
    System.out.println("q - Exit");
    String result = in.nextLine()

    if (result.equals("1")){

      while(!app.Login()){}
      System.out.println("Valid Credentials!");

    }
    else if(result.equals("2")){

    }
    else if(result.equals("q")){

    }

  }

  public Boolean Login(){
    Boolean valid = FALSE;

    System.out.print("Please enter your email: ");
    app.client_email = in.next();
    System.out.print("Please enter your password: ");
    app.password = in.next();

    Connection m_con;
    String findUsers;
    findUsers = "SELECT email, pass " +
    "FROM users";
    Statement stmt;

    try
    {
      Class drvClass = Class.forName(m_driverName);
      DriverManager.registerDriver((Driver)
      drvClass.newInstance());
    } catch(Exception e)
    {
      System.err.print("ClassNotFoundException: ");
      System.err.println(e.getMessage());
    }

    try
    {
      m_con = DriverManager.getConnection(m_url, m_userName, m_password);

      stmt = m_con.createStatement();
      ResultSet rst = stmt.executeUpdate(createString);
      while(rst.next()){
        if (rst.getString(1).equals(app.client_email) && rst.getString(2).equals(app.client_password)){
          valid = TRUE;
          break;
        }
      }
      rst.close();
      stmt.close();
      m_con.close();
    } catch(SQLException ex) {
      System.err.println("SQLException: " +
      ex.getMessage());
    }


      return valid;
  }


}
