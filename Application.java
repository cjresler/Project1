import java.util.*;
import java.sql.*;

public class Application{

  public String client_email = "";
  public String client_password = "";
  public boolean isAgent = false;


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

      while(app.Login(app) == false){}
      System.out.println("Valid Credentials!");
      app.Menu(app);
    }
    else if(result.equals("2")){
	    app.createUser(app);
    }
    else if(result.equals("q")){

    }

  }

  //Display main menu
  public void Menu(Application app)
  {
    Scanner in = new Scanner(System.in);
    System.out.println("\nWhat would you like to do? Choose an option.");
    System.out.println("1 - Search for flight");
    System.out.println("2 - View existing bookings");
    if (app.isAgent)
    {
      System.out.println("3 - Record Departure");
      System.out.println("4 - Record Arrival");
    }
    System.out.println("0 - Logout");

    int choice = in.nextInt();
    if (choice == 1)
    {
      //search for flight
	    app.searchFlights(app);
    }
    else if (choice == 2)
    {
      System.out.println();
      //View bookings
      app.viewBookings(app);
    }
    else if (choice == 3)
    {
      //log out
    }
  }


  public void searchFlights(Application app) {
    Scanner in = new Scanner(System.in);

    System.out.println("\nEnter source: ");
	String src = in.nextLine();
	//check for acode, city, or name
	src = app.findAcode(app, src);

	System.out.println("\nEnter destination: ");
	String dst = in.nextLine();
	//check for acode, city, or name
	dst = app.findAcode(app, dst);

	System.out.println("\nEnter departure date - day: ");
	int day = in.nextInt();
	while ((day <= 0) || (day > 31)) {
		System.out.println("Invalid day. Try again: ");
		day = in.nextInt();
	}

	System.out.println("\nEnter departure date - month (numerical): ");
	int month = in.nextInt();
	while ((month <= 0) || (month> 12)) {
		System.out.println("Invalid month. Try again: ");
		month = in.nextInt();
	}

	System.out.println("\nEnter departure date - year");
	int year = in.nextInt();

	System.out.println("\nSort criteria: 1 - price low to high, 2 - connection primary, price secondary: ");
	int sortoption = in.nextInt();
	while ((sortoption <= 0) || (sortoption > 2)) {
		System.out.println("Invalid option. 1 - price low to high, 2 - connection primary, price secondary: ");
		sortoption = in.nextInt();
	}
  }


  public void viewBookings(Application app)
  {
    Scanner in = new Scanner(System.in);
    String findBookings;
    //Find booking information related to client email
    findBookings = "select b.tno, to_char(dep_date, 'DD-Mon-YYYY') as dep_date, paid_price, name " +
                  "from bookings b, tickets t " +
                  "where b.tno = t.tno " +
                  "and t.email = '" + app.client_email +"'";
    Statement stmt;
    Connection con;
    Statement stmt2;
    try
    {
      con = DriverManager.getConnection(app.m_url, app.m_userName, app.m_password);
      stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stmt2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

      ResultSet rs = stmt.executeQuery(findBookings);

      displayResultSet(rs);

      System.out.println("Choose an option: ");
      System.out.println("Enter ticket number - view more details about booking");
      System.out.println("2 - Cancel a booking");
      System.out.println("0 - Return to main menu");
      System.out.print("Choice: ");
      int input = in.nextInt();
      if(input == 2)
      {
        System.out.print("Enter ticket number of booking you would like to cancel: ");
        input = in.nextInt();
      }
      else if(input == 0)
      {
        app.Menu(app);
      }
      else
      {
        String moreInfo = "select distinct b.tno, to_char(dep_date, 'DD-Mon-YYYY') as dep_date, paid_price, name, b.fare, bag_allow " +
                  "from bookings b, tickets t, flight_fares f " +
                  "where b.tno = t.tno " +
                  "and b.fare = f.fare " +
                  "and t.email = '" + app.client_email +"'" +
                  "and b.tno = '" + input + "'";
        ResultSet rs2 = stmt2.executeQuery(moreInfo);
        displayResultSet(rs2);
      }


      stmt.close();
      con.close();
    } catch(SQLException ex)
    {
      System.err.println("SQLException: " + ex.getMessage());
    }
  }

  public String findAcode(Application app, String input) {
    Scanner in = new Scanner(System.in);
    Boolean found = false;
    Connection m_con;
    String findAcode;
    findAcode = "SELECT acode, city, name FROM airports";
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
      m_con = DriverManager.getConnection(app.m_url, app.m_userName, app.m_password);

      stmt = m_con.createStatement();
      ResultSet rst = stmt.executeQuery(findAcode);
      while(rst.next()){
        if (input.equalsIgnoreCase(rst.getString(1).trim())){
          found = true;
          System.out.print("Valid acode!");
          System.out.println(rst.getString(1).trim() + " - " +  rst.getString(2).trim() + " " +  rst.getString(3).trim());
          return rst.getString(1);
        }
		else if (rst.getString(2).toLowerCase().contains(input.toLowerCase()) || rst.getString(3).toLowerCase().contains(input.toLowerCase())){
		  System.out.println(rst.getString(1).trim() + " " +  rst.getString(2).trim() + " " +  rst.getString(3).trim());
		}
      }
      rst.close();
      stmt.close();
      m_con.close();
    } catch(SQLException ex) {
      System.err.println("SQLException: " +
      ex.getMessage());
    }

	System.out.print("Enter the acode of the airport you want: ");
	String acode = in.next();
	acode = app.findAcode(app, acode);

    return acode;
  }

	//function to create a new user
  public void createUser(Application app)
  {
    //Get user information
    Scanner in = new Scanner(System.in);
    System.out.print("\nPlease enter an email address: ");
    app.client_email = in.next();
    System.out.print("Enter a password: ");
    app.client_password = in.next();

    if (app.client_password.length() > 4) {
      System.out.println("Password is too long; maximum 4 char");
      app.createUser(app);
    }

    Connection m_con;
    String updateTable;
    //Add user email and password to table Users. Not sure what to initialize the date to
    updateTable = "insert into users values" + "('" + app.client_email + "', '" + app.client_password + "', SYSDATE)";

    Statement stmt;

    try
    {
      m_con = DriverManager.getConnection(app.m_url, app.m_userName, app.m_password);

      stmt = m_con.createStatement();
      stmt.executeUpdate(updateTable);
      stmt.close();
      m_con.close();
      System.out.println("Successfully created account");
      app.Menu(app);
    } catch(SQLException ex) {
      System.err.println("SQLException: " +
      ex.getMessage());
      System.out.println("An error has occurred. Please try again.");
      app.createUser(app);
    }
  }

  public boolean Login(Application app){
    Scanner in = new Scanner(System.in);
    boolean valid = false;

    System.out.print("\nPlease enter your email: ");
    app.client_email = in.next();
    System.out.print("Please enter your password: ");
    app.client_password = in.next();

    Connection m_con;
    String findUsers;
    findUsers = "SELECT email, pass FROM users";
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
      m_con = DriverManager.getConnection(app.m_url, app.m_userName, app.m_password);

      stmt = m_con.createStatement();
      ResultSet rst = stmt.executeQuery(findUsers);
      while(rst.next()){
        if (app.client_email.equals(rst.getString(1).trim()) && app.client_password.equals(rst.getString(2).trim())){
          valid = true;
          System.out.print("Found valid.");
		  System.out.println(rst.getString(1) + " " + rst.getString(2));
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

  //Function for displaying a result set, with column names
  public void displayResultSet(ResultSet rs)
  {
    String value = null;
    Object o = null;

    try
    {
      ResultSetMetaData rsM = rs.getMetaData();

      int columnCount = rsM.getColumnCount();

      for (int column = 1; column <= columnCount; column++)
      {
        value = rsM.getColumnLabel(column);
        System.out.print(value + "\t");
      }
      System.out.println();

      while(rs.next())
      {
        for (int i = 1; i <= columnCount; i++)
        {
          o = rs.getObject(i);
          if (o != null)
          {
            value = o.toString();
          }
          else
          {
            value = "null";
          }
          System.out.print(value + "\t");
        }
      System.out.println();
      }
    } catch(Exception io)
    {
      System.out.println(io.getMessage());
    }
  }


}
