package app.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorHandlerController implements ErrorController {

  // @RequestMapping("/error")
  // public void handleError() {
  //   System.out.println("an error has occured");
  // }
}
