package com.remotes.utils;


import java.util.*;

public class RemoteNameValueVector
{

  private Vector dataVec;

  public RemoteNameValueVector() {
      dataVec = new Vector();
  }


  public void clear()
  {
    dataVec.removeAllElements();
  }


  public int size()
  {
    return dataVec.size();
  }


  public Vector elementAt(int index)
  {
    if (index < size())
      return (Vector)dataVec.elementAt(index);
    else {
      System.out.println("Error: Over indexed QPaRTS_NameValueVector");
      return null;
    }
  }


  public void add(String name, String value)
  {
    add(name,value,"string");
  }

  public void add(String name, boolean value)
  {
    if (value)
      add(name,"true","boolean");
    else
      add(name,"false","boolean");
  }


  public void add(String name, String value, String type)
  {
    Vector tmpVec = new Vector();
    String nameVec = null;

    //System.out.println(dataVec.size());
    // If the name already exist in the list, remove it.
     int len = dataVec.size();
    for (int i=0; i<len;i++) {
      tmpVec = (Vector)dataVec.elementAt(i);
      nameVec = (String)tmpVec.elementAt(0);
      if (nameVec.equals(name)) {
        dataVec.removeElementAt(i);
        break;
      }
    }

    tmpVec = new Vector();

    tmpVec.addElement(name);
    tmpVec.addElement(value);
    tmpVec.addElement(type);

    dataVec.addElement(tmpVec);
  }


  public Vector getVector()
  {
    return (Vector) dataVec.clone();

  }

  public String getValue(String name)
  {
    Vector tmpVec = new Vector();
    String nameVec = null;
    String value = null;

    // If the name already exist in the list, remove it.
     int len = dataVec.size();
    for (int i=0; i<len;i++) {
      tmpVec = (Vector)dataVec.elementAt(i);
      nameVec = (String)tmpVec.elementAt(0);
      if (nameVec.equals(name)) {
        value=(String)tmpVec.elementAt(1);
        return value;
      }
    }
    return null;
  }


  public int indexOf(String name)
  {

    Vector tmpVec = new Vector();
    String nameVec = null;
    String value = null;

    // If the name already exist in the list, remove it.
    int len = dataVec.size();
    for (int i=0; i<len;i++) {
      tmpVec = (Vector)dataVec.elementAt(i);
      nameVec = (String)tmpVec.elementAt(0);
      if (nameVec.equals(name))
        return i;
    }
    return -1;
  }



  public void remove(String name)
  {
    int index = indexOf(name);

    dataVec.remove(index);
  }


}
