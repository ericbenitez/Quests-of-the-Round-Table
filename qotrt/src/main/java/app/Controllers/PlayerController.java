package app.Controllers;

import app.Service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
public class PlayerController {
    @Autowired
    GameService gameService;


}
