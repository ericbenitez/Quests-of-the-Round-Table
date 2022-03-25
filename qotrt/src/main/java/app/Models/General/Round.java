package app.Models.General;

import java.util.ArrayList;

import app.Controllers.GameController;
import app.Models.AdventureCards.AdventureCard;
import app.Models.StoryCards.Quest;
import app.Models.StoryCards.StoryCard;

public class Round {
  public String name;
  public ProgressStatus roundProgress;

  StoryCard storyCard; // each turn identified by story card..
  ArrayList<Player> participants; // for quests
  private int cannotSponsor = 0;
  ArrayList<AdventureCard> discardedCards;
  ArrayList<ArrayList<String>> stageCards;
  private int currentStage = 0;
  private int maxStages = 0;

  private GameController gameController;

  public Round(GameController gameController) {
    this.gameController = gameController;
    this.roundProgress = ProgressStatus.NEW;
    this.participants = new ArrayList<>();
    this.discardedCards = new ArrayList<>();
    this.stageCards = new ArrayList<>();
  }

  public void setStages(ArrayList<ArrayList<String>> clientStages) {
    this.stageCards = clientStages;
  }

  public void setStage(ArrayList<String> clientStage) {
    if (this.stageCards.size() >= maxStages) {
      return;
    }
    stageCards.add(clientStage);
  }

  // the quest participants
  public void addParticipant(Player player) {
    participants.add(player);
  }

  public void withdrawParticipant(int playerId) {
    for (int i = 0; i < participants.size(); i++) {
      if (participants.get(i).getId() == playerId) {
        participants.remove(i);
        return;
      }
    }
  }

  public int getCurrentStage() {
    return this.currentStage;
  }

  public boolean goToNextStage() {
    this.currentStage++;
    return (this.currentStage == this.maxStages) ? false : true;
  }

  public String getName() {
    return this.name;
  }

  public StoryCard setStoryCard(StoryCard storyCard) {
    this.storyCard = storyCard;

    if (storyCard instanceof Quest) {
      Quest quest = (Quest) storyCard;
      this.maxStages = Integer.parseInt(quest.getTotalStages());
    }

    return this.storyCard;
  }

  public void addDiscardedCards(AdventureCard card) {
    discardedCards.add(card);
  }

  public ArrayList<ArrayList<String>> getStageCards() {
    return stageCards;
  }

  public void nextStep(GameController gameController) {
    // get active player
    // send request to pick story card

    // Game game = this.gameService.getCurrentGame();
    // Player player =
    // game.getPlayers().get(this.gameService.getCurrentActivePlayer());

    // new story card
    // if (this.roundProgress == ProgressStatus.NEW) {
    // Game game = gameService.getCurrentGame();

    // Player player = game.getPlayers().get(game.currentActivePlayer);

    // gameController.startTurn(player.getId()); //tell the player to make a move
    // game.setCurrentActivePlayer(game,.getc);
    // }

    // if (this.roundProgress == ProgressStatus.IN_PROGRESS) {

    // }
  }
  
  public void increaseCannotSponsor() {
    this.cannotSponsor++;
  }
  
  public int getCannotSponsor() {
    return this.cannotSponsor;
  }

  /*
   * public void decideTurn(){
   * if (this.storyCard instanceof Quest ){
   * //
   * 
   * }else if (this.storyCard instanceof Tournament){
   * 
   * 
   * }else {
   * this.storyCard.playEvent(game.getPlayers(), game.getCurrentActivePlayer())
   * }
   * 
   * 
   * 
   * 
   * 
   * 
   * }
   */

  /*
   * public drawStoryCard(){
   * 
   * 
   * 
   * }
   * 1) draw random card from the deck
   * 
   * 2) if card is Quest -> Generate quest
   * ask everyone in row to sponsor
   * if sponsor says yes
   * generate stages
   * ask everyone else to participate in sposored Quest ( build participant list)
   * players place cards: No armours of the same kind, one amour card per Quest
   * reveal the results
   * determine winners and award the shields based on number of stages
   * players draw addditional cards
   * 
   * if card is Turnament -> Generate turnament
   * ask everyone in row to participate: (build participant list)
   * players place cards
   * reveal the results
   * determine winner/winners and award shields = bonus shields + # of
   * participants
   * players draw additional cards
   * 
   * 
   * 
   * 
   * if card is Event -> playEvent
   * 
   * 3) Place story card in discarded storyCards
   * 
   * 
   * 
   * 
   * 
   * 
   */

}