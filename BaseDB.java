package com.remotes.database;



import java.io.*;
import java.sql.*;
import java.util.*;


import javax.sql.DataSource;
import javax.naming.InitialContext;

//
// This class is the base class for all datastores.
//
public class BaseDB {
  protected String __datasource;
 // protected RemoteBusyFlag __syncLock;

  protected static final String __db_base_path = "c:/website/database";
  protected boolean __debug = false;
  protected String __username;
  protected String __password;


  // ***************************
  // Constuct the database class
  // ***************************
  protected BaseDB(String datasource, String username, String password) {
    __datasource = null;
   // __syncLock = null;
    __debug = false;
    __username = null;
    __password = null;
    __datasource = datasource;
    __username = username;
    __password = password;
   // __syncLock = new RemoteBusyFlag();
  }

 

  public Connection open(boolean readonly, boolean direct, String host) {
    return open(readonly, direct, host, __username, __password);
  }

  public Connection open(boolean readonly, boolean direct, String host, String username, String password) {

    Connection con = null;
    Connection connection = null;
    if (true) {
      //System.out.println("Connecting directly to database");
      try {
        String driverName = "oracle.jdbc.driver.OracleDriver";
        Class.forName(driverName);
       
        String portNumber = "1521";
        String sid = "XE";
        String url = "jdbc:oracle:thin:@" + host + ":" + portNumber + ":" +
            sid;
        connection = DriverManager.getConnection(url, username, password);
        return connection;
      }
      catch (ClassNotFoundException e) {}
      catch (SQLException e) {
        System.out.println("Error connecting to database");
        e.printStackTrace(System.out);
      }
    }
    else {
    	try {
      InitialContext ctx = new InitialContext();

     
      System.out.println(__datasource);
      DataSource ds = (DataSource) ctx.lookup(__datasource);
      con = ds.getConnection();
      if (readonly) {
        con.setReadOnly(true);
      }
      else {
        con.setReadOnly(false);
        con.setAutoCommit(false);
      }
    }
    catch (SQLException e) {
      e.printStackTrace(System.out);
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
    }
    return con;
  }

  public void close(Connection con) {
    try {

      if (con != null)
        con.close();
    }
    catch (SQLException e) {
      e.printStackTrace(System.out);
    }
  }

  public int update(String update, String host) {
    Connection con = open(false, false, host);

    if (con == null)
      return -1;

    Statement stmt = null;

    try {
      //     locksync();

      if (__debug)
        System.out.println(update);

      con.setReadOnly(false);
      stmt = con.createStatement();

      int retval = stmt.executeUpdate(update);
      stmt.close();

      return retval;
    }

    catch (SQLException e) {
      System.out.println(update);
      System.out.println(e);
      e.printStackTrace(System.out);
    }
    finally {
//      unlocksync();
      if (stmt != null) {
        try {
          stmt.close();
          close(con);
          return 0;
        }
        catch (SQLException ee) {
          System.out.println(update);
          System.out.println("Database Error: SQLException.");
          return 0;
        }
      }
    }
    return 0;
  }

  /***************
   * executeUpdate
   ***************/
  // This function sends each line to the database in the vector
  // Unless it dies trying. First exception will return and not
  // execute and more lines.
  protected int update(Vector update, String host) {
    Connection con = open(false, false, host);
    if (__debug)
      System.out.println(update);
    if (con == null)
      return -1;

    Statement stmt = null;
    String line = null;

    int retVal = 0;

    try { // OK ExcuteUpdate needs to close statement even if exception is called
      // locksync();
      stmt = con.createStatement();

      if (update != null) {
        int len = update.size();
        for (int i = 0; i < len; i++) {

          line = (String) update.elementAt(i);

          retVal = stmt.executeUpdate(line);
        }
        return retVal;
      }
    }
    catch (SQLException e) {
      System.out.println(e);
    }
    finally {
      //unlocksync();
      if (stmt != null) {
        try {
          stmt.close();
          close(con);
          return 0;
        }
        catch (SQLException ee) {
          System.out.println(update);
          System.out.println("Database Error: SQLException.");
          return 0;
        }
      }
    }
    return 0;
  }

  public Vector query(String query, String host) {
    return query(query, true, host);
  }

  // This update function takes a connection. The client
  // needs to close the connection when they are done
  // with it.
  //
  public int update(Connection con, String update) {
    if (con == null)
      return -1;
    //  if (__debug)
    //System.out.println(update);
    Statement stmt = null;

    int retVal = 0;

    try { // OK ExcuteUpdate needs to close statement even if exception is called
      //  locksync();
      stmt = con.createStatement();

      int retval = stmt.executeUpdate(update);

      stmt.close();
      return retval;
    }

    catch (SQLException e) {
      System.out.println(e);
    }
    finally {
      //  unlocksync();
      if (stmt != null) {
        try {
          stmt.close();
          return 0;
        }
        catch (SQLException ee) {
          System.out.println(update);
          System.out.println("Database Error: SQLException.");

          return 0;
        }
      }
    }
    return 0;
  }

  protected  Vector query(String query, boolean display, String host)
  {
    return query(query,display, host, __username,__password);
  }




  // Query function used to query the database
  public  Vector query(String query, boolean display, String host, String username, String password) {
    Connection con = null;
    Vector retval = null;

    try {
      con = open(true, false, host, username, password);
      retval = query(con, query, display);
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
    finally {
      try {
        close(con);
      }
      catch (Exception ee) {
        System.out.println(query);
        ee.printStackTrace(System.out);
      }
    }
    return retval;
  }

  // Query function used to query the database
  // Since you pass in a connection, you need to close it.
  public Vector query(Connection con, String query, boolean display) {

    if (con == null)
      return null;

    if (__debug)
      System.out.println(query);

    Vector retVal = new Vector();
    String ColumnName = "";
    String value = "";
    Vector tmpVec = null;

    ResultSet rs = null;
    Statement stmt = null;

    Vector columnVec = null;


    //  System.out.println(query);
    try {

      // Setup for the query
      stmt = con.createStatement();
      rs = stmt.executeQuery(query);

      // Create a place to store the column headers
      columnVec = new Vector();

      // Get the column headers
      int len = rs.getMetaData().getColumnCount();
      for (int i = 1; i <= len; i++) {
        ColumnName = rs.getMetaData().getColumnName(i);
        columnVec.addElement(ColumnName);
      }

      // Add the column headers to location 0
      retVal.addElement(columnVec);

      // Step through the rest of the result set and add rows to the return value.
      // if the value is null, just put an empty string into the element
      while (rs.next()) {
        tmpVec = new Vector();
        int len2 = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= len; i++) {
          value = rs.getString(i);

          if (value != null)
            tmpVec.addElement(removeLeadingTrailingSpaces(value));
          else
            tmpVec.addElement("");
        }

        // add the row to the return value
        retVal.addElement(tmpVec);
      }
    }
    catch (SQLException e) {
      // helpful in debugging what exactly crashed
      System.out.println("query = " + query);
      if (true) {
        e.printStackTrace(System.out);
        e.setNextException(new SQLException("Faulty statement was: " + query));
      }
    }

    // Make sure we close everything
    finally {
      try {
        if (rs != null)
          rs.close();
        if (stmt != null)
          stmt.close();

        // close(con);
      }
      catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    }

    if (__debug)
      System.out.println(retVal);

    // System.out.println(retVal);
    return retVal;
  }

  // Query function used to query the database
  public Vector find(String query, String message, int search_field, String host) {
    Connection con = null;
    Vector retval = null;

    try {
      con = open(true, false, host);
      retval = find(con, query, message, search_field, false);
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
    finally {
      try {
        close(con);
      }
      catch (Exception ee) {
        ee.printStackTrace(System.out);
      }
    }
    return retval;
  }

  protected Vector find(Connection con, String query, String str,
                        int search_field, boolean display) {
    Vector retval = null;
    if (con == null)
      return null;

    if (__debug)
      System.out.println(query);

    System.out.println(query);
    System.out.println(str);

    String ColumnName = "";
    String value = "";
    Vector tmpVec = null;

    ResultSet rs = null;
    Statement stmt = null;

    Vector columnVec = null;

    // System.out.println(query);
    try {

      // Setup for the query
      stmt = con.createStatement();
      rs = stmt.executeQuery(query);

      // Create a place to store the column headers
      columnVec = new Vector();

      // Get the column headers
      int len = rs.getMetaData().getColumnCount();
      for (int i = 1; i <= len; i++) {
        ColumnName = rs.getMetaData().getColumnName(i);
        columnVec.addElement(ColumnName);
      }

      // Add the column headers to location 0
      //  retVal.addElement(columnVec);

      // Step through the rest of the result set and add rows to the return value.
      // if the value is null, just put an empty string into the element
      tmpVec = new Vector(3);

      search_field++;

      while (rs.next()) {

        tmpVec.clear();
        len = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= len; i++) {

          value = rs.getString(i);

          if (value != null)
            tmpVec.addElement(removeLeadingTrailingSpaces(value));
          else
            tmpVec.addElement("");

          // check if the value that is in the database is in the string
          if (i == search_field && str.indexOf(value) != -1) {
            return tmpVec;
          }

        }

      }
    }
    catch (SQLException e) {
      // helpful in debugging what exactly crashed
      if (true) {
        e.printStackTrace(System.out);
        e.setNextException(new SQLException("Faulty statement was: " + query));
      }
    }

    // Make sure we close everything
    finally {
      try {
        if (rs != null)
          rs.close();
        if (stmt != null)
          stmt.close();

        // close(con);
      }
      catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    }

    return null;
  }


  protected boolean createSchemaByFile(String filename, String host) {
     return createSchemaByFile(filename, host, __username, __password);
  }

  protected boolean createSchemaByFile(String filename, String host, String username, String password) {
    //
    // create the product master database
    String contents = "";
    String filepath = __db_base_path + "/source/" + filename;
    BufferedReader contentsReader = null;

    Connection con = null;

    File fp = new File(filepath);

    if (!fp.exists()) {
      System.out.println("Error: SQL file does not exist - " + filepath);
      return false;
    }

    try {
      con = open(false, false, host, username, password);
      contentsReader = new BufferedReader(new FileReader(filepath));
      int index = 0;

      String line = null;

      while (true) {
        line = contentsReader.readLine();
        //  System.out.println(line);
        if (line == null) {
          break;
        }

        if ( (index = line.indexOf("//")) != -1) {
          // need to eat the comment line
          if (index == 0)
            continue;

          // not at beginning of line. Each just comment
          line = line.substring(0, index) + +'\015';

        }

        contents += line;

        if (contents.indexOf(";") != -1) {
          contents = contents.replace('\012', ' ');
          contents = contents.replace('\015', ' ');
          contents = contents.replace('\t', ' ');
          System.out.println("###");
          System.out.println(contents);
          System.out.println("###");
          update(con, contents);

          contents = "";
        }
      }
      con.commit();

    }
    catch (Exception e) {
      System.out.println("Error: Unable to create schema");
      e.printStackTrace(System.out);
      return false;
    }
    finally {
      try {
        contentsReader.close();
        con.close();
      }
      catch (Exception e) {
        System.out.println("Error: Unable to close file");
        e.printStackTrace(System.out);
      }
    }
    return true;
  }

  protected String removeLeadingTrailingSpaces(String str) {
    if (str == null)
      return "";

    if (str.equals(" "))
      return str;

    int index = 0;
    //leading spaces
    int len = str.length();
    for (index = 0; index < len; ) {
      if (str.charAt(index) != ' ')
        break;
      str = str.substring(index + 1, str.length());
    }

    // trailing spaces
    while (true) {
      if (str.length() < 2)
        break;
      index = str.length() - 1;
      if (str.charAt(index) != ' ')
        break;
      str = str.substring(0, index);
    }
    return str;
  }

}


