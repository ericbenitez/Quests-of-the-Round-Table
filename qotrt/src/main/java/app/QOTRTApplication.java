package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import Models.*;

@SpringBootApplication
@ComponentScan({"Models.TestClass"})
public class QOTRTApplication {

  @Autowired
  TestClass test;

  public static void main(String[] args) {
    QOTRTApplication application = new QOTRTApplication();

    SpringApplication.run(QOTRTApplication.class, args);
    SpringApplication.run(Models.TestClass.class, args);

    System.out.println("running");
    String x = "";
    System.out.println(application.test.value);

  }
}
