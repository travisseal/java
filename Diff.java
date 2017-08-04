package com.remotes.utils;


import java.util.*;

public abstract class Diff
{
  /**
   * When returned by more, the Objects on left and right are different.
   */
  final public static int CHG = 0;
  /**
   * When returned by more, the Objects on the left are not present on the right.
   */
  final public static int DEL = 1;
  /**
   * When returned by more, the Objects on the right are not present on the left.
   */
  final public static int INS = 2;
  /**
   * There are no more differences.
   */
  final public static int DONE = 3;
  final static int MATCH = 4;

  /**
   * These Vectors will contain any differences that are reported to the user.
   *
   * For CHG, both leftDiff and rightDiff will be populated.
   * For DEL, only leftDiff contains Objects.
   * For INS, only rightDiff contains Objects.
   * For DONE, both leftDiff and rightDiff should be empty.
   */
  public Vector leftDiff, rightDiff;

  /** Constructs a difference object for determining how two groups of similar
    objects differ.
   *
   */
  public Diff() {
    // We have been asked to create a difference object for these two objects.
    // These vectors will contain any differences that are reported to the user.
    leftDiff = new Vector();
    rightDiff = new Vector();
  }

  /**
   * Returns the next set of differences between two objects.
   *
   */
  public int more() {
    // We clear these Vectors because we are assuming that all differences were
    // already grabbed and used on the previous call.
    leftDiff.clear();
    rightDiff.clear();
    int result = MATCH;
    while (result == MATCH) {
      // Pick up the next object available on the left side.
      Object leftObj = getLeft();
      if (leftObj != null) {
        // We haven't run out of objects yet on the left.
        // Get one on the right side.
        Object rightObj = getRight();
        if (rightObj != null) {
          // We haven't run out of objects yet on the right.
          if (compare(leftObj, rightObj))
            result = MATCH;
          else {
            // We may have some lines inserted on the right; let's try to scroll
            // through the right until we find another match.  In case we have
            // to back up, we count the number of right objects we get.
            rightDiff.add(rightObj);
            int scrolled = 1;
            boolean foundMatchAgain = false;
            do {
              rightObj = getRight();
              if (rightObj != null) {
                scrolled++;
                // Add it to the differences list.
                rightDiff.add(rightObj);
                if (compare(leftObj, rightObj))
                  foundMatchAgain = true;
              }
            }
            while (!foundMatchAgain && (rightObj != null));
            // Now figure out why we stopped getting right objects.
            if (rightObj == null) {
              // We ran out of right objects -- this means these were all
              // changed objects.  Back up scrolled right objects and resume
              // getting left objects, seeing if we need to scroll them.
              while (scrolled-- > 0) {
                ungetRight();
                rightDiff.remove(rightDiff.size() - 1);
              }
              rightObj = getRight();
              // It's possible that an object was inserted on the left.
              // Let's see if we can scroll forward on the left to get a
              // match to the right object.
              leftDiff.add(leftObj);
              scrolled = 1;
              foundMatchAgain = false;
              do {
                leftObj = getLeft();
                if (leftObj != null) {
                  scrolled++;
                  // Add it to the differences list.
                  leftDiff.add(leftObj);
                  if (compare(leftObj, rightObj))
                    foundMatchAgain = true;
                }
              }
              while (!foundMatchAgain && (leftObj != null));
              // Now figure out why we stopped getting left objects.
              if (leftObj == null) {
                while (scrolled-- > 0) {
                  ungetLeft();
                  leftDiff.remove(leftDiff.size() - 1);
                }
                // So the next go around will have something in leftObj.
                leftObj = getLeft();
                // Of course, if scrolling forward on both left and right
                // objects didn't get us a match again, it means that we
                // need to tag this difference as a change, not an insertion.
                leftDiff.add(leftObj);
                rightDiff.add(rightObj);
                // We have already established that there is no match by
                // scrolling forward, so let's continue adding to the diff
                // lists until we get a match again.
                foundMatchAgain = false;
                while (!foundMatchAgain) {
                  leftObj = getLeft();
                  rightObj = getRight();
                  if (compare(leftObj, rightObj)) {
                    foundMatchAgain = true;
                    ungetLeft();
                    ungetRight();
                  }
                  else {
                    leftDiff.add(leftObj);
                    rightDiff.add(rightObj);
                  }
                }
                result = CHG;
              }
              else {
                // We found a match again.  This means we are back in
                // sync between left and right objects, but we need to
                // inform the consumer of the new objects on the left side.
                ungetLeft();
                leftDiff.remove(leftDiff.size() - 1);
                ungetRight();
                result = DEL;
              }
            }
            else {
              // We found a match again.  This means that we are back in
              // sync between left and right objects, but we need to inform
              // the consumer of the new objects inserted on the right side.
              ungetRight();
              rightDiff.remove(rightDiff.size() - 1);
              ungetLeft();
              result = INS;
            }
          }
        }
        else {
          // Okay, nothing on the right, so this is a left insertion.
          leftDiff.add(leftObj);
          result = DEL;
        }
      }
      else {
        // Okay nothing, on the left.  Is there anything left on the right?
        Object rightObj = getRight();
        if (rightObj != null) {
          rightDiff.add(rightObj);
          // Okay, this is a right insertion.
          result = INS;
        }
        else
          result = DONE;
      }
    }
    return (result);
  }
  // get/unget the next left object
  /**
  * The subclass must implement this method to return the next available left Object.
  *
  * return the next left side Object to compare
  */
  public abstract Object getLeft ();

  /**
  * The subclass must implement this method to unget the current left Object.
  *
  */
  public abstract void ungetLeft ();

  /**
  * The subclass must implement this method to return the next available right Object.
  *
  * return the next right side Object to compare
  */
  public abstract Object getRight ();

  /**
  * The subclass must implement this method to unget the current right Object.
  *
  */
  public abstract void ungetRight ();

  /**
  * The subclass must implement this method to compare the current left Object and
  right Object.
  *
  * return true means left and right Objects are equal; false means unequal
  */
  public abstract boolean compare (Object leftObject, Object rightObject);
 }


