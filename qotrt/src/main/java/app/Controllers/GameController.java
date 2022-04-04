package app.Controllers;

import java.util.ArrayList;
import java.util.HashMap;

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

import app.Controllers.dto.ArrayMessage;
import app.Controllers.dto.CardsMessage;
import app.Controllers.dto.DoubleArrayMessage;
import app.Controllers.dto.Message;
import app.Controllers.dto.ShieldMessage;
import app.Models.AdventureCards.AdventureCard;
import app.Models.AdventureCards.Test;
import app.Models.General.Card;
import app.Models.General.FilteredPlayer;
import app.Models.General.Game;
import app.Models.General.Player;
import app.Models.General.Session;
import app.Models.StoryCards.EventCard;
import app.Models.StoryCards.Quest;
import app.Models.StoryCards.StoryCard;
import app.Models.StoryCards.Tournament;
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
  


  //We will also be returning the session here because we need to ask the currentActive player if they wanna sponsor quest, bid etc.
  @MessageMapping("/pickCard")
  @SendTo("/topic/pickCard")
  public Session pickCard(){
    // System.out.println("this is the player Id for pick Card" +playerId);
    StoryCard storyCard = this.gameService.getCurrentGame().pickCard();
    this.gameService.setCurrentStoryCard(storyCard);
    //maybe a timeout because it returns null as currentStorycard...
    Session currSession = new Session();
    currSession.currentActivePlayer = gameService.getCurrentActivePlayer();
    System.out.println(storyCard.getName());
    currSession.currentStoryCard = gameService.getCurrentStoryCard(); //returns all the elments of that storyCard
    currSession.questInPlay = gameService.getQuestInPlay(); //bool 
    currSession.testInPlay = false;
    currSession.testCard= null;
    return currSession;
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
    finishTurn();//move on to the next player
  }

  @MessageMapping("/finishTurn")
  @SendTo("/topic/finishTurn")
  public Session finishTurn() {
    
    Session currSession = new Session();

    currSession.currentActivePlayer = gameService.startNextPlayer(); ///increments the player
    currSession.currentStoryCard = gameService.getCurrentStoryCard(); //returns all the elments of that storyCard
    currSession.questInPlay = gameService.getQuestInPlay(); //bool, changes this to false when you complete all stages.
    currSession.tournamentInPlay=gameService.getTournamentInPlay(); //bool
    
   
    //if we round back to the sponsor, the stage goes up
    if(gameService.getQuestInPlay() && gameService.getCurrentActivePlayer()==gameService.getCurrentGame().getCurrentQuest().getSponsor()){
      if(!currSession.testInPlay){
        gameService.getCurrentGame().getCurrentQuest().incrementCurrentStage();
      } 
      if(currSession.testInPlay && gameService.getCurrentGame().getCurrentQuest().getParticipantsId().size()==0) {//everyone dropped out with no bids
        gameService.getCurrentGame().getCurrentQuest().incrementCurrentStage();
        currSession.testInPlay = false;
      }
      // withdraw from quest if you dont want to keep bidding in the test
      if(currSession.testInPlay && gameService.getCurrentGame().getCurrentQuest().getParticipantsId().size()==1) { //the winner
        //some function to announce the winner and then takes cards of the test winner (last bid in test.bids is the number of cards we remove from the winner)
        testWinner(gameService.getCurrentGame().getCurrentQuest().getParticipantsId());
        gameService.getCurrentGame().getCurrentQuest().incrementCurrentStage(); 
        currSession.testInPlay = false;

      }
      if(currSession.testInPlay && gameService.getCurrentGame().getCurrentQuest().getParticipantsId().size()>1) { //test keeps going
        currSession.currentActivePlayer = gameService.startNextPlayer(); //skip the sponsor
      } 
    }
    
    if (gameService.getQuestInPlay()) {
      currSession.testInPlay = (gameService.getCurrentGame().getCurrentQuest().getQuestIncludesTest() && (gameService.getCurrentGame().getCurrentQuest().getTestInStage() == gameService.getCurrentGame().getCurrentQuest().getCurrentStageNumber()));
      currSession.testCard= (currSession.testInPlay) ?  gameService.getCurrentGame().getCurrentQuest().getTestCard() : null;
    }
    // currSession.sponsorId = gameService.getCurrentGame().getCurrentQuest().getSponsor(); //id of the sponsor
    // currSession.participantsId = gameService.getCurrentGame().getCurrentQuest().getParticipantsId();//id of the sponsor



    //For Tournaments: 
    
    // last participant
    //if (gameService.getTournamentInPlay() && gameService.getCurrentActivePlayer() == gameService.getCurrentGame().getCurrentTournament().getLastParticipantId()){

    //}
    if (gameService.getTournamentInPlay()){
        Tournament currentTournament = gameService.getCurrentGame().getCurrentTournament();

        //so when we loop back to the first participant
        if(gameService.getTournamentInPlay() && gameService.getCurrentActivePlayer()==currentTournament.getFirstParticipantId()){
            //getAllTournPlayerCards(); //right now this is triggered from the client in Tournament.js
            System.out.println("The tournament has come to an end!");
            //probably should set the tournament in play to false
            int currentRound = gameService.incrementRound();


            // first round played, and no tie => end of tournament
            if (currentRound == 1 && !currentTournament.getTieOccured()){
                System.out.println("gee: " + currentTournament.getTieOccured());
                gameService.setTournamentInPlay(false);
                currSession.tieBreakerPlayed = true;
                gameService.setCurrentStoryCard(null);

            }
            
            // first round played and tie occurred (so tie breaker round not yet played)
            else if (currentRound == 1 && currentTournament.getTieOccured() && !currentTournament.getTieBreakerPlayed()){
                currentTournament.setTieOccurred(false);
                currSession.tieBreakerPlayed = true;
            }

            // tie breaker round played
            // end tournamnet here
            else if (currentRound == 2){
                gameService.setTournamentInPlay(false);
                currSession.tieBreakerPlayed = false;
                currentTournament.setTieOccurred(false);
                currentTournament.setTieBreakerPlayed(false);
                gameService.setCurrentStoryCard(null);
            }



            // If there's a tie, but the tie breaker round has not been played yet
           /* if (this.gameService.getCurrentGame().getCurrentTournament().getTieOccured() && !(this.gameService.getCurrentGame().getCurrentTournament().getTieBreakerPlayed())){
            currSession.tieBreakerPlayed = true;
            System.out.println("hello, im right here buf"); 
            // this.gameService.getCurrentGame().getCurrentTournament().playingTieBreaker(); // sets tiebreakerplayed = true
            }
            if (this.gameService.getCurrentGame().getCurrentTournament().getTieBreakerPlayed() || this.gameService.getCurrentGame().getCurrentTournament().getTieOccured() == false){
            gameService.setTournamentInPlay(false);
            }*/
            
            
            //alert the player to click finish turn,ok
        }
    }

    
    

    currSession.winners = gameService.getWinners();
   
    return currSession;
  }

@SendTo("/topic/testWinner")
public String testWinner(ArrayList<Integer> participantsId){
  String name = "";
  if(participantsId.size() == 1){
    int id  = participantsId.get(participantsId.size()-1);
    Player player = gameService.getCurrentGame().getPlayerById(id);
    gameService.discardCards(Integer.toString(id), gameService.getCurrentGame().getCurrentQuest().getTestCard().getBids());
    name = player.getName();
  }
  return name;
}

  @MessageMapping("/nextStageIsTest")
  @SendTo("/topic/nextStageIsTest")
  public Test nextStageIsTest() {
    return gameService.getCurrentGame().getCurrentQuest().getTestCard();
  }
  

  

  @MessageMapping("/placeTestBid")
  @SendTo("/topic/finishTurn") // {"message": "3"}
  public Session placeTestBid(@RequestBody ArrayMessage bids) {    
    gameService.getCurrentGame().getCurrentQuest().getTestCard().addBid(bids.getBids());
    System.out.println("Current active player: " + gameService.getCurrentActivePlayer());
    Session currSession = new Session();
    currSession.currentActivePlayer = gameService.startNextPlayer(); ///increments the player
    //skip the sponsor
    if(gameService.getCurrentActivePlayer()==gameService.getCurrentGame().getCurrentQuest().getSponsor()){
      currSession.currentActivePlayer = gameService.startNextPlayer(); ///increments the player
    }
    currSession.currentStoryCard = gameService.getCurrentStoryCard(); //returns all the elments of that storyCard
    currSession.questInPlay = gameService.getQuestInPlay(); //bool
    currSession.testInPlay = (gameService.getCurrentGame().getCurrentQuest().getQuestIncludesTest() && (gameService.getCurrentGame().getCurrentQuest().getTestInStage() == gameService.getCurrentGame().getCurrentQuest().getCurrentStageNumber()));
    currSession.testCard= (currSession.testInPlay) ?  gameService.getCurrentGame().getCurrentQuest().getTestCard() : null;
    currSession.winners = gameService.getWinners();
    
    return currSession;
  }
  

  
  // [[stage 1 cards], [stage 2 cards]] .. ["sfs","grgw","rger"]
  @MessageMapping("/setStages")  //recall this is coming from the sponsor.
  @SendTo("/topic/setStages") // String [] clientStages
  public Quest setStages(@RequestBody DoubleArrayMessage sponsorStages) {  
    ArrayList<ArrayList<String>> arr = sponsorStages.getCards();

    System.out.println(arr);
    gameService.getCurrentGame().getCurrentQuest().setSponsorStages(arr);
    Test test = this.gameService.setTestCard(arr); //<----------------make this function which returns a Test; 
   // System.out.println( "inside the game controller"+test.getName());
    gameService.getCurrentGame().getCurrentQuest().setTestCard(test);
    return gameService.getCurrentGame().getCurrentQuest();
  }
  
  @MessageMapping("/transferQuest")
  @SendTo("/topic/transferQuest")
  public int transferQuest(int playerId) {
    int currentActivePlayerIndex = this.gameService.getCurrentActivePlayer();
    // Player currentActivePlayer = this.gameService.getCurrentGame().getPlayers().get(currentActivePlayerIndex - 1);
    // if (currentActivePlayer.getId() != playerId) return -1;
    
    // if people still left to ask, get next player
    Quest quest = this.gameService.getCurrentGame().getCurrentQuest();
    int amountOfPlayers = this.gameService.getCurrentGame().getPlayers().size();
    int sponsorAttempts = quest.getSponsorAttempts();
    if (sponsorAttempts < amountOfPlayers) {
      int value = sponsorAttempts + (currentActivePlayerIndex - 1);
      int nextPlayerIndex = value > amountOfPlayers ? value - amountOfPlayers: value;
      quest.incrementSponsorAttempts();
      return this.gameService.getCurrentGame().getPlayers().get(nextPlayerIndex).getId();
    }
    
    // else finish turn
    else {
      return -1;
    }
  }


  // ------------- Tournaments ---------------

  @MessageMapping("/addParticipantTournament")
  public void addParticipantTournament(@RequestBody Message playerId){
    int playerIdInt = Integer.parseInt(playerId.getMessage());
    this.gameService.getCurrentGame().getCurrentTournament().addParticipant(playerIdInt);
    Tournament x = this.gameService.getCurrentGame().getCurrentTournament();
    System.out.println(x);
  }

  @MessageMapping("/isSinglePlayerTournament")
  @SendToUser("/queue/isSinglePlayerTournament")
  public boolean isSinglePlayerTournament(){
    if (this.gameService.getNumPlayersTourn() == 1){
      return true;
    }
    return false;
  }

  @MessageMapping("emptyTournament")
  @SendToUser("/queue/emptyTournament")
  public boolean emptyTournament(){
    if (this.gameService.getNumPlayersTourn() <= 0){
      return true;
    }
    return false;
  }

  @MessageMapping("/autoAwardSingleTourn")
  @SendTo("/topic/autoAwardSingleTourn")
  public int autoAwardSingleTourn(){
    return this.gameService.getAutoAwardSinglePlayer();

  }

  @MessageMapping("/addPlayerCardsTourn")
  public boolean addPlayerCardsTourn(@RequestBody CardsMessage message){
    return this.gameService.addPlayerCardsTourn(Integer.parseInt(message.getPlayerId()), message.getCards()); 
  }

  @MessageMapping("/getAllTournPlayerCards")
  @SendTo("/topic/getAllTournPlayerCards")
  public ArrayList<ArrayList<Card>> getAllTournPlayerCards(){
    return this.gameService.getAllTournamentPlayerCards();
  }
  

  // award for when there is no tie (doesn't matter which round)
  // returns the updated shields for the winner
  @MessageMapping("/awardSingleWinner")
  @SendToUser("/queue/awardSingleWinner")
  public int awardSingleWinner(@RequestBody Message message){
    int winnerId = Integer.parseInt(message.getMessage());
    return this.gameService.awardSingleWinner(winnerId);
  }

  @MessageMapping("/awardTiedWinner")
  @SendToUser("/queue/awardTiedWinner")
  public int awardTiedWinner(@RequestBody Message message){
    int winnerId = Integer.parseInt(message.getMessage());
    return this.gameService.awardTiedWinner(winnerId);
  }

  @MessageMapping("/awardSingleGameWinner")
  @SendToUser("/queue/awardSingleGameWinner")
  public int awardSingleGameWinner(@RequestBody Message message){
    int winnerId = Integer.parseInt(message.getMessage());
    return this.gameService.awardSingleGameWinner(winnerId);
  }
  

  @MessageMapping("/discardCardsAfterTournament")
  public void discardCardsAfterTournament(){
    this.gameService.discardCardsAfterTournament();
  }

  @MessageMapping("/discardCardsAfterTie")
  public void discardCardsAfterTie(){
    this.gameService.discardCardsAfterTie();
  }


  
  @MessageMapping("/calculateStage")
  @SendTo("/topic/calculateStage")
  public ArrayList<String> calculateStage() {
    ArrayList<String> survivors = this.gameService.getCurrentGame().calculateSurvivor();
    return survivors;
  }
  
  @MessageMapping("/setClientStage")
  public void setClientStage(@RequestBody CardsMessage cardsMessage) {

    int playerId = Integer.parseInt(cardsMessage.getPlayerId());
    ArrayList<String> cards = cardsMessage.getCards();
    System.out.println(playerId);
    System.out.println(cards.toString());
    
    Quest quest = this.gameService.getCurrentGame().getCurrentQuest();
    quest.setClientStage(playerId, cards);

    //setting the active ally for the player
    this.gameService.setActiveAlliesFromQuestStage(playerId,cards);
    
  }
  
  @MessageMapping("/playEvent")
  @SendTo("/topic/playEvent")
  public HashMap<String, Object> playEvent() {
    EventCard storyCard = (EventCard) this.gameService.getCurrentStoryCard();
    String message;
    HashMap<String, Object> data = new HashMap<>();
    ArrayList<Player> players = this.gameService.getCurrentGame().getPlayers();
    Player drawer = this.gameService.getCurrentGame().getPlayerById(this.gameService.getCurrentActivePlayer());
    
    if (storyCard.getName().equals("King's Recognition")) {
      this.gameService.getCurrentGame().setKingsRecognition(storyCard);
      message = "The next player(s) to complete a Quest will receive 2 extra shields";
    } else {
      message = storyCard.getEventBehaviour().playEvent(players, drawer);      
    }
    
    // iterate through players, and make a list of filtered players
    ArrayList<FilteredPlayer> filteredPlayers = new ArrayList<>();
    for (Player player: players) {
      filteredPlayers.add(new FilteredPlayer(player.getName(), player.getId(), player.getCards(), player.getNumShields()));
    }
    
    data.put("players", filteredPlayers);
    data.put("message", message);
    
    return data;
  }
}