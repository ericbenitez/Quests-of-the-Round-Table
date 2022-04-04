package app.Models.AdventureCards;

import app.Models.Enums.AdventureCardType;

public class Amour extends AdventureCard {
  
  int battlePoints;
  int bids;

  public Amour(String name, String description, int battlePoints, int bids){
    this.name = name;
    this.description = description;
    this.battlePoints = battlePoints;
    this.bids = bids;
    this.adventureCardType = AdventureCardType.Amour;
  }

  public int getBattlePoints(){
    return battlePoints;
  }
}