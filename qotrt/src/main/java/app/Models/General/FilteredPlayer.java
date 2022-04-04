package app.Models.General;

import java.util.ArrayList;

import app.Models.AdventureCards.AdventureCard;

public class FilteredPlayer {
  public String name;
  public int id;
  public ArrayList<AdventureCard> cards;
  public int shields;
  
  public FilteredPlayer(String name, int id, ArrayList<AdventureCard> cards, int shields) {
    this.name = name;
    this.id = id;
    this.cards = cards;
    this.shields = shields;
  }
}