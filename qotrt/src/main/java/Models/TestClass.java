package Models;

import org.springframework.stereotype.Service;

@Service
public class TestClass {
  public String value;
  
  TestClass() {
    this.value = "initialized";
  }
}

