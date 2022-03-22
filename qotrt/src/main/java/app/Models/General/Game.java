package app.Models.General;
import app.Objects.CardObjects;
import java.util.ArrayList;


import app.Models.AdventureCards.*;
import app.Models.StoryCards.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Game implements Mediator { // Main = Game
  ArrayList<AdventureCard> discardedCards;
  ArrayList<AdventureCard> adventureCardsDeck;
  ArrayList<StoryCard> storyCardsDeck;
  CardObjects gameCards; //get the adventure /story cards that are in the game
  ArrayList<Player> players; 
  String gameID;
  // Variable which asks the initial/starting player how many players should join
  int numOfPlayers;
  //Status of Game : NEW, IN-Progress, Finished..a set status function below..
  private ProgressStatus status;//we dont have instances of games, it's just the currentGame, if it's not null, it is NEW/In-progress
  Quest currentQuest;

  @Autowired
  public Game() {
    this.adventureCardsDeck = new ArrayList<>();
    this.storyCardsDeck = new ArrayList<>();
    this.discardedCards = new ArrayList<>();

  }
  
  public int getNumOfPlayers(){
    return numOfPlayers;
  }

  public Player registerPlayer(Player player) {
    if (players.size() >= numOfPlayers) {
      return null; //we should throw an exception instead...
    }
    player.setMediator(this); 
    this.players.add(player); //add to array
    // player.drawCards(12); in the game controller now
    return player;
  }
  
  public String getGameID(){
    return gameID;
  }
  
  // removes a player
  public Player removePlayer(Player player) {
    int i = this.players.indexOf(player);
    if (i >= 0) {
      this.players.remove(i);
    }

    return player;
  }

  /**
   * Sets the game's adventure cards to a list
   * @param cards
   */
  public void setAdventureCards(ArrayList<AdventureCard> cards) {
    this.adventureCardsDeck = cards;
  }
  
  /**
   * Sets the game's story cards a list
   * @param cards
   */
  public void setStoryCards(ArrayList<StoryCard> cards) {
    this.storyCardsDeck = cards;
  }

  // removes the last card from the adventure card deck
  public AdventureCard getLastCard() {
    if (adventureCardsDeck.size() == 0) {
      return null;
    }
    return adventureCardsDeck.remove(adventureCardsDeck.size() - 1);
  }

  /**
   * Picks a story card from the deck and returns it
   * @return StoryCard
   */
  public StoryCard pickCard() {
    if (this.storyCardsDeck.isEmpty()) {
      return null;
    }
    StoryCard card;
    card = this.storyCardsDeck.get(storyCardsDeck.size() - 1);
    if (card instanceof Quest) {
      this.setCurrentQuest((Quest) card); //this is also setting the current whereas we have setcurrent function as well
      // this.currentQuest.draw(this.getPlayerById(playerId));
    }
    //have to do this for all other story cards.
    
    this.storyCardsDeck.remove(storyCardsDeck.size() - 1);
    return card;
  }

 

  // displays all the discarded cards
  public void displayDiscardedCards() {
    for (AdventureCard card : discardedCards) {
      System.out.println(card.name);
    }
  }

  // returns all the discarded cards
  public ArrayList<String> getDiscardedCards() {
    ArrayList<String> dCards = new ArrayList<>();
   
    for (AdventureCard card : discardedCards) {
      dCards.add(card.name);
    }
    return dCards;
  }

  public void addDiscardedCards(AdventureCard card){
    discardedCards.add(card);
  }
  /**
   * Returns the size of the adventure cards deck
   * 
   * @return int
   */
  public int getAdventureDeckSize() {
    return adventureCardsDeck.size();
  }

 
  /**
   * Returns the connected players
   * 
   * @return ArrayList<Player>
   */
  public ArrayList<Player> getPlayers() {
    return this.players;
  }



  // From TTT
  public void setGameID(String x) {
    gameID = x;
  }

  // Sets the num of players and initializes the arraylist with that capacity so no
  // more players can join
  public void setNumOfPlayers(int x) {
    numOfPlayers = x;
    this.players = new ArrayList<Player>(x);
  }

  public void setProgressStatus(ProgressStatus x){
    status = x;
  }
  public ProgressStatus getProgressStatus(){
    return status;
  }
 
 
  public ArrayList<AdventureCard> getAdventureCardsDeck(){return adventureCardsDeck;}

public void setCurrentQuest(Quest card){
      this.currentQuest = card;
}

  public Quest getCurrentQuest(){
    return currentQuest;
  }

  public Player getPlayerById(int playerId) {
    for (Player player: players) {
      if (player.getId() == playerId) {
        return player;
      }
    }
    
    return null;
  }
  
  public Player checkWinner() {
    // return the winning player
    // Player winningPlayer;
    for (Player p : players) {
        if (p.getNumShields() >= 7) {
            return p; // it has enough shields to be a knight
        }
    }
    return null;
}
  
  
}