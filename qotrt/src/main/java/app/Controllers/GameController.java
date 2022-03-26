package app.Controllers;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.type.TypeFactory;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import app.Controllers.dto.DoubleArrayMessage;

import app.Controllers.dto.CardsMessage;
import app.Controllers.dto.Message;
import app.Controllers.dto.ShieldMessage;
import app.Models.AdventureCards.AdventureCard;
import app.Models.General.Game;
import app.Models.General.Player;
import app.Models.General.ProgressStatus;
import app.Models.General.Session;
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
    this.gameService.createGame(numPlayers);
    // wait for players
    while (this.gameService.getCurrentGame().getPlayers().size() < numPlayers) {
      Thread.sleep(1000);
    }
    Game game= this.gameService.getCurrentGame();
    System.out.println("the num of players in this game"+this.gameService.getCurrentGame().getNumOfPlayers());
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
    // Progress - edit 1
    //if (this.gameService.getCurrentGame().getProgressStatus() != ProgressStatus.IN_PROGRESS)
    //  return null;
    return gameService.getAdventureCard(playerId);
  }

  @MessageMapping("/giveCards")
  @SendToUser("/queue/giveCards")
  public ArrayList<AdventureCard> giveCards(String playerId) {
    Player player = this.gameService.getCurrentGame().getPlayerById(Integer.parseInt(playerId));
    player.drawCards(12);
    return player.getCards();
  }

  // basically what giveCards used to do before drawing cards was added
  @MessageMapping("/getCards")
  @SendToUser("/queue/getCards")
  public ArrayList<AdventureCard> getCards(String playerId) {
    Player player = this.gameService.getCurrentGame().getPlayerById(Integer.parseInt(playerId));
    return player.getCards();
  }

  @MessageMapping("/ready")
  @SendTo("/topic/startTurn")
  public int ready() {
    if(this.gameService.getCurrentGame().getPlayers().size() == this.gameService.getCurrentGame().getNumOfPlayers()){
      return this.gameService.getCurrentActivePlayer();}
    return 0;
  }


  @MessageMapping("/sponsorQuest")
  @SendTo("/topic/sponsorQuest")
  public Quest sponsorQuest() {// name of the quest
   
    Player p = gameService.getCurrentGame().getPlayerById(gameService.getCurrentActivePlayer());
    gameService.getCurrentGame().getCurrentQuest().setSponsor(p.getId());
    //setting the boolean to be true;
    gameService.setQuestInPlay(true);
    return gameService.getCurrentGame().getCurrentQuest();
  }
  

  @MessageMapping("/pickCard")
  @SendTo("/topic/pickCard")
  public StoryCard pickCard(){
    // System.out.println("this is the player Id for pick Card" +playerId);
    StoryCard storyCard = this.gameService.getCurrentGame().pickCard();
    this.gameService.setCurrentStoryCard(storyCard);
    return storyCard;
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

  @MessageMapping("/finishTurn")
  @SendTo("/topic/finishTurn")
  public Session finishTurn() {
    
    Session currSession = new Session();
    currSession.currentActivePlayer = gameService.startNextPlayer(); ///increments the player
    //if we round back to the sponsor, the stage goes up
    if(gameService.getQuestInPlay() && gameService.getCurrentActivePlayer()==gameService.getCurrentGame().getCurrentQuest().getSponsor()){
      gameService.getCurrentGame().getCurrentQuest().incrementCurrentStage();
    }
    currSession.currentStoryCard = gameService.getCurrentStoryCard(); //returns all the elments of that storyCard
    currSession.questInPlay = gameService.getQuestInPlay(); //bool
    // currSession.sponsorId = gameService.getCurrentGame().getCurrentQuest().getSponsor(); //id of the sponsor
    // currSession.participantsId = gameService.getCurrentGame().getCurrentQuest().getParticipantsId();//id of the sponsor
   
    return currSession;
  }


  
  // [[stage 1 cards], [stage 2 cards]] .. ["sfs","grgw","rger"]
  @MessageMapping("/setStages")  //recall this is coming from the sponsor.
  @SendTo("/topic/setStages") // String [] clientStages
  public Quest setStages(@RequestBody DoubleArrayMessage sponsorStages) {  
    ArrayList<ArrayList<String>> arr = sponsorStages.getCards();
    System.out.println(arr);
    gameService.getCurrentGame().getCurrentQuest().setSponsorStages(arr);
    return gameService.getCurrentGame().getCurrentQuest();
  }


  // ------------- Tournaments ---------------

  @MessageMapping("/addParticipantTournament")
  public void addParticipantTournament(@RequestBody Message playerId){
  
    int playerIdInt = Integer.parseInt(playerId.getMessage());
    this.gameService.getCurrentGame().getCurrentTournament().addParticipant(playerIdInt);
  }
}