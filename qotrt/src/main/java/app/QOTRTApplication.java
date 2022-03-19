package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication()
//@ComponentScan(basePackages = {"app.Models", "app.Models.TestClass", "app.Controllers"})
@EnableAutoConfiguration
public class QOTRTApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(QOTRTApplication.class, args);
    
    // Game game = context.getBean(Game.class);
    // System.out.println(game.getGameID());
    
    // QOTRTApplication app = context.getBean(QOTRTApplication.class);
    // System.out.println(app.test);
  }
}
