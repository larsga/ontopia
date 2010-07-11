
// $Id: FakeBodyContent.java,v 1.9 2005/09/08 10:00:53 ian Exp $

package net.ontopia.utils.ontojsp;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * INTERNAL: Fake body content for use with the ontopia fake jsp
 * environment.
 */
public class FakeBodyContent extends BodyContent {

  private char[] cb;
  protected int bufferSize = 8*1024;
  private int nextChar;
  static String lineSeparator = System.getProperty("line.separator");

  public FakeBodyContent(JspWriter writer) {
    super(writer);
    cb = new char[bufferSize];
    nextChar = 0;
  }

  public void write(int c) throws IOException {
    synchronized (lock) {
      if (nextChar >= bufferSize) {
        reAllocBuff (0);
      }
      cb[nextChar++] = (char) c;
    }
  }

  private void reAllocBuff (int len) {
    char[] tmp = null;
    if (len <= bufferSize){
      bufferSize *= 2;
    } else {
      bufferSize += len;
    }
    tmp = new char[bufferSize];
    System.arraycopy(cb, 0, tmp, 0, cb.length);
    cb = tmp;
  }

  public void write(char cbuf[], int off, int len) throws IOException {
    synchronized (lock) {
      if ((off < 0) || (off > cbuf.length) || (len < 0) ||
          ((off + len) > cbuf.length) || ((off + len) < 0)) {
        throw new IndexOutOfBoundsException();
      } else if (len == 0) {
        return;
      } 
      if (len >= bufferSize - nextChar) reAllocBuff (len);
      System.arraycopy(cbuf, off, cb, nextChar, len);
      nextChar+=len;
    }
  }
  
  public void write(char buf[]) throws IOException {
    write(buf, 0, buf.length);
  }

  public void write(String s, int off, int len) throws IOException {
    synchronized (lock) {
      if (len >= bufferSize - nextChar) reAllocBuff(len);    
      s.getChars(off, off + len, cb, nextChar);
      nextChar += len;
    }
  }

  public void newLine() throws IOException {
    synchronized (lock) {
      write(lineSeparator);
    }
  }

  public void print(boolean b) throws IOException {
    write(b ? "true" : "false");
  }

  public void print(char c) throws IOException {
    write(String.valueOf(c));
  }

  public void print(int i) throws IOException {
    write(String.valueOf(i));
  }
 
  public void print(long l) throws IOException {
    write(String.valueOf(l));
  }

  public void print(float f) throws IOException {
    write(String.valueOf(f));
  }

  public void print(double d) throws IOException {
    write(String.valueOf(d));
  }

  public void print(char s[]) throws IOException {
    write(s);
  }

  public void print(String s) throws IOException {
    if (s == null) s = "null";
    write(s);
  }

  public void print(Object obj) throws IOException {
    write(String.valueOf(obj));
  }

  public void println() throws IOException {
    newLine();
  }

  public void println(boolean x) throws IOException {
    synchronized (lock) {
      print(x);
      println();
    }
  }

  public void println(char x) throws IOException {
    synchronized (lock) {
      print(x);
      println();
    }
  }

  public void println(int x) throws IOException {
    synchronized (lock) {
      print(x);
      println();
    }
  }

  public void println(long x) throws IOException {
    synchronized (lock) {
      print(x);
      println();
    }
  }

  public void println(float x) throws IOException {
    synchronized (lock) {
      print(x);
      println();
    }
  }
  
  public void println(double x) throws IOException{
    synchronized (lock) {
      print(x);
      println();
    }
  }
  
  public void println(char x[]) throws IOException {
    synchronized (lock) {
      print(x);
      println();
    }
  }
  
  public void println(String x) throws IOException {
    synchronized (lock) {
      print(x);
      println();
    }
  }

  public void println(Object x) throws IOException {
    synchronized (lock) {
      print(x);
      println();
    }
  }

  public void clear() throws IOException {
    synchronized (lock) {
      nextChar = 0;
    }
  }

  public void clearBuffer() throws IOException {
    this.clear();
  }

  public void close() throws IOException {
    synchronized (lock) {
      cb = null;        
    }
  }

  public int getRemaining() {
    return bufferSize - nextChar;
  }

  public Reader getReader() {
    return new CharArrayReader (cb, 0, nextChar);
  }

  public String getString() {
    return new String(cb, 0, nextChar);
  }

  public void writeOut(Writer out) throws IOException {
    out.write(cb, 0, nextChar);
  }

}
