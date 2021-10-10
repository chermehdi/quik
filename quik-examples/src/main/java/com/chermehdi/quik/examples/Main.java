package com.chermehdi.quik.examples;
import com.chermehdi.quik.annotations.JsonObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;

public class Main {

  public static void main(String[] args) throws Exception {
    var student = new Student("hello", 12);
    var s = new ByteArrayOutputStream();
    var om = new ObjectMapper();
    var res = om.writeValueAsString(student);
    System.out.println(res);
    StudentEncoder.encode(student, s);
    System.out.println("Value si " + s.toString());
  }
}
