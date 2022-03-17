package app.Controllers;

import app.Controllers.dto.CreateGameRequest;
import app.Controllers.dto.Message;
import app.Models.AdventureCards.AdventureCard;
import app.Models.General.Player;
import app.Models.StoryCards.StoryCard;
import app.Service.GameService;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import java.security.InvalidParameterException;
import java.util.ArrayList;

@RestController
@RequestMapping("/game")
@Controller

public class GameController {
  GameService gameService = new GameService();
  private final SimpMessagingTemplate simpMessagingTemplate;

  public GameController(SimpMessagingTemplate simpMessagingTemplate) {
    gameService = new GameService();
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

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
  @MessageMapping("/playerJoining")
  @SendTo("/topic/joinGame")
  public ResponseEntity<Integer> joinGame(@RequestBody String anotherPlayer, String game) throws Exception {
    // when a player joins the game, send them their unique id (player number)
    String gameID = gameService.joinGame(new Player(anotherPlayer), game);
    int playerNum = gameService.getCurrPlayerNum();
    return ResponseEntity.ok(playerNum);
  }

  @MessageMapping("/getAdvCard")
  @SendTo("/topic/getAdvCard")
  public AdventureCard getAdvCard() {
    return gameService.getAdventureCard();
  }

  

  @MessageMapping("/pickCard")
  @SendTo("/topic/pickCard")
  public ResponseEntity<String> pickCard() throws Exception {
    StoryCard storyCard = this.gameService.getCurrentGame().pickCard();
    return ResponseEntity.ok(storyCard.name);
  }
}