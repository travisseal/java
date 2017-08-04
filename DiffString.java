package com.remotes.utils;




public class DiffString extends Diff
{
 int i = 0;
 int j = 0;
 String[] left;
 String[] right;

 public DiffString(String[] left, String[] right)
 {
  super();
  i = 0;
  j = 0;
  this.left = left;
  this.right = right;
 }

 public Object getLeft ()
 {
  if(i < (left.length))
   return((Object)left[i++]);
  else
   return(null);
 }

 public Object getRight ()
 {
  if(j < (right.length))
   return((Object)right[j++]);
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
  String leftStr = (String)leftObj;
  String rightStr = (String)rightObj;
  return(leftStr.equals(rightStr));
 }
}

