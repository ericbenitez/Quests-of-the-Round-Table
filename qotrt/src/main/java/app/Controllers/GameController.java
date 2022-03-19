package app.Controllers;

import app.Models.AdventureCards.AdventureCard;
import app.Models.General.Game;
import app.Models.General.Player;
import app.Models.StoryCards.Quest;
import app.Models.StoryCards.StoryCard;
import app.Service.GameService;
import app.Controllers.dto.CardsMessage;
import app.Controllers.dto.Message;
import app.Controllers.dto.ShieldMessage;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    System.out.println("game starting");
    
    while (this.gameService.getCurrentGame().getPlayers().size() < numPlayers) {
      Thread.sleep(1000);
    }
    
    return ResponseEntity.ok(gameService.createGame(numPlayers).getGameID());
  }

  // After starting, allow other players to connect
  @MessageMapping("/playerJoining")
  @SendTo("/topic/joinGame")
  public ResponseEntity<Integer> joinGame(String playerName) throws Exception {
    System.out.println("player joining");

    Player player = new Player(playerName);
    this.gameService.getCurrentGame().registerPlayer(player);
    return ResponseEntity.ok(player.getId());
  }

  @MessageMapping("/getAdvCard")
  @SendToUser("/topic/getAdvCard")
  public AdventureCard getAdvCard(int playerId) {
    return gameService.getAdventureCard(playerId);
  }

  @MessageMapping("/sponsorQuest")
  @SendTo("/topic/sponsorQuest")
  public Quest sponsorQuest(String currentQuest){
    System.out.println(currentQuest);
    gameService.settingSponsor(0);
    gameService.getCurrentGame().setCurrentQuest(currentQuest);
    System.out.println(gameService.getCurrentGame().getCurrentQuest());
    return gameService.getCurrentGame().getCurrentQuest();
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


  // --------------- Player Participating Stuff ------------------
  
  @MessageMapping("/getRankPts")
  @SendTo("/topic/getRankPts")
  public ResponseEntity<Integer> getPlayerRankBattlePts(@RequestBody Message playerId) throws Exception {
    String id = playerId.getMessage();
    int pts = gameService.getPlayerRankBattlePts(playerId.getMessage());
    return ResponseEntity.ok(pts);
  }

  @MessageMapping("updateShields")
  public void updateShields(@RequestBody ShieldMessage shieldInfo) throws Exception {
    gameService.updateShields(shieldInfo.getPlayerId(), shieldInfo.getShields());
  }


  @MessageMapping("discardCards")
  public void discardCards(@RequestBody CardsMessage cards) throws Exception {
    gameService.discardCards(cards.getPlayerId(), cards.getCards());
  }

  @MessageMapping("joinQuest")
  public void joinQuest(@RequestBody Message playerId){
    gameService.joinQuest(playerId.getMessage());
  }

  @MessageMapping("withdrawQuest")
  public void withdrawQuest(@RequestBody Message playerId){
    gameService.withdrawQuest(playerId.getMessage());
  }

}