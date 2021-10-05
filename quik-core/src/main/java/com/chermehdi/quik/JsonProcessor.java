package com.chermehdi.quik;

import com.chermehdi.quik.annotations.JsonObject;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

@SupportedSourceVersion(SourceVersion.RELEASE_16)
public class JsonProcessor extends AbstractProcessor {

  private Filer filer;
  private Types types;
  private Elements elements;
  private Messager messager;

  @Override
  public synchronized void init(ProcessingEnvironment environment) {
    super.init(environment);
    this.filer = environment.getFiler();
    this.elements = environment.getElementUtils();
    this.types = environment.getTypeUtils();
    this.messager = environment.getMessager();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(
        JsonObject.class.getCanonicalName()
    );
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      System.out.println("Processing is over");
      return false;
    }
    Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(
        JsonObject.class);

    for (Element element : elements) {
      processElement((TypeElement) element, roundEnv);
    }
    return false;
  }

  private void processElement(TypeElement element, RoundEnvironment roundEnv) {
    messager.printMessage(Kind.NOTE, "Processing element '%s'".formatted(element.getSimpleName()));
    var builder = ClassModel.newBuilder();
    builder.name(element.getSimpleName().toString());
    builder.fullyQualifiedName(element.getQualifiedName().toString());

    builder.type(element.asType());
    var fields = elements.getAllMembers(element).stream()
        .filter(e -> e.getKind() == ElementKind.FIELD)
        .collect(Collectors.toList());
    var fieldsGetters = fields.stream()
        .map(e -> getGetterForElement(element, e))
        .collect(Collectors.toList());

    for (int i = 0; i < fields.size(); i++) {
      var field = fields.get(i);
      var fieldGetter = fieldsGetters.get(i);
      if (fieldGetter.isEmpty()) {
        messager.printMessage(Kind.ERROR,
            "Could not get getter for field '%s' of type '%s'".formatted(field.getSimpleName(),
                element.getSimpleName()));
        return;
      }
      builder.addGetter(new MethodModel(field.getSimpleName().toString(),
          fieldGetter.get().getSimpleName().toString()));
    }

    var classModel = builder.build();

    generateEncoder(classModel);
  }

  private void generateEncoder(ClassModel model) {
    var encoderBuilder = TypeSpec.classBuilder(model.getName() + "Encoder")
        .addModifiers(Modifier.PUBLIC);
    System.out.println("Class model " + model);

    var encodeMethodBuilder = MethodSpec.methodBuilder("encode")
        .addParameter(ParameterSpec.builder(TypeName.get(model.getType()), "value").build())
        .returns(byte[].class)
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addStatement("var buffer = $T.allocate(1024)", ByteBuffer.class);

    encodeMethodBuilder.addStatement("$T.write('{', buffer)", Encoders.class);
    for (MethodModel getter : model.getGetters()) {
      writeFieldName(encodeMethodBuilder, getter);
      encodeMethodBuilder.addStatement("$T.write(value.$N(), buffer)", Encoders.class, getter.methodName());
    }
    encodeMethodBuilder.addStatement("$T.write('}', buffer)", Encoders.class);


    encodeMethodBuilder.addStatement("return $T.copyOfRange(buffer.array(), 0, buffer.position())",
        Arrays.class);

    var encoder = encoderBuilder.addMethod(encodeMethodBuilder.build()).build();

    var jFile = JavaFile.builder("com.chermehdi.quik.examples", encoder)
        .build();
    try {
      jFile.writeTo(filer);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void writeFieldName(Builder encodeMethodBuilder, MethodModel getter) {
    encodeMethodBuilder.addStatement("$T.write('\"', buffer)", Encoders.class);
    encodeMethodBuilder.addStatement("$T.write($S, buffer)", Encoders.class, getter.fieldName());
    encodeMethodBuilder.addStatement("$T.write('\"', buffer)", Encoders.class);
  }

  private Optional<? extends Element> getGetterForElement(TypeElement type, Element el) {
    var name = el.getSimpleName().toString();
    for (String candidate : getGetterCandidates(name)) {
      Optional<? extends Element> found = elements.getAllMembers(type).stream()
          .filter(e -> e.getKind() == ElementKind.METHOD)
          .filter(e -> e.getSimpleName().toString().equals(candidate))
          .findAny();
      if (found.isPresent()) {
        return found;
      }
    }
    return Optional.empty();
  }

  private String[] getGetterCandidates(String name) {
    return new String[]{
        "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1), // old style pojo
        name, // record object
    };
  }
}
