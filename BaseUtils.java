package com.remotes.utils;



import java.io.*;
import java.math.*;
import java.net.*;
import java.nio.*;
import java.text.*;
import java.util.*;
import javax.net.ssl.*;

//import java.nio.file.Path;
public class BaseUtils {

  boolean debug;


  public BaseUtils() {
    debug = true;
  }


  public String get_remote_image_path(String brand, String smodel, boolean sub) {
    String extension = ".jpg";
    String brand_dir = formatbranddir(brand);

    String image_path = "";

    image_path += "";
    image_path += "/remote_images/small/";
    image_path += brand_dir + "/";
    image_path += getPageName(smodel).toLowerCase();

    if (sub)
      image_path += "_sub";

    image_path += extension;
    return image_path;
  }





  public boolean fileExists(String filepath) {

    java.io.File fp = new java.io.File(filepath);

    return fp.exists();
  }

  public String longToString(long value) {
    BigDecimal biguuid = new BigDecimal(value);

    return biguuid.toString();
  }

  public String intToString(int value) {
    BigDecimal biguuid = new BigDecimal(value);

    return biguuid.toString();
  }

  public String doubleToString(double value) {
    BigDecimal biguuid = new BigDecimal(value);

    return biguuid.toString();
  }

  public boolean directoryExists(String path) {
    java.io.File fp = new java.io.File(path);

    if (fp == null)
      return false;

    return fp.isDirectory();
  }

  public void mkdir(String path) {
    java.io.File fp = new java.io.File(path);

    if (fp == null)
      return;
    if (!fp.exists())
      fp.mkdirs();

  }

  public String removeLeadingTrailingSpaces(String str) {
    if (str == null)
      return "";

    int index = 0;
    //leading spaces
    int len = str.length();
    if (str == null || str.equals(""))
    	return str;
    
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

  /*  public void copyFile(File in, File out)  throws IOException {
       FileChannel sourceChannel = new
            FileInputStream(in).getChannel();
       FileChannel destinationChannel = new
            FileOutputStream(out).getChannel();
       sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
       // or
       //  destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
       sourceChannel.close();
       destinationChannel.close();
       }
   */

  public void copyFile(File in, File out) throws Exception {
    FileInputStream fis = new FileInputStream(in);
    FileOutputStream fos = new FileOutputStream(out);
    byte[] buf = new byte[1024];
    int i = 0;
    while ( (i = fis.read(buf)) != -1) {
      fos.write(buf, 0, i);
    }
    fis.close();
    fos.close();
  }

  public String formatbranddir(String brand) {
    brand = brand.replaceAll("&", "and");
    brand = formatbrand(brand);
    brand = brand.replace(' ', '_');
    brand = brand.replace(':', '_');
    brand = brand.replace('(', ' ');
    brand = brand.replace(')', ' ');
    brand = brand.replaceAll(" ", "");
    brand = brand.replaceAll("'", "");
    return brand.toLowerCase();

  }


  private void debug(String s) {
    System.out.println("[DEBUG] -- TestServlet -- \n" + s);
  }
/*
  public static class miTM
      implements javax.net.ssl.TrustManager,
      javax.net.ssl.X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
      return null;
    }

    public boolean isServerTrusted(
        java.security.cert.X509Certificate[] certs) {
      return true;
    }

    public boolean isClientTrusted(
        java.security.cert.X509Certificate[] certs) {
      return true;
    }

    public void checkServerTrusted(
        java.security.cert.X509Certificate[] certs, String authType) throws
        java.security.cert.CertificateException {
      return;
    }

    public void checkClientTrusted(
        java.security.cert.X509Certificate[] certs, String authType) throws
        java.security.cert.CertificateException {
      return;
    }
  }

  private static void trustAllHttpsCertificates() throws Exception {

    //  Create a trust manager that does not validate certificate chains:

    javax.net.ssl.TrustManager[] trustAllCerts =

        new javax.net.ssl.TrustManager[1];

    javax.net.ssl.TrustManager tm = new miTM();

    trustAllCerts[0] = tm;

    javax.net.ssl.SSLContext sc =

        javax.net.ssl.SSLContext.getInstance("SSL");

    sc.init(null, trustAllCerts, null);

    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(

        sc.getSocketFactory());

  }
*/
  public String formatbrand(String str) {
    if (str == null)
      return "";

    str = removeLeadingTrailingSpaces(str);
    str = str.toLowerCase();

    str = str.replaceAll("&", "&amp;");

//  int space = name.indexOf (' ');

    //String word = null;

    //Character character = null;
    int len = str.length();
    for (int i = 0; i < len; i = i + 1) {

      if (str.equals("jc penney")) {
        str = "JC Penney";
        break;
      }

      if (str.equals("us electronics")) {
        str = "US Electronics";
        break;
      }

      if (str.equals("3m") ||
          str.equals("ge") ||
          str.equals("gpx") ||
          str.equals("jbl") ||
          str.equals("jvc") ||
          str.equals("kec") ||
          str.equals("klh") ||
          str.equals("lxi") ||
          str.equals("kec") ||
          str.equals("klh") ||
          str.equals("lxi") ||
          str.equals("mtc") ||
          str.equals("mtx") ||
          str.equals("nec") ||
          str.equals("rca") ||
          str.equals("sts") ||
          str.equals("tdk") ||
          str.equals("xr1000")
          ) {
        str = str.toUpperCase();
        break;
      }

      if (i == 0) {
        //character = str.charAt(0);
        str = Character.toUpperCase(str.charAt(0)) +
            str.substring(1, str.length());
      }
      else if (i > 1 && str.charAt(i) == ' ' || str.charAt(i) == '/') {
        str = str.substring(0, i) + " " +
            Character.toUpperCase(str.charAt(i + 1)) +
            str.substring(i + 2, str.length());
      }
    }
    return str;
  }

  public String getLeaf(String FilePath) {
    boolean slash = false;
    String leaf = "";
    if (FilePath.indexOf('/') != -1 || FilePath.indexOf('\\') != -1) {

      for (int i = FilePath.length() - 1; i > 0; i--) {
        if (FilePath.charAt(i) == '/' || FilePath.charAt(i) == '\\') {
          slash = true;
          break;
        }
        leaf = FilePath.charAt(i) + leaf;
      }
    }

    if (slash)
      return leaf;
    else
      return FilePath;
  }

  public Vector reverseVectorOrder(Vector queryResults) {

    if (queryResults == null)
      return null;

    Vector retVal = new Vector();
    int x = 1;

    // set the size

    retVal.setSize(queryResults.size());

    // set the first item in the vector as the header
    retVal.setElementAt( (Vector) queryResults.elementAt(0), 0);

    // process the rest of the data elements in the vector
    for (int i = queryResults.size() - 1; i > 0; i--) {
      retVal.setElementAt( (Vector) queryResults.elementAt(i), x);
      x++;
    }

    return retVal;
  }

  public boolean getBooleanString(String boolstr) {
    if (boolstr != null && boolstr.equals("true"))
      return true;

    return false;
  }

  public String formatValue(String value) {
    // this function formats the value if NULL
    // &nbsp cause the HTML to display a empty cell in a table

    if (value == null || value.equals("") || value.equals("NA") ||
        value.equals("null"))
      return "&nbsp;";

    value = replaceInvalidChars(value);

    return value;
  }

  public String formatValue(boolean str) {
    // this function formats the value if NULL
    // &nbsp cause the HTML to display a empty cell in a table

    if (str)
      return "true";
    else
      return "false";

  }

  public String getPageName(String model_number) {
    if (model_number == null)
      return null;

    String pagename = "";

    pagename = model_number.replace(' ', '_');
    pagename = pagename.replace('"', '_');
    pagename = pagename.replace('/', '_');
    pagename = pagename.replace('\\', '_');
    pagename = pagename.replace('*', '_');
    pagename = pagename.replace('#', '_');
    pagename = pagename.replaceAll("\\+", "_plus_");
    pagename = pagename.replaceAll("\\(", "");
    pagename = pagename.replaceAll("\\)", "");
    pagename = pagename.replace('(', '_');
    pagename = pagename.replace(')', '_');

    return pagename.toLowerCase();
  }

  public String replaceFilePathChars(String model_number) {
    if (model_number == null)
      return null;

    String pagename = "";

    pagename = model_number.replace(' ', '_');
    pagename = pagename.replace('"', '_');
    //pagename = pagename.replace('/', '_');
    pagename = pagename.replace('\\', '_');
    pagename = pagename.replace('*', '_');
    pagename = pagename.replace('#', '_');
    pagename = pagename.replaceAll("\\+", "_plus_");
    pagename = pagename.replaceAll("\\(", "");
    pagename = pagename.replaceAll("\\)", "");
    pagename = pagename.replace('(', '_');
    pagename = pagename.replace(')', '_');

    return pagename.toLowerCase();
  }

  public String replaceInvalidChars(String str) {
    // Need to fix comment string. have to replace all " with ' for HTML output to work.
    // Need to also fix the comment string. Need to remove newline and carriage returns.

    // String oldstr = str;

    if (str != null) {
      str = str.replace('\'', '`');
      str = str.replace('"', '`');
      str = str.replace('\012', ' ');
      str = str.replace('\015', ' ');

      //	str = str.replace('&', ','); // this is for the database

      //str = str.replace('-','_'); // this char causes new line in HTML
    }

    return str;
  }

  public int stringToInt(String value) {

    try {
      BigDecimal bigvalue = new BigDecimal(value);

      return bigvalue.intValue();
    }
    catch (NumberFormatException ee) {
      return -1;
    }
  }

  public BigDecimal stringToBigDecimal(String value) {
    BigDecimal bigvalue = new BigDecimal(value);

    return bigvalue;
  }

  public double stringToDouble(String value) {
    BigDecimal bigvalue = new BigDecimal(value);

    return bigvalue.doubleValue();
  }

  public boolean isStringNumber(String value) {
    try {
      BigDecimal bigvalue = new BigDecimal(value);

      return true;
    }
    catch (NumberFormatException ee) {
      return false;
    }
  }

  public long stringToLong(String value) {
    BigDecimal bigvalue = new BigDecimal(value);

    return bigvalue.longValue();
  }

  public BigDecimal stringToDecimal(String value) {
    BigDecimal bigvalue = new BigDecimal(value);

    return bigvalue;
  }

  /****************
   * stringToVector
   ****************/
  public Vector stringToVector(String str) {

    Vector retval = new Vector();
    String tempstr = "";
    int len = str.length();
    for (int i = 0; i < len; i = i + 1) {
      if ( (str.charAt(i) == ',') || (str.charAt(i) == '\n') ||
          (str.charAt(i) == '\015') || (str.charAt(i) == ';')) {
        // check the length of the string greater the 1 char
        if (tempstr.length() > 0) {
          retval.addElement(tempstr);
        }
        tempstr = "";
      }
      else {
        tempstr += str.charAt(i);
      }

    }
    // Make sure we get the last element
    if (tempstr.length() > 0) {
      retval.addElement(tempstr);
    }

    return retval;
  }

  /****************
   * stringToVector
   ****************/
  // uses the seperator character to seperator string to vector
  public Vector stringToVector(String str, char seperator) {

    Vector retval = new Vector();
    String tempstr = "";
    int len = str.length();
    for (int i = 0; i < len; i = i + 1) {
      if ( (str.charAt(i) == seperator)) {
        // check the length of the string greater the 1 char
        //  if (tempstr.length() > 0) {
        retval.addElement(tempstr);
        //  }
        tempstr = "";
      }
      else {
        tempstr += str.charAt(i);
      }

    }
    // Make sure we get the last element
    if (tempstr.length() > 0) {
      retval.addElement(tempstr);
    }

    return retval;
  }

  /*******************
   * stringToVectorTab
   *******************/
  public Vector stringToVectorTab(String str) {

    Vector retval = new Vector();
    String tempstr = "";
    int len = str.length();
    for (int i = 0; i < len; i = i + 1) {
      if ( (str.charAt(i) == '\t')) {
        // check the length of the string greater the 1 char
        // if (tempstr.length() > 0) {
        // replace NULL with a null string
        if (tempstr.equals("NULL"))
          tempstr = "";

        retval.addElement(tempstr);
        // }
        tempstr = "";
      }
      else {
        tempstr += str.charAt(i);
      }

    }
    // Make sure we get the last element
    if (tempstr.length() > 0) {
      retval.addElement(tempstr);
    }

    return retval;
  }

  public String getTodaysDate() {
    java.util.Date today = new java.util.Date();
    String todaysDate = DateFormat.getDateTimeInstance(DateFormat.SHORT,
        DateFormat.MEDIUM).format(today);
    return todaysDate;
  }

  public String getTodaysDateShort() {
    java.util.Date today = new java.util.Date();
    String todaysDate = DateFormat.getDateInstance(DateFormat.SHORT).format(
        today);

    todaysDate = todaysDate.replaceAll("/", "_");
    return todaysDate;
  }

  public String getTodaysMonth() {
    java.util.Date today = new java.util.Date();
    String todaysDate = DateFormat.getDateTimeInstance(DateFormat.SHORT,
        DateFormat.MEDIUM).format(today);

    int index = todaysDate.indexOf("/", 0);

    String month = todaysDate.substring(0, index);

    if (month.length() == 1)
      month = "0" + month;

    return month;
  }

  public String getTodaysYear() {
    java.util.Date today = new java.util.Date();
    String todaysDate = DateFormat.getDateTimeInstance(DateFormat.SHORT,
        DateFormat.MEDIUM).format(today);

    int index = todaysDate.indexOf("/", 0);

    index = todaysDate.indexOf("/", index + 2);

    int index_end = todaysDate.indexOf(" ", index);

    String year = "20" + todaysDate.substring(index + 1, index_end);

    return year;
  }

  public String getTodaysDay() {
    java.util.Date today = new java.util.Date();
    String todaysDate = DateFormat.getDateTimeInstance(DateFormat.SHORT,
        DateFormat.MEDIUM).format(today);

    int index = todaysDate.indexOf("/", 0);

    int index_end = todaysDate.indexOf("/", index + 2);

    String day = todaysDate.substring(index + 1, index_end);

    if (day.length() == 1)
      day = "0" + day;

    return day;
  }

  public String getTodaysDateTime() {
    java.util.Date today = new java.util.Date();
    String todaysDate = DateFormat.getDateTimeInstance(DateFormat.SHORT,
        DateFormat.MEDIUM).format(today);

    System.out.println("Time=" + todaysDate);


    return todaysDate;
  }



  public String getUniqueStringID() {
    java.util.Date today = new java.util.Date();
    // create a Pacific Standard Time time zone

    // create a GregorianCalendar
    Calendar calendarFrom = new GregorianCalendar();
    calendarFrom.setTime(today);

    long uuidlong = calendarFrom.getTime().getTime();
    return longToString(uuidlong);
  }

  public Vector CmdExec(String cmdline) {
    Vector retVal = new Vector();

    Process p = null;
    BufferedReader input = null;

    try {
      String line;
      //   int index = 0;

      p = Runtime.getRuntime().exec(cmdline, null, null);
      p.waitFor();

      input = new BufferedReader(new InputStreamReader(p.getInputStream()));

      while ( (line = input.readLine()) != null) {

        // if ((index = line.indexOf("!!")) != -1) {
        //line = line.substring(index, line.length());
        System.out.println(line);
        retVal.addElement(line);
        //}
      }
    }
    catch (Exception err) {
      //sendErrorReport(err);
      err.printStackTrace(System.out);

    }
    finally {
      if (input != null) {
        try {
          input.close();
        }
        catch (Exception err) {
          //sendErrorReport(err);
          err.printStackTrace(System.out);

        }
      }
      if (p != null)
        p.destroy();
    }
    return retVal;
  }

  public Vector readFileIntoVector(String filepath) {
    // check if the file exists
    if (!fileExists(filepath))
      return null;

    Vector retval = new Vector();
    BufferedReader theReader = null;
    String line = null;
    Vector dataVec = null;

    try {
      theReader = new BufferedReader(new FileReader(filepath));

      while ((line = theReader.readLine()) != null) {

        // check for a blank or commented line
        if (line.equals("") || line.charAt(0) == '#') {
          continue;
        }
        else {
        	
          retval.addElement(line);
        }
      }
      // close the file
      theReader.close();
      theReader = null;

    }
    catch (IOException e) {
      //endErrorReport(e);
      System.out.println("  <ERROR> reading file!");
    }
    return retval;
  }
  
  public Vector readFileIntoVectorTab(String filepath) {
	    // check if the file exists
	    if (!fileExists(filepath))
	      return null;

	    Vector retval = new Vector();
	    BufferedReader theReader = null;
	    String line = null;
	    Vector dataVec = null;

	    try {
	      theReader = new BufferedReader(new FileReader(filepath));

	      while ((line = theReader.readLine()) != null) {

	        
	        // check for a blank or commented line
	        if (line.equals("") || line.charAt(0) == '#') {
	          continue;
	        }
	        else {
	        	dataVec = stringToVectorTab(line);
	          retval.addElement(dataVec);
	        }
	      }
	      // close the file
	      theReader.close();
	      theReader = null;

	    }
	    catch (IOException e) {
	      //endErrorReport(e);
	      System.out.println("  <ERROR> reading file!");
	    }
	    return retval;
	  }

// removes everything except numbers from string
  public String get_just_numbers(String str) {
    String tmpstr = "";
    char c = ' ';

    if (str == null)
      return "";
    int len = str.length();
    for (int i = 0; i < len; i = i + 1) {
      c = str.charAt(i);

      if (c == '0' ||
          c == '1' ||
          c == '2' ||
          c == '3' ||
          c == '4' ||
          c == '5' ||
          c == '6' ||
          c == '7' ||
          c == '8' ||
          c == '9')
        tmpstr = tmpstr + c;

    }

    // for some reason spaces get through. Remove them
    tmpstr = tmpstr.replaceAll(" ", "");
    return tmpstr;

  }

}
