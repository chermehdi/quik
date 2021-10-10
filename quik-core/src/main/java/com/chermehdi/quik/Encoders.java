package com.chermehdi.quik;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

// TODO(chermehdi)
public class Encoders {

  public static void write(int value, OutputStream out) {
    try {
      out.write(Integer.toString(value).getBytes());
    }catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void write(short value, OutputStream out) {
    write(String.valueOf(value), out);
  }

  public static void write(char value, OutputStream out) {
    try {
      out.write(Character.toString(value).getBytes());
    }catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void write(long value, OutputStream out) {
    write(String.valueOf(value), out);
  }

  public static void write(float value, OutputStream out) {
    write(String.valueOf(value), out);
  }

  public static void write(double value, OutputStream out) {
    write(String.valueOf(value), out);
  }

  public static void write(String value, OutputStream out) {
    try {
      out.write(new byte[]{'"'});
      out.write(value.getBytes(StandardCharsets.UTF_8));
      out.write(new byte[]{'"'});
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void write(byte[] value, OutputStream out) {
    try {
      out.write(value);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
