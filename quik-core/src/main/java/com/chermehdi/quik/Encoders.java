package com.chermehdi.quik;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

// TODO(chermehdi)
public class Encoders {

  public static void write(int value, ByteBuffer buffer) {
    buffer.putInt(value);
  }

  public static void write(short value, ByteBuffer buffer) {
    buffer.putShort(value);
  }

  public static void write(char value, ByteBuffer buffer) {
    buffer.putChar(value);
  }

  public static void write(long value, ByteBuffer buffer) {
    buffer.putLong(value);
  }

  public static void write(float value, ByteBuffer buffer) {
    buffer.putFloat(value);
  }

  public static void write(double value, ByteBuffer buffer) {
    buffer.putDouble(value);
  }

  public static void write(String value, ByteBuffer buffer) {
    write('\"', buffer);
    buffer.put(value.getBytes(StandardCharsets.UTF_8));
    write('\"', buffer);
  }

  public static void write(byte[] value, ByteBuffer buffer) {
    buffer.put(value);
  }
}
