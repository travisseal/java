package com.remotes.utils;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class RemoteBusyFlag {
  private Thread d_busyflag = null;
  private int d_busycount = 0;

  public synchronized void getBusyFlag() {
    while (tryGetBusyFlag() == false) {
      try {
        wait();
      }
      catch (Exception e) {}
    }
  }

  public synchronized boolean tryGetBusyFlag() {
    if (d_busyflag == null) {
      d_busyflag = Thread.currentThread();
      d_busycount = 1;
      return true;
    }
    if (d_busyflag == Thread.currentThread()) {
      d_busycount++;
      return true;
    }
    return false;
  }

  public synchronized void freeBusyFlag() {
    if (d_busyflag == Thread.currentThread()) {
      d_busycount--;
      if (d_busycount == 0) {
        d_busyflag = null;
        notifyAll();
      }
    }
  }

  public synchronized Thread getBusyFlagOwner() {
    return d_busyflag;
  }

}
