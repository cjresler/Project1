import java.util.*;
import java.sql.*;

public class Application{

  private String m_userName;
  private String m_password;
  private String m_url = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
  private String m_driverName = "oracle.jdbc.driver.OracleDriver";

  public static void main(String args[]) {
    Application app = new Application();



    app.m_userName = args[0];
    app.m_password = args[1];

    Connection m_con;
    String createString;

    System.out.println(app.m_userName);
    System.out.println(app.m_driverName);

  }
}
