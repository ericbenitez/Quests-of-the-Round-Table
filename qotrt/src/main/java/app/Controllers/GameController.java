package app.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import app.Controllers.dto.DoubleArrayMessage;

import app.Controllers.dto.CardsMessage;
import app.Controllers.dto.Message;
import app.Controllers.dto.ShieldMessage;
import app.Models.AdventureCards.AdventureCard;
import app.Models.General.Game;
import app.Models.General.Player;
import app.Models.General.ProgressStatus;
import app.Models.General.Round;
import app.Models.StoryCards.Quest;
import app.Models.StoryCards.StoryCard;
import app.Service.GameService;

@RestController
@RequestMapping("/game")
@Controller
public class GameController {
  public GameService gameService;
  SimpMessagingTemplate simpMessage;

  @Autowired
  public GameController(GameService gameService, SimpMessagingTemplate simpMessage) {
    this.gameService = gameService;
    this.simpMessage = simpMessage;
  }

  // we need an initializing player and the number of player to call createGame()
  // From TTT
  @PostMapping("/start")
  @MessageMapping("/game/start") // server
  @SendTo("/topic/game/started") // client
  public ResponseEntity<String> start(int numPlayers) throws Exception {
    Game game = gameService.createGame(numPlayers);

    // wait for players
    while (this.gameService.getCurrentGame().getPlayers().size() < numPlayers) {
      Thread.sleep(1000);
    }

    // this.gameService.nextStep();
    this.gameService.getCurrentGame().startNewRound(this);

    return ResponseEntity.ok(game.getGameID());
  }

  // After starting, allow other players to connect
  @MessageMapping("/playerJoining")
  @SendToUser("/queue/joinGame")
  public ResponseEntity<Integer> joinGame(String playerName) throws Exception {
    return ResponseEntity.ok(this.gameService.joinGame(playerName));
  }

  @MessageMapping("/getAdvCard")
  @SendToUser("/queue/getAdvCard")
  public AdventureCard getAdvCard(String playerId) {
    if (this.gameService.getCurrentGame().getProgressStatus() != ProgressStatus.IN_PROGRESS)
      return null;
    return gameService.getAdventureCard(playerId);
  }

  @MessageMapping("/giveCards")
  @SendToUser("/queue/giveCards")
  public ArrayList<AdventureCard> giveCards(String playerId) {
    Player player = this.gameService.getCurrentGame().getPlayerById(Integer.parseInt(playerId));
    return player.getCards();
  }

  @MessageMapping("/sponsorQuest")
  @SendTo("/topic/sponsorQuest")
  public Quest sponsorQuest(String currentQuest) {// name of the quest
    System.out.println(currentQuest); // sponsorQuest()
    // gameService.getCurrentGame().setCurrentQuest(currentQuest); //setting the
    // quest before the player

    Player player = gameService.getCurrentGame().getPlayers().get(gameService.getCurrentActivePlayer());
    gameService.settingSponsor(player.getId());// it's supose to pass in the currActive player's id
    System.out.println(gameService.getCurrentGame().getCurrentQuest());
    // this.gameService.nextStep();

    Quest quest = gameService.getCurrentGame().getCurrentQuest();
    return quest;
  }

  @MessageMapping("/pickCard")
  @SendTo("/topic/pickCard")
  public ResponseEntity<StoryCard> pickCard() throws Exception {
    // if (this.gameService.getCurrentGame().getProgressStatus() !=
    // ProgressStatus.IN_PROGRESS)
    // return null;

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
  @SendTo("/queue/getRankPts")
  public ResponseEntity<Integer> getPlayerRankBattlePts(@RequestBody Message playerId) throws Exception {
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
  @SendToUser("/queue/joinQuest")
  public String joinQuest(@RequestBody Message playerId) {
    gameService.joinQuest(playerId.getMessage());// add them
    return "Choose stages for next";
  }

  @MessageMapping("withdrawQuest")
  public void withdrawQuest(@RequestBody Message playerId) {
    gameService.withdrawQuest(playerId.getMessage());
  }

  // @SendTo("/topic/startTurn")
  // @MessageMapping("/startTurn")
  public void startTurn(int playerId) {
    this.simpMessage.convertAndSend("/topic/startTurn", playerId);
    // return ResponseEntity.ok(playerId);
  }

  @MessageMapping("/nextStep")
  @SendTo("/topic/nextStep")
  public int nextStep() {
    this.gameService.nextStep();
    this.gameService.startNextPlayer();
    return this.gameService.getCurrentActivePlayer();
  }

  @MessageMapping("/finishTurn")
  @SendTo("/topic/finishTurn")
  public HashMap<String, Object> finishTurn() {
    int index = gameService.startNextPlayer();
    Player player = this.gameService.getCurrentGame().getPlayers().get(index);
    
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("player-id", player.getId());
    hashMap.put("can-draw", this.gameService.canDraw());
    
    return hashMap;
  }

  @MessageMapping("/incrementStage")
  @SendTo("/topic/incrementStage")
  public boolean incrementStage(int currStage) {
    return gameService.incrementStage(currStage);
  }
  
  
  
  // [[stage 1 cards], [stage 2 cards]] .. ["sfs","grgw","rger"]
  @MessageMapping("/setStages")
  @SendTo("/topic/setStages") // String [] clientStages
  public ArrayList<ArrayList<String>> setStages(@RequestBody DoubleArrayMessage clientStages) {
    /*
     * String[] stages = clientStages.split("/");
     * 
     * ArrayList<List<String>> serverStages = new ArrayList<>();
     * 
     * for (String stage: stages) {
     * String newStage = stage
     * String[] stageList = stage.split(",");
     * List<String> convertedStageList = new ArrayList<String>();
     * convertedStageList = Arrays.asList(stageList);
     * 
     * serverStages.add(convertedStageList);
     * }
     * 
     * System.out.println("stages: " + stages.toString());
     * Round round = this.gameService.getCurrentGame().getCurrentRound();
     */
    Round round = this.gameService.getCurrentGame().getCurrentRound();
    // the double array list
    ArrayList<ArrayList<String>> arr = clientStages.getCards();
    System.out.println(arr);
    round.setStages(arr);
    return round.getStageCards();
  }
  
  @MessageMapping("/transferQuest")
  @SendTo("/topic/transferQuest")
  public int transferQuest(int playerId) {
    Round round = this.gameService.getCurrentGame().getCurrentRound();
    if (round.getCannotSponsor() < this.gameService.getCurrentGame().getPlayers().size()) {
      round.increaseCannotSponsor();
    }
    
    int index = gameService.startNextPlayer();
    Player player = this.gameService.getCurrentGame().getPlayers().get(index);
    
    return player.getId();
  }
}