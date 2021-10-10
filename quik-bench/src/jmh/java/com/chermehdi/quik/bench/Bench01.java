package com.chermehdi.quik.bench;

import com.chermehdi.quik.examples.TeacherEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@Warmup(iterations = 1, time = 2)
@Measurement(iterations = 3, time = 5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Bench01 {

  @Benchmark
  @BenchmarkMode(Mode.All)
  @Fork(value = 1, warmups = 1)
  public void jackson(Blackhole bh) throws Exception {
    var mapper = new ObjectMapper();
    var teacher = new Teacher("mehdi", 27, "221B baker street", "me@chermehdi.com", "engineer(ish)",
        1e9);
    var result = mapper.writeValueAsString(teacher);
    bh.consume(result);
  }

  @Benchmark
  @BenchmarkMode(Mode.All)
  @Fork(value = 1, warmups = 1)
  public void quik(Blackhole bh) throws Exception {
    var mapper = new ObjectMapper();
    var be = new ByteArrayOutputStream();
    var teacher = new Teacher("mehdi", 27, "221B baker street", "me@chermehdi.com", "engineer(ish)",
        1e9);
    TeacherEncoder.encode(teacher, be);
    bh.consume(be.toString());
  }
}
