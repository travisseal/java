package com.remotes.database;

import java.sql.Connection;
import java.util.Vector;

import com.remotes.utils.BaseUtils;
 

public class dbUtils {
	
	static BaseUtils __utils = new BaseUtils();	
	
	public static int getTableColumnDataId(Connection con, String table, String data)
	  {
	    int retval = -1;
	    String query = null;
	    Vector dataVec = null;
	    Vector queryResults = null;
	    String value = null;

	    query = "SELECT id from " + table + " ";
	    query += "WHERE data='" + data + "'";
	   // System.out.println(query);
	    

	    queryResults = MasterDB.getInstance().query(con,query,false);
	    //System.out.println(query);


	  if (data == null || data.equals(""))
	    return -1;

	    if (queryResults.size() < 2) {
	      String update = "insert into " + table + " (data,active) values ";
	      update += "('" + data + "', 1)";
	      MasterDB.getInstance().update(con,update);
	    }

	    queryResults = MasterDB.getInstance().query(con,query,false);
	    if (queryResults.size() < 2) {
	      return -1;
	    }

	    dataVec = (Vector)queryResults.elementAt(1);
	    value = (String)dataVec.elementAt(0);
	    retval = __utils.stringToInt(value);

	   // System.out.println(data + " -> " + retval);

	    return retval;
	  }
	
	public static int getPartnumberIds(Connection con, String part_number)
	{
		// returns the id of the part number in the product master table. If
		// it does not exist, -1 is returned
		String query = "select distinct id from tbl_product_master ";
		query += "where partnumber = '" + part_number + "'";
		
		 Vector dataVec = MasterDB.getInstance().query(con,query,false);
		 
		
		 
		 if (dataVec.size() < 2)
			 return -1;
		 
		 // get the id
		 dataVec = (Vector)dataVec.elementAt(1);
		 return __utils.stringToInt((String)dataVec.elementAt(0));
	}
	
	public static int getPartnumberIdSubPart(Connection con, String brand, String sub_part)
	{
		// returns the id of the part number in the product master table. If
		// it does not exist, -1 is returned
		String query = "";
		query = "select distinct prod_master_id from tbl_product_model ";
		query += "where data = '" + sub_part + "'";
		
		
		//System.out.println(query);
		 Vector dataVec = MasterDB.getInstance().query(con,query,false);
		 
		// System.out.println(dataVec);
		 if (dataVec.size() < 2) {
			 
			 	query = "select distinct prod_mast.id, prod_mast.partnumber partnumber, prod_mast.mpn mpn "; 
				query += "from remote_master.tbl_product_brand brand, remote_master.tbl_product_master prod_mast ";
				query += "where prod_mast.brand_id =  brand.id (+) ";
				query += "AND brand.data = '" + brand + "'";
				query += "AND (sub_part = '" + sub_part + "' ";
				query += "OR   sub_remote = '" + sub_part + "' ";
				query += "OR   alt_remote_pn = '" + sub_part + "' ";
				query += "OR   partnumber = '" + sub_part + "' ";
				query += "OR   remote = '" + sub_part + "' ";
				query += "OR   sub_part = '" + sub_part + "' ";

				query += "OR   alt_remote = '" + sub_part + "') ";
		
			 
				dataVec = MasterDB.getInstance().query(con,query,false);
			 
				if (dataVec.size() < 2)
					return -1;
			 
			 
		 }
			 
		 
		 
		 // get the id
		 dataVec = (Vector)dataVec.elementAt(1);
		
		 return __utils.stringToInt((String)dataVec.elementAt(0));
	}
	
	public static Vector getEbayIds(Connection con, int part_id)
	{
		Vector retval = null;
		
		String query = "select data from tbl_ebay_id ";
		query += "where prod_master_id = " + part_id;
		
		retval = MasterDB.getInstance().query(con,query,false);
		
		return retval;
	}
	
	public static Vector getEbayIdsTmp(Connection con, String brand, String mpn)
	{
		Vector retval = null;
		
		String query = "select ebayid from remote_master.TBL_EBAY_ID_TMP ";
		query += "where mpn = '" + mpn + "' ";
	//	query += "where brand = '" + brand + "' ";
	//	query += "and mpn = '" + mpn + "' ";
		
		
		retval = MasterDB.getInstance().query(con,query,false);
		
		// if no results then check if the mpn starts with zero and remove
		if (retval.size() <2 && mpn.startsWith("0")) {
			
			return getEbayIdsTmp(con, brand, mpn.substring(1,mpn.length()));
			
			
		}
			
		
		return retval;
	}
	
	public static int getPartnumberId(Connection con, String brand, String part_number)
	{
		// returns the id of the part number in the product master table. If
		// it does not exist, -1 is returned
		String query = "";
		query += "select distinct prod_mast.id, prod_mast.partnumber partnumber, prod_mast.mpn mpn "; 
		query += "from remote_master.tbl_product_brand brand, remote_master.tbl_product_master prod_mast ";
		query += "where prod_mast.brand_id =  brand.id (+) ";
		query += "AND brand.data = '" + brand + "'";
		query += "AND (partnumber = '" + part_number + "' OR mpn = '" + part_number + "')";
		
		//System.out.println(query);
		 Vector dataVec = MasterDB.getInstance().query(con,query,false);
		 
		// System.out.println(dataVec);
		 if (dataVec.size() < 2)
			 return -1;
		 
		 
		 // get the id
		 dataVec = (Vector)dataVec.elementAt(1);
		
		 return __utils.stringToInt((String)dataVec.elementAt(0));
	}
	
	public static String getPartnumberTmp(Connection con, String rt_number)
	{
		String retval = null;
		String query = "";
		query += "SELECT distinct part_numbr from remote_dbf.dbf_inv_mast  "; 
		query += "WHERE sub_rmt_n = '" + rt_number + "' ";
		query += "OR sub_part_n = '" + rt_number + "' ";
		query += "OR sub_part_n = '" + rt_number + "' ";
		query += "OR sub_rmt_n = '" + rt_number + "' ";
		query += "OR extra3 = '" + rt_number + "' ";
		
		 Vector dataVec = MasterDB.getInstance().query(con,query,false);
		 
		// System.out.println(dataVec);
		 if (dataVec.size() < 2)
			 return null;
		 
		 
		 // get the id
		 dataVec = (Vector)dataVec.elementAt(1);
	
		
		
		return (String)dataVec.elementAt(0);
	}
	
	public static String getPartnumber(Connection con, int productMasterId)
	{
		String retval = null;
		String query = "";
		query += "select distinct prod_mast.partnumber partnumber, prod_mast.mpn mpn "; 
		query += "from remote_master.tbl_product_brand brand, remote_master.tbl_product_master prod_mast ";
		query += "where prod_mast.brand_id =  brand.id (+) ";
		query += "AND prod_mast.id = " + productMasterId;
		
		 Vector dataVec = MasterDB.getInstance().query(con,query,false);
		 
		// System.out.println(dataVec);
		 if (dataVec.size() < 2)
			 return null;
		 
		 
		 // get the id
		 dataVec = (Vector)dataVec.elementAt(1);
	
		
		
		return (String)dataVec.elementAt(0);
	}
	
	
	
	

	
	public static int getModelId(Connection con, String model, String partnumber)
	{
		/*
		System.out.println("Brand: " + brand);
		System.out.println("Model: " + model);
		System.out.println(" Part: " + partnumber);
		System.out.println();
		*/
		
		
		// Set empty data to null		
		if (model != null && model.equals(""))
			model = null;
		
		if (partnumber == null) {
			System.out.println("Error: in getModelId - Partnumber is NULL");
			System.exit(1);
		}
		// returns the id of the part number in the product master table. If
		// it does not exist, -1 is returned
		String query = "select distinct id from view_product_models ";
		
		if (model == null)
			query += "where model is null AND partnumber = '" + partnumber + "'";
		
		else 
			query += "where model = '" + model + "' AND partnumber = '" + partnumber + "'";

			
		Vector dataVec = MasterDB.getInstance().query(con,query,false);
		 
		//System.out.println(query);
		//System.out.println(dataVec);
		if (dataVec.size() < 2)
			return -1;

		// get the id
		dataVec = (Vector)dataVec.elementAt(1);
		return __utils.stringToInt((String)dataVec.elementAt(0));
	}
	


	public static int getBrandId(Connection con, String brand)
	{
		// returns the id of the part number in the product master table. If
		// it does not exist, -1 is returned
		String query = "select distinct id from tbl_product_brand ";
		query += "where data = '" + brand + "'";
		
		 Vector dataVec = MasterDB.getInstance().query(con,query,false);
		
		 
		 if (dataVec.size() < 2)
			 return -1;
		 
		 // get the id
		 dataVec = (Vector)dataVec.elementAt(1);
		 return __utils.stringToInt((String)dataVec.elementAt(0));
	}


}
