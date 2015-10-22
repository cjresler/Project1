import java.util.*;
import java.sql.*;

public class Application{

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
    String result = in.nextLine();
    if (result.equals("1")){
      System.out.print("Please enter your email: ");
      String email = in.next();
      System.out.print("Please enter your password: ");
      String pass = in.next();
      System.out.println(email + " " + pass);
    }
    else if(result.equals("2")){

    }
    else if(result.equals("q")){

    }

    Connection m_con;
    String createString;

  }


}
