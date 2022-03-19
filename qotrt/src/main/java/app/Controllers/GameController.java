package app.Controllers;

import app.Models.AdventureCards.AdventureCard;
import app.Models.General.Game;
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

@RestController
@RequestMapping("/game")
@Controller

public class GameController {
  GameService gameService = new GameService();
  
  public GameController(SimpMessagingTemplate simpMessagingTemplate) {
    gameService = new GameService();
  }

  // we need an initializing player and the number of player to call createGame()
  // From TTT
  @PostMapping("/start")
  @MessageMapping("/game/start") // server
  @SendTo("/topic/game/started") // client
  public ResponseEntity<String> start(int numPlayers) throws InvalidParameterException, Exception {
    gameService.createGame(numPlayers);
    
    while (this.gameService.getCurrentGame().getPlayers().size() < numPlayers) {
      Thread.sleep(1000);
    }
    
    return ResponseEntity.ok(gameService.createGame(numPlayers).getGameID());
  }

  // After starting, allow other players to connect
  @MessageMapping("/playerJoining")
  @SendTo("/topic/joinGame")
  public ResponseEntity<Integer> joinGame(String playerName) throws Exception {
    Player player = new Player(playerName);
    this.gameService.getCurrentGame().registerPlayer(player);
    return ResponseEntity.ok(player.getId());
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

  @SendTo("/topic/doYouWantToSponsor")
  public ResponseEntity<String> askForSponsor() throws Exception {
    // StoryCard storyCard = this.gameService.getCurrentGame().pickCard();
    // sponsor id
    return ResponseEntity.ok("sponsor id");
  }
}