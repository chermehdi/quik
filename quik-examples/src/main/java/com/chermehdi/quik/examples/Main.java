package com.chermehdi.quik.examples;
import com.chermehdi.quik.annotations.JsonObject;

public class Main {

  public static void main(String[] args) {
    var student = new Student("hello", 12);
    byte[] value = StudentEncoder.encode(student);
    System.out.println("The value is " +new String(value));
  }
}
