package com.chermehdi.quik.examples;

import com.chermehdi.quik.annotations.JsonObject;
import java.io.OutputStream;

@JsonObject
public class Student {
  private String name;
  private int age;

  public Student() {}

  public Student(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAge(int age) {
    this.age = age;
  }
}

