package com.remotes.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;

import com.remotes.database.MasterDB;

public class AmazonUtils {
	
	static BaseUtils __utils = new BaseUtils();
	private static int __total_count_not_used = 0;
	private static int __total_count_models = 0;

	

	public String getBrandFromVector(Vector modelVec) {
		String retVal = null;
		Vector dataVec = null;

		if (modelVec != null) {
			dataVec = (Vector) modelVec.elementAt(0);

			retVal = (String) dataVec.elementAt(0);
		}
		return retVal;
	}

	
	public String getAdditionPartInfo(Connection con, String model)
	{
		String retval = null;
		
		Vector alphaResults = null;
		//AlphaDB alphaDb = new AlphaDB();

		Vector dataVec = null;
		String price = null;

		String query = "select distinct extra8 ";
		query += " from " + "dbf_" + "inv_mast ";
		query += " WHERE (ACCESSORY='REMOTE CONTROL' OR ACCESSORY='REMOTE BASE UNIT') ";
		query += "AND mod_rmt_pt LIKE '" + model + "%'";

		// System.out.println(query);
		// Connection con = open();
		alphaResults = MasterDB.getInstance().query(con, query, false);
		//alphaResults = MasterDB.getInstance().query(query, false, __host, __db_username, __db_password);
		//alphaResults = MasterDb.query(query, __connect_str);

		if (alphaResults.size() > 1) {
			dataVec = (Vector) alphaResults.elementAt(1);
			return (String) dataVec.elementAt(0);
		}
		
		
		
		return retval;
	}
	
	public String getTabs(int count) {
		// returns a string that contains the number of tabs specified
		String retval = "";
		for (int i = 1; i < count; i++)
			retval += "\t";

		return retval;
	}

	public static Vector getTitleDescriptVector(Vector modelVec, String title, String description, int max_length, int max_rows)
	{
		Vector retVal = new Vector();
		
		//System.out.println("base title = " + title);
		
		// Need to get the length of the title without the keyword <MODELS>
		int baseTitleLength = title.replaceAll("<MODELS>", "").length();
	
		
		//System.out.println("Length to work with " + baseTitleLength);
		
		int start_index = 0;
		int end_index = 0;
		
		String models = "";
		String model = "";
		Vector tmpVec = new Vector();
		Vector dataVec = null;
		
		String models_for_title = "";
		String new_title = null;
		String new_description = null;
		Vector titleVec = null;
		Vector dModelVec = null;
		Vector descriptVec = null;
		int row = 1;
	
		
		
		setTotal_count_models(getTotal_count_models() + modelVec.size());
		
		//System.out.println("modelVec=" + modelVec);
		while (start_index < modelVec.size())
		{
			//System.out.println("start_index=" + start_index);
			end_index = getModelMaxIndexFromVector(modelVec, start_index, max_length - baseTitleLength);
			
			
			titleVec = new Vector();
			dModelVec = new Vector();
			
			//System.out.println("EndIndex=" + end_index);
			// build the title
			for (int i = start_index; i < end_index; i++) {
				dataVec = (Vector)modelVec.elementAt(i);
				model = (String)dataVec.elementAt(1);
				
				tmpVec = new Vector();
				tmpVec.addElement("brand");
				tmpVec.addElement(model);
				
				titleVec.addElement(tmpVec);
				
				dModelVec.addElement(tmpVec);
			}
			
				
				
				
		//	System.out.println(tmpVec);
			models_for_title = getModelListFromVector(titleVec,
					max_length - title.length() - 2);
			
			
			
		
			
			
			row++;
			
			if (row > max_rows && end_index < modelVec.size()) {
				// display count and models not included
				int count = modelVec.size() - end_index;
				Vector data = null;
				System.out.println("***************************");
				System.out.println("   Warning: " + count + " Models not included in title");
				System.out.println("***************************");
				
				setTotal_count_not_used(getTotal_count_not_used() + count);
				
				// print the models, also add them to the current description
				for (int x=start_index;x<modelVec.size();x++) {
					data = (Vector)modelVec.elementAt(x);
					
					// check if we are on the max row. If so, we add the rest of the models to the description. Title stays the same
					dModelVec.addElement(data);
					//System.out.println((String)data.elementAt(1));
				}
				

			}
			
			if (description != null) {
				String modelTable = modelsToTable(dModelVec);
				new_description = description.replaceAll("<MODELS>",
						modelTable);
			}
			
			// add as a new title
			new_title = title.replaceAll("<MODELS>", models_for_title);	
			
			dataVec = new Vector();
			dataVec.addElement(new_title);
			dataVec.addElement(new_description);
			
			retVal.addElement(dataVec);
			
			//System.out.println("Description models = " + dModelVec);
			start_index = end_index;
			tmpVec.clear();
			dModelVec.clear();
	
			if (row > max_rows && end_index < modelVec.size()) 
				break; // while loop
		}
		
	//	System.out.println(retVal);
		return retVal;
	}
	
	public static String modelsToTable(Vector models)
	{
		String retval = "";
		String model = null;
		//t count = 0;
		Vector datavec = null;
		
		for (int i = 0;i<models.size();i++)
		{
			//unt++;
			datavec = (Vector)models.elementAt(i);
			model = (String)datavec.elementAt(1);
		
			retval += "<li>" + model + "&nbsp;</li>";
			//System.out.println(model);
	
		}
		//retval += "</ul>";
		
		return retval;
		
	}
	public Vector getFilters(String data) {
		Vector retval = new Vector();
		Vector dataVec = new Vector();
		String part_no = null;
		String line = "";
		char ch = '1';
		System.out.println(data);
		for (int i = 0; i < data.length(); i++) {
			ch = data.charAt(i);

			if (ch == '\n') {

				dataVec.addElement(line);
				line = "";
				continue;
			}
			line += ch;
		}
		dataVec.addElement(line);
		System.out.println(dataVec);

		return retval;
	}

	
	public String getCustPrice(Connection con, String model, String table_prefix) {
		Vector alphaResults = null;
		//AlphaDB alphaDb = new AlphaDB();

		Vector dataVec = null;
		String price = null;

		String query = "select distinct cust_cost ";
		query += " from " + table_prefix + "inv_mast ";
		query += " WHERE (ACCESSORY='REMOTE CONTROL' OR ACCESSORY='REMOTE BASE UNIT') ";
		query += "AND mod_rmt_pt LIKE '" + model + "%'";

		// System.out.println(query);
		// Connection con = open();
		alphaResults = MasterDB.getInstance().query(con, query, false);
		//alphaResults = MasterDB.getInstance().query(query, false, __host, __db_username, __db_password);
		//alphaResults = MasterDb.query(query, __connect_str);

		if (alphaResults.size() > 1) {
			dataVec = (Vector) alphaResults.elementAt(1);
			return (String) dataVec.elementAt(0);
		}
		return null;
	}

	public Vector getModels(Connection con, String model, String table_prefix) {
		Vector retVal = new Vector();
		Vector alphaResults = null;
		//AlphaDB alphaDb = new AlphaDB();
		Vector dataVec = null;
		String data = null;
		
		Vector tmpVec = null;
		
//	if (model.startsWith("RT"))
//			model = model.substring(2, model.length());
			
		//	System.out.println(model);

		String query = "select distinct brand, mod_rmt_pt ";
		query += " from " + table_prefix + "inv_mast ";
		query += " WHERE (ACCESSORY='REMOTE CONTROL' OR ACCESSORY='REMOTE BASE UNIT') ";
		query += "AND (part_numbr='" + model + "' ";
		query += "OR extra3='" + model + "'";
		query += "OR mod_rmt_pt='" + model + "' ";
		query += "OR remote_num='" + model + "' ";
		query += "OR sub_rmt_n='" + model + "' ";
		query += "OR model='" + model + "' ";
		query += "OR sub_part_n='" + model + "')";

		//System.out.println(query);
		alphaResults = MasterDB.getInstance().query(con, query, false);
	
		
		//System.out.println(alphaResults);
		if (alphaResults.size() > 1) {
			for (int i = 1; i < alphaResults.size(); i++) {
				dataVec = (Vector) alphaResults.elementAt(i);
				data = (String)dataVec.elementAt(1);
				
				// filter out any redi-remotes
				if (!data.startsWith("RT"))
					retVal.addElement(dataVec);
			}
		} else {
			// System.out.println(model);
			
			model = model.replaceAll("-", "");

			query = "select distinct brand, mod_rmt_pt ";
			query += " from " + table_prefix + "inv_mast ";
			query += " WHERE (ACCESSORY='REMOTE CONTROL' OR ACCESSORY='REMOTE BASE UNIT') ";
			query += "AND (part_numbr='" + model + "' ";
			query += "OR extra3='" + model + "'";
			query += "OR mod_rmt_pt='" + model + "' ";
			query += "OR remote_num='" + model + "' ";
			query += "OR sub_rmt_n='" + model + "' ";
			query += "OR model='" + model + "' ";
			query += "OR sub_part_n='" + model + "')";

			alphaResults = MasterDB.getInstance().query(con, query, false);
			// System.out.println(query);
			//alphaResults = MasterDB.getInstance().query(query, false, __host, __db_username, __db_password);
			// System.out.println(alphaResults);

			if (alphaResults.size() > 1) {
				for (int i = 1; i < alphaResults.size(); i++) {
					dataVec = (Vector) alphaResults.elementAt(i);
					data = (String)dataVec.elementAt(1);
					
					// filter out any redi-remotes
					if (!data.startsWith("RT"))
						retVal.addElement(dataVec);
				}
			}
		}
		return retVal;
	}
	public String formatUpc(String upc) {
		if (true)
			return upc;

		String retVal;

		retVal = upc.substring(0, 1);
		retVal += "-";
		retVal += upc.substring(1, 6);
		retVal += "-";
		retVal += upc.substring(6, 11);
		retVal += "-";
		retVal += upc.substring(11, 12);

		return retVal;
	}

	public String getFieldValue(String fieldname, Vector configVec) {

		Vector dataVec = null;
		String field = null;

		if (configVec == null)
			return null;

		for (int i = 0; i < configVec.size(); i++) {
			dataVec = (Vector) configVec.elementAt(i);

			field = (String) dataVec.elementAt(0);

			if (field.equals(fieldname))
				return (String) dataVec.elementAt(1);
		}

		return null;
	}

	public Vector readConfigFile(String filepath) {
		Vector rval = new Vector();
		Vector dataVec = new Vector();
		String fieldname = null;
		String contents = "";

		BufferedReader theReader = null;
		try {
			theReader = new BufferedReader(new FileReader(filepath));

			String line;
			// int index;

			while (true) {
				line = theReader.readLine();
				if (line == null) {
					break;
				}

				// check if we have a field name - indicated by starting with %
				if (line.startsWith("%END")) {
					dataVec = new Vector();
					dataVec.addElement(fieldname);
					dataVec.addElement(contents);

					rval.addElement(dataVec);

					contents = "";
				} else if (line.startsWith("%")) {
					fieldname = line.substring(1, line.length());
					// System.out.println("Fieldname = " + fieldname);
				} else {
					contents += line + " ";
				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}

		finally {
			if (theReader != null) {
				try {
					theReader.close();
				} catch (IOException e) {

					e.printStackTrace(System.out);
				}
			}
		}

		theReader = null;
		return rval;
	}

	public Vector stringToVectorTab(String str) {

		Vector retval = new Vector();
		String tempstr = "";
		int len = str.length();
		for (int i = 0; i < len; i = i + 1) {
			if ((str.charAt(i) == '\t')) {
				// check the length of the string greater the 1 char
				// if (tempstr.length() > 0) {
				// replace NULL with a null string
				if (tempstr.equals("NULL"))
					tempstr = "";

				retval.addElement(tempstr);
				// }
				tempstr = "";
			} else {
				tempstr += str.charAt(i);
			}

		}
		// Make sure we get the last element
		if (tempstr.length() > 0) {
			retval.addElement(tempstr);
		}

		return retval;
	}

	public Vector readFileIntoVector(String filepath) {

		Vector dataVec = null;
		Vector rval = new Vector();
		BufferedReader theReader = null;
		try {
			theReader = new BufferedReader(new FileReader(filepath));

			String line;
			// int index;

			int count = 0;

			while (true) {

				line = theReader.readLine();
				count++;

				if (count == 1000)
					break;
				// Separate into fields
				// System.out.println(line);
				if (line == null || line == "")
					continue;

				dataVec = stringToVectorTab(line);

				if (dataVec.size() < 4)
					continue;

				if (line == null) {
					break;
				}

				rval.addElement(line);
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}

		finally {
			if (theReader != null) {
				try {
					theReader.close();
				} catch (IOException e) {

					e.printStackTrace(System.out);
				}
			}
		}

		theReader = null;
		return rval;
	}
	
	public static int getModelMaxIndexFromVector(Vector modelVec, int start_index, int max_size) {
		// walks through models and creates a comma separated string of models
		// without exceeding the max size. If max size is reached, that model
		// will be put into the next element of the vector...and so on

		Vector retval = new Vector();
		Vector dataVec = null;
		String data = "";

		String model = null;

		if (modelVec != null) {

			for (int i = start_index; i < modelVec.size(); i++) {

				dataVec = (Vector) modelVec.elementAt(i);
				model = (String) dataVec.elementAt(1);

				model = __utils.removeLeadingTrailingSpaces(model);

				// System.out.println("compare = " + (retVal.length() +
				// model.length() + 6));
				// check if by adding the model we exceed the max char count

				if (max_size > 0) {
					// System.out.println("data = " + data.length());
					// System.out.println("model = " + model.length());

					if ((data.length() + model.length() + 2) > max_size) {
						return i;
					}
				}
				// add the model to the list
				if (data.length() != 0) {
					data += ", ";
				}
				data += model;
				
			}
		}

		return modelVec.size();
	}

	public Vector getModelListVectorFromVector(Vector modelVec, int max_size) {
		// walks through models and creates a comma separated string of models
		// without exceeding the max size. If max size is reached, that model
		// will be put into the next element of the vector...and so on

		Vector retval = new Vector();
		Vector dataVec = null;
		String data = "";

		String model = null;

		if (modelVec != null) {

			for (int i = 0; i < modelVec.size(); i++) {

				dataVec = (Vector) modelVec.elementAt(i);
				model = (String) dataVec.elementAt(1);

				model = __utils.removeLeadingTrailingSpaces(model);

				// System.out.println("compare = " + (retVal.length() +
				// model.length() + 6));
				// check if by adding the model we exceed the max char count

				if (max_size > 0) {
					// System.out.println("data = " + data.length());
					// System.out.println("model = " + model.length());

					if ((data.length() + model.length() + 2) > max_size) {
						// System.out.println(
						// "   Warning: Max character count exceeded, trucating");
						retval.addElement(data);
						// System.out.println(data);
						data = "";

					}
				}
				// add the model to the list
				if (data.length() != 0) {
					data += ", ";
				}
				data += model;
			}
		}

		retval.addElement(data);
		/*
		 * System.out.println(retval); System.out.println(retval.size());
		 * for(int i=0;i<retval.size();i++) { data =
		 * (String)retval.elementAt(i); System.out.println(i + "= " + data); }
		 */
		return retval;
	}

	public static String getModelListFromVector(Vector modelVec, int max_size) {
		
		int char_count = 0;

		String retVal = "";

		Vector dataVec = null;
		String model = null;
		
		int spacer = 0;

		if (modelVec != null) {

			for (int i = 0; i < modelVec.size(); i++) {

				dataVec = (Vector) modelVec.elementAt(i);
				model = (String) dataVec.elementAt(1);

				model = __utils.removeLeadingTrailingSpaces(model);

				//System.out.println("compare = " + (retVal.length() +
				// model.length() + 2));
				// check if by adding the model we exceed the max char count
				if (i != 0)
					spacer = 2;
				
				if (max_size > 0) {
					
					//System.out.println(model);
					if (retVal.length() + model.length() + spacer > max_size) {
						// System.out.println(
						// "   Warning: Max character count exceeded, trucating");
						return retVal;
					}
				}
				// add the model to the list
				if (i != 0) {
					retVal += ", ";
				}
				retVal += model;
			}
		}
		return retVal;
	}

	public static void setTotal_count_not_used(int total_count_not_used) {
		AmazonUtils.__total_count_not_used = total_count_not_used;
	}

	public static int getTotal_count_not_used() {
		return __total_count_not_used;
	}

	public static void setTotal_count_models(int total_count_models) {
		AmazonUtils.__total_count_models = total_count_models;
	}

	public static int getTotal_count_models() {
		return __total_count_models;
	}

	
	
}
