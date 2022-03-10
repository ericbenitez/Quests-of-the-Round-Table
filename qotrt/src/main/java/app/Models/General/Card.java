package app.Models.General;

public abstract class Card {
  protected String name;
  protected String description;
  protected boolean cardFace = false;

  boolean getCardFace() { return this.cardFace; }
  
  protected abstract void draw(Player player);
  
  @Override
  public String toString() {
    return this.name + "\n";
  }
}
