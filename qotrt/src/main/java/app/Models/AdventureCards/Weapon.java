package app.Models.AdventureCards;

import app.Models.Enums.AdventureCardType;

public class Weapon extends AdventureCard {
  int battlePoints;

  public Weapon(String name, int battlePoints){
    this.name = name;
    this.description = "";
    this.battlePoints = battlePoints;
    this.adventureCardType = AdventureCardType.Weapon;
  }
  public int getBattlePoints(){
    return this.battlePoints;
  }
}