package com.remotes.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;

public class FileUtils {

	
	static BaseUtils __utils = new BaseUtils();	
	
	  
	  public void insertFile(String filepath, PrintWriter out
      ) {

    BufferedReader contentsReader = null;
    String contentsline;

    if (!__utils.fileExists(filepath)) {
      return;
    }

    try {
      // get the page and display the contents
  contentsReader = new BufferedReader(new FileReader(filepath));

  System.out.println("###Reading file - " + filepath);
// Read until we find the body of the HTML doc
      while (true) {
        contentsline = contentsReader.readLine();
        if (contentsline == null) {
          break;
        }
        out.println(contentsline);

      }

    }
    catch (Exception e) {
    	//__utils.sendErrorReport(e);
      e.printStackTrace(System.out);
    }

    finally {
      if (contentsReader != null) {
        try {
          contentsReader.close();
        }
        catch (IOException e) {
         // d_utils.sendErrorReport(e);
          e.printStackTrace(System.out);
        }
      }
    }
  }
	  
  public RemoteNameValueVector getDirectoryContents(String directory, String extension) {
		    // returns a vector of files in a directory. If extension is null, then all files are in the vector otherwise, only
		    // the files that have the following extension is returned.
	  	RemoteNameValueVector retval = new RemoteNameValueVector();
		    String filename = null;
		    Vector dataVec = null;

		    if (directory == null)
		      return retval;

		    
		    
		    //  System.out.println("Dir =" + directory);
		    //  System.out.println("Extension" + extension);

		    // Open the directory
		    java.io.File fp = new File(directory);

		    // directory does not exist
		    if (!fp.exists())
		      return retval;

		    if (fp.list() == null)
		      return retval;
		    
		    String line = null;
		    // change extension to be lowercase
		    if (extension != null)
		    	extension = extension.toLowerCase();

		   
		    	//System.out.println("/website/amazon/bin/getdirectory.bat " + directory);
		    	Vector results = __utils.CmdExec("sh dir /b " + directory);
		    	
		    	for (int i=0;i<results.size();i++) {
		    		
		    		line = (String)results.elementAt(i);
			        //String d = br.readLine();
			      System.out.println(line);
			        filename = getLeaf(line);
			        if (extension == null || filename.toLowerCase().endsWith(extension)) {
			        	//System.out.println(filename);
			        	retval.add(filename, filename);
				      }
			    }
		    	 return retval;
		    	    
		    
		    // step through the files and check filter
		/*    int len = fp.list().length;
		    for (int i = 0; i < len; i++) {
		    	
		    	if ( i % 100 == 1)
		    		System.out.println(i);
		      filename = fp.list()[i];
		      filename = filename.toLowerCase();
		      
		      //System.out.println(filename);

		      if (extension == null || filename.toLowerCase().endsWith(extension)) {
		         retval = retval.tree_AddString(retval, filename);
		      }
		    }
		    */
		   
		  } 


  public Vector getDirectoryListing(String directory, String extension) {
    // returns a vector of files in a directory. If extension is null, then all files are in the vector otherwise, only
    // the files that have the following extension is returned.
    Vector retval = new Vector();
    String filename = null;
    Vector dataVec = null;

    if (directory == null)
      return retval;

    //  System.out.println("Dir =" + directory);
    //  System.out.println("Extension" + extension);

    // Open the directory
    java.io.File fp = new File(directory);

    // directory does not exist
    if (!fp.exists())
      return retval;

    if (fp.list() == null)
      return retval;
    
    // change extension to be lowercase
    if (extension != null)
    	extension = extension.toLowerCase();

    // step through the files and check filter
    int len = fp.list().length;
    for (int i = 0; i < len; i++) {
      filename = fp.list()[i];
      filename = filename.toLowerCase();

      if (extension == null || filename.toLowerCase().endsWith(extension)) {
        dataVec = new Vector(2);
        dataVec.addElement(filename);
        dataVec.addElement(filename);
        retval.addElement(dataVec);
      }
    }
    return retval;
  }

  public String getFileExtension(String filepath) {
    String filename = null;
    String extension = "";

    if (filepath != null && filepath != "") {
      filename = getLeaf(filepath);

      String leaf = "";
      if (filename.indexOf('.') != -1) {
        for (int i = filename.length() - 1; i > 0; i--) {
          if (filename.charAt(i) == '.') {
            break;
          }

          extension = filename.charAt(i) + extension;
        }
        return extension;
      }
    }
    return null;
  }

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
  
  public boolean fileExists(String filepath) {

	    java.io.File fp = new java.io.File(filepath);
	   
	    return fp.exists();
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
  public void mkdir(String path) {
	    java.io.File fp = new java.io.File(path);

	    if (fp == null)
	      return;
	    if (!fp.exists())
	      fp.mkdirs();

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
		        	dataVec = __utils.stringToVectorTab(line);
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

}
