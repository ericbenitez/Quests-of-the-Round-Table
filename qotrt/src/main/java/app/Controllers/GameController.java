package app.Controllers;

import app.Controllers.dto.CreateGameRequest;
import app.Models.General.Game;
import app.Models.General.Player;
import app.Service.GameService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import java.io.IOError;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

@RestController
@RequestMapping("/game")
@Controller
public class GameController {
  GameService gameService = new GameService();

  // we need an initializing player and the number of player to call createGame()
  // From TTT
  @PostMapping("/start")
  @MessageMapping("/game/start") // server
  @SendTo("/topic/game/started") // client
  public ResponseEntity<String> start(@RequestBody CreateGameRequest info) throws InvalidParameterException, Exception {
    // From T.T.T dude:
    System.out.println(info.getPlayerName());
    System.out.println(info.getNumOfPlayers());
    Thread.sleep(1000);

    int num = info.getNumOfPlayers();
    return ResponseEntity.ok(gameService.createGame(new Player(info.getPlayerName()), info.getNumOfPlayers()));
    // return "hello " + request.getNumOfPlayers() + request.getPlayerName();
  }

  // After starting, allow other players to connect

  // trying
  @MessageMapping("/playerJoining")
  @SendTo("/topic/joinGame")
  public ResponseEntity<String> joinGame(@RequestBody String anotherPlayer, String game) throws Exception {

    return ResponseEntity.ok(gameService.joinGame(new Player(anotherPlayer), game));
  }

  // Game Play from T.T.T
  // @PostMapping(value="/gameplay")
  // public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) {
  // //TODO: process POST request
  // Game game =
  // return entity;
  // }

  @MessageMapping("/test")
  @RequestMapping("/test")
  public void test() {
    System.out.println("test worked");
  }

  // Game Play from T.T.T
  // @PostMapping(value="/gameplay")
  // public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) {
  // //TODO: process POST request
  // Game game =
  // return entity;
  // }
}