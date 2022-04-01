package app.Models.General;
import java.util.ArrayList;
import app.Models.AdventureCards.*;
import app.Models.Enums.Rank;

public class Player {
  public String name;
  private static int uniqueId = 1;
  private int id;
  private int numShields;
  private Game game; 

  // list of cards
  public Card pickedCard; // from the story deck
  public ArrayList<AdventureCard> cards; // 12 cards
  private Amour currentAmour;
  public ArrayList <Ally> activeAllies;


  public Player(String name) {
    this.name = name;
    this.id = uniqueId;
    this.numShields = 0;
    uniqueId++; 
    this.cards = new ArrayList<AdventureCard>();
    this.currentAmour = null;
    this.activeAllies = new ArrayList<Ally>();
  }

  /**
   * Returns the name of the player
   *
   * @return String
   */
  public String getName() {
    return this.name;
  }

  public Game getGame() {
    return this.game;
  }
  


  /**
   * Returns the id of the player
   *
   * @return int
   */
  public int getId() {
    return this.id;
  }

  public int getNumShields() {
    return numShields;
  }


  /***
   * Here the problem is the player can discard cards, but they don't go in the discarded pile.......
   * @param cardName
   * @return
   */
  // selects a card to remove from the cards list
  public AdventureCard discardCard(String cardName) {
    for (AdventureCard card : this.cards) {
      if ((card.name).equals(cardName)) {
        this.cards.remove(card);
        this.game.addDiscardedCards(card);
        return card;
      }
    }
    return null;
  }


  // set the mediator for players to communicate
  public void setMediator(Game game) {
    this.game = game;
  }

  // draws the given amount of cards to add to the player's cards
  // returns false if there are not enough cards to draw, or if the player will
  // have more than 12 cards total
  public boolean drawCards(int amount) {
    if (amount > game.getAdventureDeckSize() /*|| amount + cards.size() > 12*/) { // for now, don't check the size because we want the player to have the option to get more than 12 cards, and AFTER they can remove cards so that its <= 12
      return false;
    }
    for (int i = 0; i < amount; ++i) {
      AdventureCard card = game.getLastCard();
      if (card != null) {
        cards.add(card);
      } else {
        return false;
      }
    }
    return true;
  }

  // allows players to see all discarded cards in this game
  public void displayDiscardedCards() {
    game.displayDiscardedCards();
  }

  public int getNumAdventureCards() {
    return cards.size();
  }

  public boolean updateShields(int amount) {
    this.numShields += amount;
    return true;
  }

  public void printCards() {
    for (AdventureCard card : this.cards) {
      System.out.println(card.name);
    }
  }

  // one card
  public AdventureCard getCard(String name) {
    for (AdventureCard card : this.cards) {
      if (card.name == name) {
        return card;
      }
    }
    return null;
  }

  /**
   * Prints the picked card of the player
   */
  public void printPickedCard() {
    System.out.println(this.pickedCard.name);
  }

  //
  public AdventureCard getHandCard(String name) {
    for (AdventureCard card : this.cards) {
      if (card.name == name) {
        return card;
      }
    }
    return null;
  }

  // gets a string value of the player's rank
  public String getRankString() {
    if (this.numShields < 5) {
      return "Squire";
    }

    else if (this.numShields >= 5 && this.numShields < 12) {
      return "Knight";
    }

    else if (this.numShields >= 12 && this.numShields < 22) {
      return "Champion Knight";
    }

    else {
      return "Knight of the Round Table";
    }
  }

  public ArrayList<AdventureCard> getCards() {
    return this.cards;
  }

  // gets a int value of the player's rank
  public Rank getRankInt() {
    if (this.numShields < 5) {
      return Rank.Squire;
    }

    else if (this.numShields >= 5 && this.numShields < 12) {
      return Rank.Knight;
    }

    else {
      return Rank.ChampionKnight;
    }
  }

  /*
   * To determine your personal Battle Score, add the value of your current rank
   * to any additional Weapon/Armor/etc. cards you play
   */
  public int getPersonalBattleScore() {
    int personalBattleScore = 0;
    String rankName = getRankString();
    if (rankName.equals("Squire")) {
      // Squire = 5 points
      personalBattleScore += 5;
    } else if (rankName.equals("Knight")) {
      // Knight = 10 points
      personalBattleScore += 10;
    } else {
      // Champion Knight = 20 points
      personalBattleScore += 20;
    }
    // loop through the hand to add the pts: Ally, Amour, Weapon have battle points
    for (AdventureCard card : this.cards) {
      if (card instanceof Ally) {
        personalBattleScore += card.getBattlePoints();
      } else if (card instanceof Amour) {
        personalBattleScore += card.getBattlePoints();
      } else if (card instanceof Weapon) {
        personalBattleScore += card.getBattlePoints();
      }
    }
    return personalBattleScore;
  }


  public int getUniqueId(){
    return uniqueId;
  }

  public int getRankPts(){
    String rankName = getRankString();
    if (rankName.equals("Squire")) {
      // Squire = 5 points
      return 5;
    } else if (rankName.equals("Knight")) {
      // Knight = 10 points
      return 10;
    } else {
      // Champion Knight = 20 points
      return 20;
    }
  }


  public void addCard(AdventureCard newCard){
    cards.add(newCard);
  }

  public void setAmour(Amour amourCard){
    currentAmour = amourCard;
  }

  public Amour getAmour(){
    return currentAmour;
  }

}