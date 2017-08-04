package com.remotes.utils;


import java.util.*;


public class DiffVector extends Diff
{
 int i = 0;
 int j = 0;
 Vector left;
 Vector right;

 public DiffVector(Vector left, Vector right)
 {
  super();
  i = 0;
  j = 0;
  this.left = left;
  this.right = right;
 }

 public Object getLeft ()
 {
   i++;
  if(i < (left.size()))
   return(new Vector((Vector)left.elementAt(i)));
  else
   return(null);
 }

 public Object getRight ()
 {
  j++;
  if(j < (right.size()))
   return(new Vector((Vector)right.elementAt(j)));
  else
   return(null);
 }

 public void ungetLeft ()
 {
  if(i > 0)
   i--;
 }

 public void ungetRight ()
 {
  if(j > 0)
   j--;
 }

 public boolean compare (Object leftObj, Object rightObj)
 {
  if((leftObj == null) && (rightObj == null))
   return(true);
  if((leftObj == null) || (rightObj == null))
   return(false);

  Vector leftVec = (Vector)leftObj;
  Vector rightVec = (Vector)rightObj;

//  System.out.println(" leftVec=" + leftVec);
//  System.out.println("rightVec=" + rightVec);


  if (leftVec.size() != rightVec.size())
    return(false);

   // Since we are comparing vectors of strings, step through them
   String leftStr = null;
   String rightStr = null;

   for(int x=0;x<leftVec.size();x++) {
     leftStr = (String)leftVec.elementAt(x);
     rightStr = (String)rightVec.elementAt(x);

     if (!leftStr.equals(rightStr))
       return(false);
   }
   return(true);
 }

}

