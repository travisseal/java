package com.remotes.database;



import com.remotes.utils.*;
import java.sql.*;
import java.util.*;



/*
 * Defining class
 */
public class MasterDB
    extends com.remotes.database.BaseDB  {

  //static final String __db_source_script = "master.sql";
  //static final String __datasource = "remote_master";
  
  static final boolean __direct_connection = true;


  // Variable used to make this a single instance
  protected static MasterDB __instance = null;

  private BaseUtils d_utils = new BaseUtils();

  private static boolean __build_master_always = false;


  // ***************************
  // Constuct the database class
  // ***************************
  protected MasterDB() {


    // call the base class init function
    super("remote_master", "remote_master", "altigen789");


    System.out.println("Creating remote_master database");


    // Create a lock to be used in the database
   // __syncLock = new RemoteBusyFlag();

/*
    // check if we have any data.
    Vector queryResults = query("select id from tbl_prod_brand");

    System.out.println(queryResults);
      // fill with data from Alpha if we have no data
      Vector dataVec = null;

      // check if the database is empty and the schema is not defined (no tables)
      if (queryResults.size() < 1) {
        createSchemaByFile(__db_source_script);
        queryResults = query("select id from tbl_prod_brand");
      }
      // check if we have the tables defined by no data


      if (queryResults.size() > 1) {
        dataVec = (Vector)queryResults.elementAt(1);
        if (dataVec.size() < 2)
          importAlphaDatabase();
      }

if (__build_master_always) {
  Connection con = null;
  AlphaDB alphaDb = null;

  try {
    // Open connection for write access
    con = open(false,__direct_connection);

    // open Alpha database
    alphaDb = new AlphaDB();

    import_products(con, alphaDb);

    // write all updates to the database
    con.commit();
  }
  catch (Exception e) {
    e.printStackTrace(System.out);
  }
  finally {
    // always close the connection
    close(con);
    alphaDb.close();
  }
  */


  }

  // This function is used by other classes to get the RemoteDb
  // object. This makes the system so there is only one instance.
  // if the RemoteDatabase is not constucted, construct it and return
  // the object. If we are constructed, just return the object.
   public static synchronized MasterDB getInstance() {

     if (__instance == null) {
       __instance = new MasterDB();
     }

     return __instance;
   }



   public void importAlphaDatabase(String host){

    BaseUtils utils = new BaseUtils();
    Connection tmp_con = null;
    Vector alphaResults = null;
    String filter = "";
    String alphaCol = null;
    String query = null;
    Vector dataVec = null;


    // check if there is already data.
    Vector queryResults = query("select DISTINCT data from tbl_prod_brand", false, host);

    // We have data, do nothing
    if (queryResults != null && queryResults.size() > 1)
      return;


    System.out.println("Importing Alpha database into product master");

    // lock the database from access until we update it
   // locksync();

    // Open connection for write access
    Connection con = open(false,__direct_connection, host);

    // create the database schema
 //   createSchemaByFile(con, __db_source_script);


    // open Alpha database
  // AlphaDB alphaDb = new AlphaDB();




/*
    // **********************************
    // Fill in single data column fields
    // **********************************
    fillDataNameOneColumn(con, alphaDb, "inv_mast", "brand", "tbl_prod_brand", true);
    fillDataNameOneColumn(con, alphaDb, "inv_mast", "accessory", "tbl_prod_category", true);
    fillDataNameOneColumn(con, alphaDb, "inv_mast", "productype", "tbl_prod_type", true);
    fillDataNameOneColumn(con, alphaDb, "inv_mast", "sub_cat", "tbl_prod_type_sub", true);
    fillDataNameOneColumn(con, alphaDb, "inv_mast", "extra8", "tbl_prod_webnote", false);
    fillDataNameOneColumn(con, alphaDb, "inv_mast", "nla", "tbl_status_product", true);


    // needed is hard coded
   fillDataNameOneColumn(con, alphaDb, "inv_mast", "extra4", "tbl_status_needed", true);


    fillDataNameOneColumn(con, alphaDb, "inv_mast", "extra9", "tbl_status_burnable", true);

    fillDataNameOneColumn(con, alphaDb, "inv_mast", "captured", "tbl_status_captured", true);
    fillDataNameOneColumn(con, alphaDb, "inv_mast", "cap_sub", "tbl_status_captured", true);

    fillDataNameOneColumn(con, alphaDb, "inv_mast", "pic_org", "tbl_status_scanned", true);
    fillDataNameOneColumn(con, alphaDb, "inv_mast", "pic_sub", "tbl_status_scanned", true);


   // fillDataNameOneColumn(con, alphaDb, "inv_mast", "purch_note", "tbl_prod_purchase_note", false);
    fillDataNameOneColumn(con, alphaDb, "inv_mast", "supplier", "tbl_prod_suppliers", false);


    update(con,"insert into tbl_status_product (data, description, active) values ('OK', 'Indicates that the item has no special status and can be sold', 1)");

  // Fill in the cody database.
  //importCodyDatabase();

// import products
  import_products(con, alphaDb);


*/

    try {
      // write all updates to the database
      con.commit();
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
    finally {
      // always close the connection
      close(con);
     // alphaDb.close();
    }

    // unlock the database so everyone can see changes
   // unlocksync();
  }


  public int getTableColumnDataId(Connection con, String table, String data)
  {
    int retval = -1;
    String query = null;
    Vector dataVec = null;
    Vector queryResults = null;
    String value = null;

    query = "SELECT id from " + table + " ";
    query += "WHERE data='" + data + "'";



    queryResults = query(con,query,false);
  //  System.out.println(query);


  if (data == null || data.equals(""))
    return -1;

    if (queryResults.size() < 2) {
      String update = "insert into " + table + " (data,active) values ";
      update += "('" + data + "', 1)";
      update(con,update);
    }

    queryResults = query(con,query,false);
    if (queryResults.size() < 2) {
      return -1;
    }

    dataVec = (Vector)queryResults.elementAt(1);
    value = (String)dataVec.elementAt(0);
    retval = d_utils.stringToInt(value);

   // System.out.println(data + " -> " + retval);

    return retval;
  }
/*
  public void import_products(Connection con, AlphaDB alphaDb){

    String query = null;
 Vector alphaResults = null;
 Vector dataVec = null;
 String value = null;

 String alphaTable = "inv_mast";
 String storeTable = "tbl_product_master";



 int brand_id = -1;
 int category_id = -1;
 int type_id = -1;
 int type_sub_id = -1;
 int status_id = -1;

 BigDecimal cost_rms_decimal = null;
 BigDecimal cost_cust_decimal = null;

 int qty_onhand_int = 0;

 String brand = null;
 String mod_rmt_pt = null;
 String model = null;
 String remote_num = null;
 String part_numbr = null;
 String sub_rmt_n = null;
 String sub_part_n = null;
 String accessory = null;
 String rms_cost = null;
 String cust_cost = null;
 String notes = null;
 String manf_partn = null;
 String qty_onhand = null;
 String productype = null;
 String sub_cat = null;
 String nla = null;
 int captured_id = -1;
 int captured_sub_id = -1;
 int scanned_id = -1;
 int scanned_sub_id = -1;
 int needed_id =-1;
 int burnable_id = -1;
 String rec_note = null;
 String purch_note = null;
 String web_note = null;
 String alt_remote = null;



 int needed_complete_id = -1;
 int needed_unknown_id = -1;

 boolean captured_bool = false;
 boolean scanned_bool = false;

 // this is only in the new database. Need to just get the blank value and
 // set it to this.
 int needed_sub_id = getTableColumnDataId(con, "tbl_status_needed", " ");
 int burnable_sub_id = getTableColumnDataId(con, "tbl_status_burnable", " ");


 String alphaColumn = "";
 alphaColumn += "brand, accessory, productype, sub_cat, ";
 alphaColumn += "mod_rmt_pt, model, remote_num, part_numbr, sub_rmt_n, sub_part_n, ";
 alphaColumn += "rms_cost, cust_cost, notes, manf_partn, nla, ";
 alphaColumn += "captured, cap_sub, pic_org, pic_sub, ";
 alphaColumn += " rec_note, purch_note, extra8, extra9, extra4, extra3, notes ";

 String newColumns = "";
 newColumns += "brand_id, status_id, category_id, type_id, type_sub_id, ";
 newColumns +="mod_rmt_pt , model, remote, part, sub_part, sub_remote, manufacture_pn, ";
 newColumns += "cost_rms, cost_cust, ";
 newColumns += "status_captured_id, status_captured_sub_id, status_scanned_id, status_scanned_sub_id, ";
 newColumns += "status_needed_id, status_needed_sub_id, status_burnable_id, status_burnable_sub_id, ";
 newColumns += "rec_note, purch_note, web_note, notes, alt_remote, nla ";




 //update(con, "delete from tbl_product_master");

 PreparedStatement pstmt = null;
 Vector update_commands = new Vector();

 // get the data from Alpha
 query = "select DISTINCT ";
 query += alphaColumn;
 query += " from " + alphaTable;
 query += " where brand is not null ";
 alphaResults = alphaDb.query(query, "jdbc:odbc:inv_mast");

 System.out.println("Storing " + alphaColumn + "->" + storeTable +
                    " table: " + alphaResults.size());

 //    String product_id = null;
 PrintWriter out = null;
 //String brand = null;
 try {

   // d_utils.mkdir(filepath);

   // out = new PrintWriter(new BufferedWriter(new FileWriter(
   //     filepath, false)));
   String tmpfile = "c:/temp/" + storeTable + ".xls";
   out = new PrintWriter(new BufferedWriter(new FileWriter(
       tmpfile, false)));

   // update(con, "delete from " + storeTable);
   // con.commit();

   pstmt = con.prepareStatement("insert into " + storeTable +
                                " (" + newColumns + ")" +
                                " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

   int len = alphaResults.size();

   String columnName = null;
   Vector columns = (Vector)alphaResults.elementAt(0);
   System.out.println(columns);
   for (int i = 1; i < len; i++) {

     if (i % 1000 == 1)
          System.out.println(i - 1);

    //    if (i==20000)
     //     break;


     // get the row
     dataVec = (Vector) alphaResults.elementAt(i);
   //  System.out.println(dataVec);


     for (int x=0;x<dataVec.size();x++) {

       columnName = (String)columns.elementAt(x);
       value = (String) dataVec.elementAt(x);
       value = removeLeadingTrailingSpaces(value);

       // need to remove any invalid characters in value
       if (value != null) {
         value = value.replaceAll(",","");
         value = value.replaceAll("'","");
       }

//       System.out.println(columnName + " -> '" + value + "'");

       // assign new database values
       if (columnName.equals("brand")) {
         if (value == null || value.equals(""))
           break;

         brand_id = getTableColumnDataId(con, "tbl_prod_brand", value);

       }
       else if (columnName.equals("mod_rmt_pt")) {
         mod_rmt_pt = value;
       }
       else if (columnName.equals("model")) {
         model = value;
       }
       else if (columnName.equals("remote_num")) {
         remote_num = value;
       }
       else if (columnName.equals("part_numbr")) {
         part_numbr = value;
       }
       else if (columnName.equals("sub_rmt_n")) {
         sub_rmt_n = value;
       }
       else if (columnName.equals("sub_part_n")) {
         sub_part_n = value;
       }
       else if (columnName.equals("accessory")) {

         if (value != null || value.equals(""))
           category_id = getTableColumnDataId(con, "tbl_prod_category", value);
       }

       else if (columnName.equals("rms_cost")) {
         cost_rms_decimal = d_utils.stringToDecimal(value);
         rms_cost = value;
       }
       else if (columnName.equals("cust_cost")) {
         cost_cust_decimal = d_utils.stringToDecimal(value);
       }
       else if (columnName.equals("notes")) {
         notes = value;
       }

       else if (columnName.equals("manf_partn")) {
         manf_partn = value;
       }
       else if (columnName.equals("qty_onhand")) {
         qty_onhand = value;
       }

       else if (columnName.equals("productype")) {
         if (value != null || value.equals(""))
           type_id = getTableColumnDataId(con, "tbl_prod_type", value);
       }
       else if (columnName.equals("sub_cat")) {
         if (value != null || value.equals(""))
            type_sub_id = getTableColumnDataId(con, "tbl_prod_type_sub", value);
       }
       else if (columnName.equals("nla")) {
         if (value == null || value.equals(""))
           value = " ";

       //  if (value == null || value.equals(""))
       //    value = "ok";
         else if (value != null && value.equals("Y"))
           value = "nla";

          status_id = getTableColumnDataId(con, "tbl_status_product", value.toUpperCase());
       }
       else if (columnName.equals("captured")) {
         if (value == null || value.equals(""))
           value = " ";

            captured_id = getTableColumnDataId(con, "tbl_status_captured", value.toUpperCase());
         //   System.out.println("Captured = '" + value + "'  id=" + captured_id);

       }
       else if (columnName.equals("cap_sub")) {
         if (value == null || value.equals(""))
           value = " ";


         captured_sub_id = getTableColumnDataId(con, "tbl_status_captured", value.toUpperCase());
      //   System.out.println("Captured sub = '" + value + "'  id=" + captured_sub_id);

        }
        else if (columnName.equals("pic_org")) {
          if (value == null || value.equals(""))
            value = " ";


           scanned_id = getTableColumnDataId(con, "tbl_status_scanned", value.toUpperCase());

       }

       else if (columnName.equals("pic_sub")) {
         if (value == null || value.equals(""))
           value = " ";


         scanned_sub_id = getTableColumnDataId(con, "tbl_status_scanned", value.toUpperCase());
       }
       else if (columnName.equals("extra9")) {
         if (value == null || value.equals(""))
           value = " ";


         burnable_id = getTableColumnDataId(con, "tbl_status_burnable", value.toUpperCase());
       }
       else if (columnName.equals("extra4")) {
         if (value == null || value.equals(""))
           value = " ";


        needed_id = getTableColumnDataId(con, "tbl_status_needed", value.toUpperCase());
      }
       else if (columnName.equals("rec_note")) {
         rec_note = value;
       }
       else if (columnName.equals("purch_note")) {
        purch_note = value;
      }
      else if (columnName.equals("extra8")) {
        web_note = value;
      }
      else if (columnName.equals("notes")) {
        notes = value;
      }
      else if (columnName.equals("extra3")) {
       alt_remote = value;
     }
   }

     // Always have to have a brand and a category
     if (brand_id == -1 ||
         category_id == -1) {
       continue;
     }


     int field = 1;
     pstmt.setInt(field++, brand_id);

     if (status_id == -1)
       pstmt.setNull(field++, Types.INTEGER);
     else
       pstmt.setInt(field++, status_id);

     pstmt.setInt(field++, category_id);

     if (type_id == -1)
       pstmt.setNull(field++, Types.INTEGER);
     else
       pstmt.setInt(field++, type_id);

     if (type_sub_id == -1)
       pstmt.setNull(field++, Types.INTEGER);
     else
       pstmt.setInt(field++, type_sub_id);

     pstmt.setString(field++, mod_rmt_pt);
     pstmt.setString(field++, model);
     pstmt.setString(field++, remote_num);
     pstmt.setString(field++, part_numbr);
     pstmt.setString(field++, sub_part_n);
     pstmt.setString(field++, sub_rmt_n);
     pstmt.setString(field++, manf_partn);

     pstmt.setBigDecimal(field++, cost_rms_decimal);
     pstmt.setBigDecimal(field++, cost_cust_decimal);

     if (captured_id == -1)
       pstmt.setNull(field++, Types.INTEGER);
     else
       pstmt.setInt(field++, captured_id);

     if (captured_sub_id == -1)
       pstmt.setNull(field++, Types.INTEGER);
     else
       pstmt.setInt(field++, captured_sub_id);

     if (scanned_id == -1)
       pstmt.setNull(field++, Types.INTEGER);
     else
       pstmt.setInt(field++, scanned_id);

     if (scanned_sub_id == -1)
       pstmt.setNull(field++, Types.INTEGER);
     else
       pstmt.setInt(field++, scanned_sub_id);



     if (needed_id == -1)
       pstmt.setNull(field++, Types.INTEGER);
     else
       pstmt.setInt(field++, needed_id);

     if (needed_sub_id == -1)
    pstmt.setNull(field++, Types.INTEGER);
  else
    pstmt.setInt(field++, needed_sub_id);




     if (burnable_id == -1)
       pstmt.setNull(field++, Types.INTEGER);
     else
       pstmt.setInt(field++, burnable_id);

     if (burnable_sub_id == -1)
      pstmt.setNull(field++, Types.INTEGER);
    else
      pstmt.setInt(field++, burnable_sub_id);


     if (rec_note == null || rec_note.equals(""))
       pstmt.setNull(field++, Types.VARCHAR);
     else
       pstmt.setString(field++, rec_note);

     if (purch_note == null || purch_note.equals(""))
       pstmt.setNull(field++, Types.VARCHAR);
     else
       pstmt.setString(field++, purch_note);

     if (web_note == null || web_note.equals(""))
       pstmt.setNull(field++, Types.VARCHAR);
     else
       pstmt.setString(field++, web_note);


     if (notes == null || notes.equals(""))
       pstmt.setNull(field++, Types.VARCHAR);
     else
       pstmt.setString(field++, notes);

     if (alt_remote == null || alt_remote.equals(""))
       pstmt.setNull(field++, Types.VARCHAR);
     else
       pstmt.setString(field++, alt_remote);


     if (nla == null || nla.equals(""))
        pstmt.setNull(field++, Types.INTEGER);
     else
       pstmt.setInt(field++, 1);

   //  System.out.println("Count= " + field);

     pstmt.executeUpdate();
   }
   pstmt.close();
   con.commit();
   out.close();

 }
 catch (Exception e) {
   System.out.println("Error: Unable to add " + storeTable);
   System.out.println(dataVec);
   e.printStackTrace(System.out);
 }

}





  public void fillDataNameOneColumn(Connection con, AlphaDB alphaDb,
                                    String alphaTable, String alphaColumn,
                                    String storeTable, boolean uppercase) {
    String query = null;
    Vector alphaResults = null;
    Vector dataVec = null;
    String value = null;

    update(con, "delete from " + storeTable);

    PreparedStatement pstmt = null;
    Vector update_commands = new Vector();

    // get the data from Alpha
    query = "select DISTINCT ";
    if (uppercase)
      query += "upper( " + alphaColumn + ")";
    else
      query += alphaColumn;
    query += " from " + alphaTable;
    alphaResults = alphaDb.query(query, "jdbc:odbc:inv_mast");

    System.out.println("Storing " + alphaColumn + "->" + storeTable +
                       " table: " + alphaResults.size());

    //    String product_id = null;
    PrintWriter out = null;

    //System.out.println(alphaResults);
    //String brand = null;
    try {

      // d_utils.mkdir(filepath);

      // out = new PrintWriter(new BufferedWriter(new FileWriter(
      //     filepath, false)));
  //    String tmpfile = "c:/temp/" + storeTable + ".xls";
  //    out = new PrintWriter(new BufferedWriter(new FileWriter(
   //       tmpfile, false)));

      // update(con, "delete from " + storeTable);
      // con.commit();

 /*

      // hard code needed
      if (alphaColumn.equals("extra4")) {
        alphaResults = new Vector();

        dataVec = new Vector();
        dataVec.addElement("Header");
        alphaResults.addElement(dataVec);

        dataVec = new Vector();
        dataVec.addElement("unknown");
        alphaResults.addElement(dataVec);

        dataVec = new Vector();
        dataVec.addElement("capture");
        alphaResults.addElement(dataVec);

        dataVec = new Vector();
        dataVec.addElement("scan");
        alphaResults.addElement(dataVec);

        dataVec = new Vector();
        dataVec.addElement("both");
        alphaResults.addElement(dataVec);
        System.out.println("#### NEEDED RESULTS");
        System.out.println(alphaResults);
      }
  captured

      // status capture
      if (alphaColumn.equals("captured")) {
        alphaResults = new Vector();

        dataVec = new Vector();
        dataVec.addElement("Header");
        alphaResults.addElement(dataVec);

        dataVec = new Vector();
         dataVec.addElement(" ");
         alphaResults.addElement(dataVec);

        dataVec = new Vector();
        dataVec.addElement("unknown");
        alphaResults.addElement(dataVec);

        dataVec = new Vector();
        dataVec.addElement("need");
        alphaResults.addElement(dataVec);

        dataVec = new Vector();
        dataVec.addElement("yes");
        alphaResults.addElement(dataVec);

        dataVec = new Vector();
        dataVec.addElement("no");
        alphaResults.addElement(dataVec);


        dataVec = new Vector();
        dataVec.addElement("NA");
        alphaResults.addElement(dataVec);


        System.out.println(alphaResults);
      }

      // status scanned
           if (alphaColumn.equals("pic_org")) {
             alphaResults = new Vector();

             dataVec = new Vector();
             dataVec.addElement("Header");
             alphaResults.addElement(dataVec);

             dataVec = new Vector();
             dataVec.addElement(" ");
             alphaResults.addElement(dataVec);

             dataVec = new Vector();
             dataVec.addElement("unknown");
             alphaResults.addElement(dataVec);

             dataVec = new Vector();
             dataVec.addElement("need");
             alphaResults.addElement(dataVec);

             dataVec = new Vector();
             dataVec.addElement("yes");
             alphaResults.addElement(dataVec);

             dataVec = new Vector();
             dataVec.addElement("no");
             alphaResults.addElement(dataVec);

             dataVec = new Vector();
             dataVec.addElement("na");
             alphaResults.addElement(dataVec);


             System.out.println(alphaResults);
           }

           if (alphaColumn.equals("extra9")) {
              alphaResults = new Vector();


              dataVec = new Vector();
              dataVec.addElement("Header");
              alphaResults.addElement(dataVec);

              dataVec = new Vector();
              dataVec.addElement(" ");
              alphaResults.addElement(dataVec);

              dataVec = new Vector();
              dataVec.addElement("unknown");
              alphaResults.addElement(dataVec);

              dataVec = new Vector();
              dataVec.addElement("yes");
              alphaResults.addElement(dataVec);

              dataVec = new Vector();
              dataVec.addElement("no");
              alphaResults.addElement(dataVec);

              dataVec = new Vector();
              dataVec.addElement("NLA");
              alphaResults.addElement(dataVec);


              System.out.println(alphaResults);
            }



     // need to put in an " " for none in all supporting tables
     boolean need_space_record = false;

     query = "select id from storetable where data=' '";

     Vector spaceQuery = query(con,query,false);

     if (spaceQuery == null || spaceQuery.size() <2)
       need_space_record = true;




      pstmt = con.prepareStatement("insert into " + storeTable +
                                   " (data, active)" +
                                   " values (?,?)");


      // insert the space record
      if (need_space_record) {
        pstmt.setString(1," ");
        pstmt.setInt(2,1);
        pstmt.executeUpdate();
      }


      int len = alphaResults.size();

      int id = -1;

      for (int i = 1; i < len; i++) {
        dataVec = (Vector) alphaResults.elementAt(i);
        value = (String) dataVec.elementAt(0);

        // special filter the garbage out
        if (alphaColumn.equals("nla") && value.equals("Y"))
          continue;


        if (value == null || value.equals(""))
          continue;

        //out.println(removeLeadingTrailingSpaces(value));
        if (value != null) {
          value = value.replaceAll(",","");
          value = value.replaceAll("'","");
        }


        // check if the value already exists
        id = getTableColumnDataId(con,storeTable,value);

        if (id != -1)
          continue;




        pstmt.setString(1, removeLeadingTrailingSpaces(value));
        pstmt.setBoolean(2, true);
        pstmt.executeUpdate();
      }
      pstmt.close();
      con.commit();
    //  out.close();

    }
    catch (Exception e) {
      System.out.println("Error: Unable to add " + storeTable);
      e.printStackTrace(System.out);
    }
  }
  public String makeUpdateValue(String value)
{
  if(value == null || value.equals(""))
      return "null";
  else
      return (new StringBuilder()).append("'").append(value).append("'").toString();
}

  */
  /*
public void import_research(Connection con, AlphaDB alphaDb)
{
  Vector alphaResults;
  Vector dataVec;
  String update;
  String storeTable;
  String newColumns;
  Vector columns;
  String query = null;
  alphaResults = null;
  dataVec = null;
  String value = null;
  update = "";
  String alphaTable = "research";
  storeTable = "tbl_research";
  newColumns = "";
  newColumns = (new StringBuilder()).append(newColumns).append("user_id,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("name,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("phone,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("address,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("city,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("state,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("zipcode,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("brand,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("model,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("part_number,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("part_description,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("cost,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("sell,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("source,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("entered_date,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("data,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("entered_by,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("unit_type,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("unit_age,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("send_inv,").toString();
  newColumns = (new StringBuilder()).append(newColumns).append("part_count").toString();
  String name = null;
  String phone = null;
  String address = null;
  String city = null;
  String state = null;
  String zipcode = null;
  String brand = null;
  String model = null;
  String part_number = null;
  String part_description = null;
  String cost = null;
  String sell = null;
  String source = null;
  String entered_date = null;
  String data = "";
  String entered_by = null;
  String unit_type = null;
  String unit_age = null;
  String send_inv = null;
  String rec_type = null;
  String count = null;
  String time = null;
  query = "select DISTINCT ";
  query = (new StringBuilder()).append(query).append("*").toString();
  query = (new StringBuilder()).append(query).append(" from ").append(alphaTable).toString();
  alphaResults = alphaDb.query(query, "jdbc:odbc:research");
  Vector header = (Vector)alphaResults.elementAt(0);
  System.out.println(header);
  query = "select DISTINCT ";
  query = (new StringBuilder()).append(query).append("*").toString();
  query = (new StringBuilder()).append(query).append(" from ").append(storeTable).toString();
  Vector storeResults = query(con, query, false);
  columns = (Vector)storeResults.elementAt(0);
  System.out.println(columns);
  java.util.Date new_date = new java.util.Date();
  String todaysDate = DateFormat.getDateTimeInstance(3, 2).format(new_date);
  Vector record_vec = null;
  Vector alphaDataVec = null;
  int len = alphaResults.size();
  String columnName = null;
  columns = (Vector)alphaResults.elementAt(0);
  System.out.println(columns);
  for(int i = 1; i < len; i++)
  {
      if(i % 1000 == 1)
          System.out.println(i - 1);
      alphaDataVec = (Vector)alphaResults.elementAt(i);
      name = null;
       phone = null;
       address = null;
       city = null;
       state = null;
       zipcode = null;
       brand = null;
       model = null;
       part_number = null;
       part_description = null;
       cost = null;
       sell = null;
       source = null;
       entered_date = null;
       data = null;
       entered_by = null;
       unit_type = null;
       unit_age = null;
       send_inv = null;
       rec_type = null;
       count = null;
       time = null;
      for(int x = 0; x < alphaDataVec.size(); x++)
      {
          columnName = (String)columns.elementAt(x);
           value = (String)alphaDataVec.elementAt(x);
          value = removeLeadingTrailingSpaces(value);
          if(value != null)
          {
              value = value.replaceAll(",", "");
              value = value.replaceAll("'", "");
              value = value.replaceAll("'\t", " ");
              if(value.equals("null"))
                  value = null;
          }
          if(columnName.equals("phone"))
          {
              phone = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("name"))
          {
              name = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("addr"))
          {
              address = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("city"))
          {
              city = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("state"))
          {
              state = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("zipcode"))
          {
              zipcode = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("brand"))
          {
              brand = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("model"))
          {
              model = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("part_numb"))
          {
              part_number = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("part_desc"))
          {
              part_description = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("cost"))
          {
              cost = value;
              continue;
          }
          if(columnName.equals("sell"))
          {
              sell = value;
              continue;
          }
          if(columnName.equals("source"))
          {
              source = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("date"))
          {
              entered_date = value;
              continue;
          }
          if(columnName.equals("time"))
          {
              time = value;
              continue;
          }
          if(columnName.indexOf("note") != -1)
          {
              if(value != null)
                  data = (new StringBuilder()).append(data).append(" ").append(value).toString();
              continue;
          }
          if(columnName.equals("ent_by"))
          {
              entered_by = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("unit_type"))
          {
              unit_type = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("unit_age"))
          {
              unit_age = value;
              continue;
          }
          if(columnName.equals("send_inv"))
          {
              send_inv = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("unit_type"))
          {
              rec_type = makeUpdateValue(value);
              continue;
          }
          if(columnName.equals("count"))
              count = value;
      }

      if(data != null)
          data = data.replaceAll("\"", "'");
      else
          data = "null";
      if(entered_date != null && time != null)
          entered_date = (new StringBuilder()).append("to_date('").append(entered_date).append(" ").append(time).append("', 'yyyy-mm-dd hh24:mi:ss')").toString();
      update = "1, ";
      update = (new StringBuilder()).append(update).append(name).append(",").toString();
      update = (new StringBuilder()).append(update).append(phone).append(",").toString();
      update = (new StringBuilder()).append(update).append(address).append(",").toString();
      update = (new StringBuilder()).append(update).append(city).append(",").toString();
      update = (new StringBuilder()).append(update).append(state).append(",").toString();
      update = (new StringBuilder()).append(update).append(zipcode).append(",").toString();
      update = (new StringBuilder()).append(update).append(brand).append(",").toString();
      update = (new StringBuilder()).append(update).append(model).append(",").toString();
      update = (new StringBuilder()).append(update).append(part_number).append(",").toString();
      update = (new StringBuilder()).append(update).append(part_description).append(",").toString();
      update = (new StringBuilder()).append(update).append(cost).append(",").toString();
      update = (new StringBuilder()).append(update).append(sell).append(",").toString();
      update = (new StringBuilder()).append(update).append(source).append(",").toString();
      update = (new StringBuilder()).append(update).append(entered_date).append(",").toString();
      update = (new StringBuilder()).append(update).append(entered_by).append(",").toString();
      update = (new StringBuilder()).append(update).append(unit_type).append(",").toString();
      update = (new StringBuilder()).append(update).append(unit_age).append(",").toString();
      update = (new StringBuilder()).append(update).append(send_inv).append(",").toString();
      update = (new StringBuilder()).append(update).append(rec_type).append(",").toString();
      update = (new StringBuilder()).append(update).append(count).toString();
      update = (new StringBuilder()).append("INSERT INTO TBL_RESEARCH (").append(newColumns).append(") values (").append(update).append(")").toString();
      update(con, update);
  }

  Exception e;
  try
  {
      con.commit();
  }
  // Misplaced declaration of an exception variable
  catch(Exception ee)
  {
      ee.printStackTrace(System.out);
  }

  System.out.println((new StringBuilder()).append("Error: Unable to add ").append(storeTable).toString());
  System.out.println(columns);
  System.out.println(dataVec);
  System.out.println(update);

  try
  {
      con.commit();
  }
  // Misplaced declaration of an exception variable
  catch(Exception ee)
  {
      ee.printStackTrace(System.out);
  }

  try
  {
      con.commit();
  }
  catch(Exception ee)
  {
      ee.printStackTrace(System.out);
  }
 }
 */

}

