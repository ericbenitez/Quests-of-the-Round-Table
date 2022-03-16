package app.Models.AdventureCards;
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
  }

  public Foe(String name, int minPoints, int maxPoints){
    this.name = name;
    minBattlePoints = minPoints;
    maxBattlePoints = maxPoints;
  }

  public Foe(String name, int minPoints){
    this.name = name;
    minBattlePoints = minPoints;
  }


  public int getBattlePoints(){
    return minBattlePoints;
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