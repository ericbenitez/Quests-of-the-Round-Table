package app.Models.General;
import app.Objects.CardObjects;
import java.util.ArrayList;

import app.Models.AdventureCards.*;
import app.Models.StoryCards.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Game implements Mediator { // Main = Game
  Player currentActivePlayer;
  ArrayList<AdventureCard> adventureCardsDeck;
  ArrayList<StoryCard> storyCardsDeck;
  CardObjects gameCards; //get the adventure /story cards that are in the game

  ArrayList<Player> players; // the observers...
  int uniquePlayerId; // increments every time a player is registered
  ArrayList<Round> rounds; // size is 0
  public ArrayList<String> requests;
  String gameID;

  // Variable which asks the initial/starting player how many players should join
  int numOfPlayers;

  //Status of Game : NEW, IN-Progress, Finished..a set status function below..
  private ProgressStatus status;

  //we dont have instances of games, it's just the currentGame, if it's not null, it is NEW/In-progress
  Game currentGame=null; //setGame(Game g) below

  @Autowired
  public Game() {
    this.adventureCardsDeck = new ArrayList<>();
    this.storyCardsDeck = new ArrayList<>();
    this.rounds = new ArrayList<>();
    this.uniquePlayerId = 0;
  }
  public int getNumOfPlayers(){
    return numOfPlayers;
  }
  // observes a player
  public Player registerPlayer(Player player) {
    if (players.size() >= numOfPlayers) {
      return null; //we should throw an exception instead...
    }
    player.setMediator(this);
    // added by Donna: so I can see the cards for making the front end
    // player.drawCards(12);

    this.players.add(player);
    this.uniquePlayerId += 1;
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
    
    this.getCurrentRound().storyCard = this.storyCardsDeck.remove(0);
    
    return this.getCurrentRound().storyCard;
  }
  
  public String getCurrentTurnName() {
    if (this.rounds.isEmpty())
      return null;
    return this.rounds.get(this.rounds.size() - 1).getName();
  }

  public Round getCurrentRound() {
    if (this.rounds.isEmpty())
      return null;
    return this.rounds.get(this.rounds.size() - 1);
  }

  // displays all the discarded cards
  public void displayDiscardedCards() {
    if (rounds.size() == 0) {
      return;
    }
    for (AdventureCard card : rounds.get(rounds.size() - 1).discardedCards) {
      System.out.println(card.name);
    }
  }

  public Round startNewRound() {
    Round round  = new Round();
    this.rounds.add(round);
    return round;
  }

  // returns all the discarded cards
  public ArrayList<String> getDiscardedCards() {
    ArrayList<String> dCards = new ArrayList<>();
    if (rounds.size() == 0) {
      return dCards;
    }
    for (AdventureCard card : rounds.get(rounds.size() - 1).discardedCards) {
      dCards.add(card.name);
    }
    return dCards;
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
   * Returns the turns list
   */
  public ArrayList<Round> getRounds() {
    return this.rounds;
  }

  /**
   * Returns the connected players
   * 
   * @return ArrayList<Player>
   */
  public ArrayList<Player> getPlayers() {
    return this.players;
  }

  public Player getCurrentActivePlayer() {
    return currentActivePlayer;
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
  public void setGame(Game g){
    currentGame=g;
  }
  public Game getCurrentGame(){
    return currentGame;
  }
  public int getUniquePlayerId() {return uniquePlayerId;}
  public ArrayList<AdventureCard> getAdventureCardsDeck(){return adventureCardsDeck;}

  Quest currentQuest;
  public void setCurrentQuest(String name){
    for(StoryCard c: storyCardsDeck){
      if(c instanceof Quest){
        System.out.println("name of the card"+c.getName());
        if (c.getName().equals(name)){ //added getName
          System.out.println("setting name");
          currentQuest = (Quest) c;
        }
      }
    }
  }
  public Quest getCurrentQuest(){
    return currentQuest;
  }

}