package com.chermehdi.quik.examples;

import com.chermehdi.quik.annotations.JsonObject;
import java.io.OutputStream;

@JsonObject
public record Student(String name, int age) {}

