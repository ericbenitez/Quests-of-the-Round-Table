package app.Models;

import org.springframework.stereotype.Component;

@Component
public class TestClass {
  public String value;
  
  TestClass() {
    this.value = "initialized";
  }
}

