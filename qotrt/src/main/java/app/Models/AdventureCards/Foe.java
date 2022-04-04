package app.Models.AdventureCards;

import app.Models.Enums.AdventureCardType;

public class Foe extends AdventureCard {
  // if Card is the factory
  //Card card;
  int minBattlePoints;
  int maxBattlePoints;

  public Foe(String name, String description, int minPoints, int maxPoints){
    this.name = name;
    this.description = description;
    minBattlePoints = minPoints;
    maxBattlePoints = maxPoints;
    this.adventureCardType = AdventureCardType.Foe;
  }

  public Foe(String name, int minPoints, int maxPoints){
    this.name = name;
    minBattlePoints = minPoints;
    maxBattlePoints = maxPoints;
    this.adventureCardType = AdventureCardType.Foe;
  }

  public Foe(String name, int minPoints){
    this.name = name;
    minBattlePoints = minPoints;
    this.adventureCardType = AdventureCardType.Foe;
  }


  public int getBattlePoints(){
    return minBattlePoints;
  }
  public int getMaxBattlePoints(){
    return maxBattlePoints;
  }
  public String getName(){
    return name;
  }
//The sponsor is calling this function... not sure when tho 
  //Quest has the name of the foe then it's max points
  /*public int getBattlePoints(String name){
    if (this.name == name){
      return maxBattlePoints;
    }
    return minBattlePoints;
  }
  */
  
}