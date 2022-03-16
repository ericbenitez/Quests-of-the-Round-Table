package app.Controllers;

import app.Controllers.dto.CreateGameRequest;
<<<<<<< Updated upstream
import app.Controllers.dto.Message;
import app.Models.AdventureCards.AdventureCard;
=======
import app.Models.AdventureCards.AdventureCard;
import app.Models.General.Card;
import app.Models.General.Game;
>>>>>>> Stashed changes
import app.Models.General.Player;
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

  // trying
  @MessageMapping("/playerJoining")
  @SendTo("/topic/joinGame")
<<<<<<< Updated upstream
  public ResponseEntity<Integer> joinGame(@RequestBody String anotherPlayer, String game) throws Exception {
    // when a player joins the game, send them their unique id (player number)

      String gameID = gameService.joinGame(new Player(anotherPlayer), game);

    int playerNum = gameService.getCurrPlayerNum();
    return ResponseEntity.ok(playerNum);
  }

  @MessageMapping("/playerGetCards")
  public void getCards(@RequestBody Message playerNum){
    System.out.println("Player number in get cards: " + playerNum.getMessage());
    ArrayList<String> playersCards = gameService.getPlayerCards(playerNum.getMessage());

    simpMessagingTemplate.convertAndSend("/topic/current-cards/" + playerNum.getMessage(), playersCards);
=======
  public ResponseEntity<String> joinGame(@RequestBody String anotherPlayer, String game) throws Exception {
    return ResponseEntity.ok(gameService.joinGame(new Player(anotherPlayer), game));
>>>>>>> Stashed changes
  }
  @MessageMapping("/getAdvCard")
  @SendTo("/topic/getAdvCard")
  public AdventureCard getAdvCard() {
    return gameService.getAdventureCard();
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


}