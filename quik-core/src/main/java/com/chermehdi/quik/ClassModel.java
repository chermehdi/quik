package com.chermehdi.quik;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.lang.model.type.TypeMirror;

public class ClassModel {

  private String name;
  private String fullyQualifiedName;
  private String packageName;
  private List<MethodModel> getters;
  private List<MethodModel> setters;
  private TypeMirror type;

  ClassModel() {
    this.getters = new ArrayList<>();
    this.setters = new ArrayList<>();
  }

  public static ClassModelBuilder newBuilder() {
    return new ClassModelBuilder();
  }

  public String getName() {
    return name;
  }

  public String getFullyQualifiedName() {
    return fullyQualifiedName;
  }

  public List<MethodModel> getGetters() {
    return getters;
  }

  public List<MethodModel> getSetters() {
    return setters;
  }

  public TypeMirror getType() {
    return type;
  }

  public String getPackageName() {
    return packageName;
  }

  public static class ClassModelBuilder {

    private ClassModel model;

    ClassModelBuilder() {
      this.model = new ClassModel();
    }

    public ClassModelBuilder name(String name) {
      this.model.name = Objects.requireNonNull(name);
      return this;
    }

    public ClassModelBuilder fullyQualifiedName(String name) {
      this.model.fullyQualifiedName = Objects.requireNonNull(name);
      return this;
    }

    public ClassModelBuilder addSetter(MethodModel setter) {
      this.model.setters.add(setter);
      return this;
    }

    public ClassModelBuilder addGetter(MethodModel getter) {
      this.model.getters.add(getter);
      return this;
    }

    public ClassModelBuilder type(TypeMirror type) {
      this.model.type = type;
      return this;
    }

    public ClassModelBuilder packageName(String packageName) {
      this.model.packageName = packageName;
      return this;
    }

    public ClassModel build() {
      Objects.requireNonNull(model.name);
      return model;
    }
  }

  @Override
  public String toString() {
    return "ClassModel{" +
        "name='" + name + '\'' +
        ", fullyQualifiedName='" + fullyQualifiedName + '\'' +
        ", getters=" + getters +
        ", setters=" + setters +
        '}';
  }
}
