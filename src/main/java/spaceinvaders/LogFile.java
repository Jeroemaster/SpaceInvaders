package spaceinvaders;

import java.io.*;

public class LogFile {

  private static volatile LogFile uniqueInstance;

  private BufferedWriter outbuf;

  private LogFile() {
  }

  /**
   * method to get the instance of logfile.
   * 
   * @return unique LogFile
   */
  public static synchronized LogFile getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new LogFile();
    }
    return uniqueInstance;
  }

  /**
   * method to open logfile.
   * 
   * @return success of opening logfile
   */
  public boolean open() {
    try {
      File outfile = new File("logfile.txt");

      if (!outfile.exists()) {
        outfile.createNewFile();
      }

      FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
      outbuf = new BufferedWriter(fw);

      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * method to close logfile.
   * 
   * @return success of closing logfile
   */
  public boolean close() {
    try {
      outbuf.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * method to write string to logfile.
   * 
   * @param message
   *          string to write
   * @return success of writing to logfile
   */
  public boolean writeString(String message) {
    try {
      outbuf.write(message + ".\n");
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * method to write created object to logfile.
   * 
   * @param object
   *          which is created
   * @param xco
   *          position of the object
   * @param yco
   *          position of the object
   * @return success of writing to logfile
   */
  public boolean writeCreate(String object, double xco, double yco) {
    String out = object + " created at x=" + String.valueOf(xco) + ", y="
        + String.valueOf(yco);
    if (writeString(out) == true) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * method to write object which shot to logfile.
   * 
   * @param object
   *          which shot
   * @param xco
   *          position of the object
   * @param yco
   *          position of the object
   * @return success of writing to logfile
   */
  public boolean writeShoot(String object, double xco, double yco) {
    String out = object + " shot a bullet from x=" + String.valueOf(xco)
        + ", y=" + String.valueOf(yco);
    if (writeString(out) == true) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * method to write moved object to logfile.
   * 
   * @param object
   *          which has moved
   * @param xco
   *          position of the object
   * @param yco
   *          position of the object
   * @return success of writing to logfile
   */
  public boolean writeMove(String object, double xco, double yco) {
    String out = object + " moved to x=" + String.valueOf(xco) + ", y="
        + String.valueOf(yco);
    if (writeString(out) == true) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * method to write hit object to logfile.
   * 
   * @param object
   *          which is hit
   * @param xco
   *          position of the object
   * @param yco
   *          position of the object
   * @return success of writing to logfile
   */
  public boolean writeHit(String object, double xco, double yco) {
    String out = object + " at position x=" + String.valueOf(xco) + ", y="
        + String.valueOf(yco) + " is hit";
    if (writeString(out) == true) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * method to write object which went offscreen to logfile.
   * 
   * @param object
   *          which went off the screen
   * @param xco
   *          position of the object
   * @return success of writing to logfile
   */
  public boolean writeOffscreen(String object, double xco) {
    String out = object + " bullet went offscreen at x=" + String.valueOf(xco);
    if (writeString(out) == true) {
      return true;
    } else {
      return false;
    }
  }

}
