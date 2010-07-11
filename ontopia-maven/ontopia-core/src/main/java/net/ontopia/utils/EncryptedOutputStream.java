
// $Id: EncryptedOutputStream.java,v 1.4 2002/06/13 11:11:10 larsga Exp $

package net.ontopia.utils;

import java.io.*;

/**
 * INTERNAL: Output stream for reading in a encrypted input stream (for
 * example from a file) and giving back the decrypted values.
 */
public class EncryptedOutputStream extends OutputStream {

  final static int KEY = 0xFF;
  OutputStream myOutput;
  
  public EncryptedOutputStream(OutputStream myOutputStream) {
    super();
    this.myOutput = myOutputStream;
  }

  public void write(int b) throws IOException {
    int cipher = b ^ KEY;
    myOutput.write(cipher);
  }
  
}
