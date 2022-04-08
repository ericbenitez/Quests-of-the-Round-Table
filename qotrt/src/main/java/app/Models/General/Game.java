package app.Models.General;

import app.Objects.CardObjects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.Models.AdventureCards.*;
import app.Models.StoryCards.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Game implements Mediator { // Main = Game
  ArrayList<AdventureCard> discardedCards;
  ArrayList<AdventureCard> adventureCardsDeck;
  ArrayList<StoryCard> storyCardsDeck;
  // CardObjects gameCards; //get the adventure /story cards that are in the game
  ArrayList<Player> players;
  String gameID;
  // Variable which asks the initial/starting player how many players should join
  int numOfPlayers;
  // Status of Game : NEW, IN-Progress, Finished..a set status function below..
  private ProgressStatus status;// we dont have instances of games, it's just the currentGame, if it's not null,
                                // it is NEW/In-progress
  Quest currentQuest;
  Tournament currentTournament;
  EventCard currentEvent;

  CardObjects cardObjects;
  private EventCard kingsRecognitionActive = null;

  @Autowired
  public Game() {
    this.adventureCardsDeck = new ArrayList<>();
    this.storyCardsDeck = new ArrayList<>();
    this.discardedCards = new ArrayList<>();
    currentTournament = null;
    this.cardObjects = new CardObjects();
  }

  public int getNumOfPlayers() {
    return numOfPlayers;
  }

  public Player registerPlayer(Player player) {
    if (players.size() >= numOfPlayers) {
      return null; // we should throw an exception instead...
    }
    player.setMediator(this);
    this.players.add(player); // add to array
    // player.drawCards(12); in the game controller now
    return player;
  }

  public String getGameID() {
    return gameID;
  }

  public ArrayList<String> calculateSurvivor() {
    int sponsorBattlePoints = 0;
    int playerBattlePoints = 0; // max player's points

    ArrayList<ArrayList<String>> stages = currentQuest.getStages(); // the sponsors stage
    HashMap<Integer, ArrayList<String>> clientStage = currentQuest.getClientStage();

    int index;
    if (currentQuest.getCurrentStageNumber() > Integer.parseInt(currentQuest.getTotalStages())) {
      index = currentQuest.getCurrentStageNumber() - 2;
    } else {
      index = currentQuest.getCurrentStageNumber() - 1;
    }

    ArrayList<String> currentSponsorStage = stages.get(index - 1);

    // calculate sponsor battlepoints for stage
    for (String nameOfCard : currentSponsorStage) {
      // Special Cards with Max Battle Points:
      if (currentQuest.getName().equals("Journey Through the Enchanted Forest")) {
        if (nameOfCard.equals("Evil Knight")) {
          AdventureCard advCard = this.cardObjects.getCardByName(nameOfCard);
          Foe foeCard = (Foe) advCard;
          int battlePoints = foeCard.getMaxBattlePoints();
          sponsorBattlePoints += battlePoints;
        }
      }
      if (currentQuest.getName().equals("Slay the Dragon")) {
        if (nameOfCard.equals("Dragon")) {
          AdventureCard advCard = this.cardObjects.getCardByName(nameOfCard);
          Foe foeCard = (Foe) advCard;
          int battlePoints = foeCard.getMaxBattlePoints();
          sponsorBattlePoints += battlePoints;
        }
      }
      if (currentQuest.getName().equals("Repel the Saxon Raiders")) {
        if (nameOfCard.equals("Saxon Knight") || nameOfCard.equals("Saxons")) {
          AdventureCard advCard = this.cardObjects.getCardByName(nameOfCard);
          Foe foeCard = (Foe) advCard;
          int battlePoints = foeCard.getMaxBattlePoints();
          sponsorBattlePoints += battlePoints;
        }
      }
      if (currentQuest.getName().equals("Boar Hunt")) {
        if (nameOfCard.equals("Boar")) {
          AdventureCard advCard = this.cardObjects.getCardByName(nameOfCard);
          Foe foeCard = (Foe) advCard;
          int battlePoints = foeCard.getMaxBattlePoints();
          sponsorBattlePoints += battlePoints;
        }
      }
      if (currentQuest.getName().equals("Rescue the Fair Maiden")) {
        if (nameOfCard.equals("Black Knight")) {
          AdventureCard advCard = this.cardObjects.getCardByName(nameOfCard);
          Foe foeCard = (Foe) advCard;
          int battlePoints = foeCard.getMaxBattlePoints();
          sponsorBattlePoints += battlePoints;
        }
      }
      if (currentQuest.getName().equals("Test of the Green Knight")) {
        if (nameOfCard.equals("Green Knight")) {
          AdventureCard advCard = this.cardObjects.getCardByName(nameOfCard);
          Foe foeCard = (Foe) advCard;
          int battlePoints = foeCard.getMaxBattlePoints();
          sponsorBattlePoints += battlePoints;
        }
      } else {
        AdventureCard advCard = this.cardObjects.getCardByName(nameOfCard);
        int battlePoints = advCard.getBattlePoints();
        sponsorBattlePoints += battlePoints;
      }
    }

    for (Map.Entry<Integer, ArrayList<String>> entry : clientStage.entrySet()) {
      Integer playerId = entry.getKey();
      ArrayList<String> cards = entry.getValue();
      // remove the loserfrom the participants id if they dont have battlepoints
      // higher than sponsor
      for (String cardName : cards) {
        AdventureCard card = this.cardObjects.getCardByName(cardName);
        if (!(card instanceof Ally)) {
          playerBattlePoints += card.getBattlePoints();
        }
      }

      Player currentPlayer = this.getPlayerById(playerId);
      playerBattlePoints += currentPlayer.getRankPts();
      playerBattlePoints += calcAllyPtsForPlayer(playerId);
      if (currentPlayer.getAmour() != null) {
        playerBattlePoints += currentPlayer.getAmour().getBattlePoints();
      }

      if (playerBattlePoints < sponsorBattlePoints) {
        this.currentQuest.getParticipantsId().remove(playerId);
      }
    }

    ArrayList<String> survivors = new ArrayList<>();
    ArrayList<Player> survivorPlayers = new ArrayList<>();

    rewardSurvivors(currentQuest.getParticipantsId()); // if they made it out alive of a stage
    for (Integer playerId : currentQuest.getParticipantsId()) {
      String playerName = this.getPlayerById(playerId).getName();
      survivorPlayers.add(this.getPlayerById(playerId));
      survivors.add(playerName);
    }

    if (currentQuest.getCurrentStageNumber() > Integer.parseInt(currentQuest.getTotalStages())) {
      if (this.kingsRecognitionActive != null) {
        this.kingsRecognitionActive.getEventBehaviour().playEvent(survivorPlayers, null);
        this.kingsRecognitionActive = null;

        System.out.println("Kings recognition was played");
      }
    }

    return survivors; // the peeps who survived
  }

  public void rewardSurvivors(ArrayList<Integer> survivors) {
    for (int id : survivors) {
      Player p = this.getPlayerById(id);
      p.drawCards(1);
    }
  }

  public void rewardSponsor(Integer playerId) {
    Integer numOfAdvCards = 0;
    ArrayList<ArrayList<String>> stages = currentQuest.getStages();
    for (int i = 0; i < stages.size(); i++) { // loop for the rows
      for (int j = 0; j < stages.get(i).size(); j++) { // loop for the elements in each row
        numOfAdvCards++;
      }
    }
    Player p = this.getPlayerById(playerId);
    p.drawCards(numOfAdvCards);

  }

  // Helper function - Given a playerId, calculates and returns the battle pts for
  // the allies in play
  private int calcAllyPtsForPlayer(int playerId) {
    Player player = this.getPlayerById(playerId);
    int totalPts = 0;

    ArrayList<Ally> activeAllies = new ArrayList<>(); /* player.getActiveAllies(); */ // change once we have the actual
                                                                                      // arraylist
    for (Ally ally : activeAllies) {
      totalPts += ally.getBattlePoints(this.getCurrentQuest().getName(), activeAllies);
    }
    return totalPts;
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
   * 
   * @param cards
   */
  public void setAdventureCards(ArrayList<AdventureCard> cards) {
    this.adventureCardsDeck = cards;
  }

  /**
   * Sets the game's story cards a list
   * 
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
   * 
   * @return StoryCard
   */
  public StoryCard pickCard() {
    if (this.storyCardsDeck.isEmpty()) {
      return null;
    }
    StoryCard card;
    card = this.storyCardsDeck.get(storyCardsDeck.size() - 1);
    if (card instanceof Quest) {
      this.setCurrentQuest((Quest) card); // this is also setting the current whereas we have setcurrent function as
                                          // well
      // this.currentQuest.draw(this.getPlayerById(playerId));
    } else if (card instanceof Tournament) {
      this.setCurrentTournament((Tournament) card);
    } else if (card instanceof EventCard) {
      this.setCurrentEvent((EventCard) card);
    }

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

  public void addDiscardedCards(AdventureCard card) {
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

  // Sets the num of players and initializes the arraylist with that capacity so
  // no
  // more players can join
  public void setNumOfPlayers(int x) {
    numOfPlayers = x;
    this.players = new ArrayList<Player>(x);
  }

  public void setProgressStatus(ProgressStatus x) {
    status = x;
  }

  public ProgressStatus getProgressStatus() {
    return status;
  }

  public ArrayList<AdventureCard> getAdventureCardsDeck() {
    return adventureCardsDeck;
  }

  public void setCurrentQuest(Quest card) {
    this.currentQuest = card;
    // this.currentQuest.setGame()
  }

  public Quest getCurrentQuest() {
    return currentQuest;
  }

  public Player getPlayerById(int playerId) {
    for (Player player : players) {
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
      if (p.getNumShields() >= 5) {
        return p; // it has enough shields to be a knight
      }
    }
    return null;
  }

  // ------------- Tournament ---------------
  public void setCurrentTournament(Tournament tournament) {
    currentTournament = tournament;
  }

  public Tournament getCurrentTournament() {
    return currentTournament;
  }

  // calculates total battle points given a list of cards
  public int calcBattlePtsOfCards(ArrayList<String> cards) {
    int total = 0;
    for (String cardName : cards) {
      total += cardObjects.getBattlePtsByName(cardName);
    }
    return total;
  }

  public ArrayList<Integer> calcWinnersTournament() {
    int max = 0;

    ArrayList<Integer> winners = new ArrayList<>();
    for (int i : this.currentTournament.getParticipants()) {
      int currPts = calcBattlePtsOfCards(currentTournament.getPlayerCards(i));
      currPts += this.getPlayerById(i).getRankPts();
      if (currPts > max) {
        max = currPts;
        winners.clear();
        winners.add(i);
      } else if (currPts == max) {
        winners.add(i);
      }
    }

    return winners;
  }

  public CardObjects getCardObjects() {
    return cardObjects;
  }

  // ~~~~~~~~~~~~~~~EventCard~~~~~~~~~~~~~~~~~~~~~~~~~
  public void setCurrentEvent(EventCard event) {
    currentEvent = event;
  }

  public EventCard getCurrentEvent(EventCard event) {
    return currentEvent;
  }

  public boolean isKingsRecognitionActive() {
    return this.kingsRecognitionActive == null;
  }

  public void setKingsRecognition(EventCard kingsRecognition) {
    this.kingsRecognitionActive = kingsRecognition;
  }

}