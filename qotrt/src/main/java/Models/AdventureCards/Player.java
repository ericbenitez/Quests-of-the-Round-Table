package Models.AdventureCards;
import java.util.ArrayList;
public class Player {
  String name;
  int id;
  int numShields;
  

  Game game; // the mediator of players
  
  // list of cards
  ArrayList<AdventureCard> cards; //12 cards
  ArrayList<AdventureCard> hand; 

  public Player(int id, String name){
    this.id = id;
    this.name = name;

    this.numShields = 0;
    
    this.cards = new ArrayList<AdventureCard>();
    this.hand = new ArrayList<AdventureCard>();
  }

  // selects a card to remove from the cards list
  // public AdventureCard selectToDiscard(String name) {
  //   for(AdventureCard card : this.cards) {
  //     if (card.name == name) {
  //       this.cards.remove
  //       return card;
  //     }
  //   }
  //   return null;
  // }

  // set the mediator for players to communicate
  // public void setMediator(Game game) {
  //   this.game = mediator;
  // }

  // draws the given amount of cards to add to the player's cards
  // returns false if there are not enough cards to draw, or if the player will have more than 12 cards total
  public boolean drawCards(int amount){
    if (amount > game.getAdventureDeckSize() || amount+cards.size() > 12){
      return false;
    }
    for (int i = 0; i < amount; ++i){
      AdventureCard card = game.getLastCard();
      if (card != null){cards.add(card);}
      else {return false;}
    }
    return true;
  }

  // allows players to see all discarded cards in this game
  public void displayDiscardedCards(){
    game.displayDiscardedCards();
  }
  
  
  public int getNumAdventureCards(){
    return cards.size();
  }


  public boolean updateShields(int amount){
    this.numShields += amount;
    return true;
  }

  public void printCards(){
    for(AdventureCard card : this.cards) {
      System.out.println(card.name);
    }
  }

  //one card
  public AdventureCard getCard(String name){
    for(AdventureCard card : this.cards) {
      if (card.name == name) {
        return card;
      }
    }
    return null;
  }

  //
  public AdventureCard getHandCard(String name) {
    for(AdventureCard card : this.hand) {
      if (card.name == name) {
        return card;
      }
    }
    return null;
  }

  // gets a string value of the player's rank
  public String getRank() {
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

  /*To determine your personal Battle Score, add the value of your current rank to any additional Weapon/Armor/etc. cards you play*/
public int getPersonalBattleScore(){
  int personalBattleScore = 0;
  String rankName = getRank();
  if (rankName.equals("Squire")){
    //Squire = 5 points
    personalBattleScore+=5;
  }else if(rankName.equals("Knight")){
    //Knight = 10 points
    personalBattleScore+=10;
  }
  else{
  //Champion Knight = 20 points
  personalBattleScore+=20;
    }
  //loop through the hand to add the pts: Ally, Amour, Weapon have battle points
  for(AdventureCard card : this.hand) {
      if (card instanceof Ally) {
        personalBattleScore += card.getBattlePoints();
      }
      else if(card instanceof Amour){
        personalBattleScore+= card.getBattlePoints();
      }
      else if(card instanceof Weapon){
        personalBattleScore += card.getBattlePoints();
      }
  }
  return personalBattleScore;
  }
}