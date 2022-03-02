package Models.AdventureCards;
public abstract class Card {
  String name;
  String description;
  boolean cardFace = false;

  boolean getCardFace() { return this.cardFace; }
  
  abstract void draw(Player player);
}

