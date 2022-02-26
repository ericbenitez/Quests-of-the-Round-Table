import java.util.ArrayList;
public class Player {
  String name;
  // list of cards
  ArrayList<AdventureCard> cards;
  ArrayList<AdventureCard> hand;

  public Player(){
    this.cards = new ArrayList<AdventureCard>();
    this.hand = new ArrayList<AdventureCard>();
  }
  
  public int getNumAdventureCards(){
    return cards.size();
  }

  public void printCards(){
    for(AdventureCard card : this.cards) {
      System.out.println(card.name);
    }
  }
  
  public AdventureCard getCard(String name){
    for(AdventureCard card : this.cards) {
      if (card.name == name) {
        return card;
      }
    }
    return null;
  }
  
  public AdventureCard getHandCard(String name) {
    for(AdventureCard card : this.hand) {
      if (card.name == name) {
        return card;
      }
    }
    return null;
  }
}