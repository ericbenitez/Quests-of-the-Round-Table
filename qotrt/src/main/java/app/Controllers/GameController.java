package app.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.Controllers.dto.CardsMessage;
import app.Controllers.dto.Message;
import app.Controllers.dto.ShieldMessage;
import app.Models.AdventureCards.AdventureCard;
import app.Models.General.ProgressStatus;
import app.Models.StoryCards.Quest;
import app.Models.StoryCards.StoryCard;
import app.Service.GameService;

@RestController
@RequestMapping("/game")
@Controller

public class GameController {
  private GameService gameService;

  @Autowired
  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  // we need an initializing player and the number of player to call createGame()
  // From TTT
  @PostMapping("/start")
  @MessageMapping("/game/start") // server
  @SendTo("/topic/game/started") // client
  public ResponseEntity<String> start(int numPlayers) throws Exception {
    gameService.createGame(numPlayers);

    // wait for players
    while (this.gameService.getCurrentGame().getPlayers().size() < numPlayers) {
      Thread.sleep(1000);
    }

    this.gameService.nextStep();

    return ResponseEntity.ok(gameService.createGame(numPlayers).getGameID());
  }

  // After starting, allow other players to connect
  @MessageMapping("/playerJoining")
  @SendToUser("/topic/joinGame")
  public ResponseEntity<Integer> joinGame(String playerName) throws Exception {
    return ResponseEntity.ok(this.gameService.joinGame(playerName));
  }

  @MessageMapping("/getAdvCard")
  @SendToUser("/topic/getAdvCard")
  public AdventureCard getAdvCard() {
    if (this.gameService.getCurrentGame().getProgressStatus() != ProgressStatus.IN_PROGRESS)
      return null;
    return gameService.getAdventureCard();
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
  @SendToUser("/topic/pickCard")
  public ResponseEntity<StoryCard> pickCard() throws Exception {
    if (this.gameService.getCurrentGame().getProgressStatus() != ProgressStatus.IN_PROGRESS)
      return null;

    StoryCard storyCard = this.gameService.getCurrentGame().pickCard();
    return ResponseEntity.ok(storyCard);
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