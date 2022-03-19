package app.Models.General;

import org.springframework.beans.factory.annotation.Autowired;

import app.Service.GameService;

public abstract class Card {
  protected String name;
  protected String description;
  protected boolean cardFace = false;
  
  public String getName() {
    return this.name;
  }
  
  @Autowired
  protected GameService gameService; //mediator ...

  boolean getCardFace() { return this.cardFace; }
  
  protected abstract void draw(Player player);
  
  @Override
  public String toString() {
    return this.name + "\n";
  }
}
