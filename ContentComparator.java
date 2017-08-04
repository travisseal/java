package com.remotes.utils;


import java.io.*;

public class ContentComparator {
  public boolean contentsEqual(InputStream is1, InputStream is2,
                               boolean ignoreWhitespace) throws IOException {
    try {
      if (is1 == is2)
        return true;

      if (is1 == null && is2 == null) // no byte contents
        return true;

      if (is1 == null || is2 == null) // only one has
        // contents
        return false;

      while (true) {
        int c1 = is1.read();
        while (ignoreWhitespace && isWhitespace(c1))
          c1 = is1.read();
        int c2 = is2.read();
        while (ignoreWhitespace && isWhitespace(c2))
          c2 = is2.read();
        if (c1 == -1 && c2 == -1)
          return true;
        if (c1 != c2)
          break;
      }
    }
    catch (FileNotFoundException e) {
      // e.printStackTrace();
    }
    return false;
  }

  private boolean isWhitespace(int c) {
    if (c == -1)
      return false;
    return Character.isWhitespace( (char) c);
  }

  public boolean check(String path1, String path2) {
    InputStream is1 = null;
    InputStream is2 = null;
    try {
      is1 = new FileInputStream(path1);
      is2 = new FileInputStream(path2);
    }
    catch (FileNotFoundException e) {
      // e.printStackTrace();
      return false;
    }

    try {
      ContentComparator cc = new ContentComparator();
      boolean ifIgnoreWhiteSpace = true;
      boolean ifEqual = cc.contentsEqual(is1, is2, ifIgnoreWhiteSpace);
      // System.out.println("Are those two files equal? " + ifEqual);
      return ifEqual;
    }
    catch (IOException e) {
      //e.printStackTrace();
    }
    return false;
  }
}
