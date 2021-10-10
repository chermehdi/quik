package com.chermehdi.quik.bench;

import com.chermehdi.quik.annotations.JsonObject;

@JsonObject
public class Teacher {
  private String name;
  private int age;
  private String address;
  private String email;
  private String occupation;
  private double salary;

  public Teacher(String name, int age, String address, String email, String occupation,
      double salary) {
    this.name = name;
    this.age = age;
    this.address = address;
    this.email = email;
    this.occupation = occupation;
    this.salary = salary;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public String getAddress() {
    return address;
  }

  public String getEmail() {
    return email;
  }

  public double getSalary() {
    return salary;
  }

  public String getOccupation() {
    return occupation;
  }
}
