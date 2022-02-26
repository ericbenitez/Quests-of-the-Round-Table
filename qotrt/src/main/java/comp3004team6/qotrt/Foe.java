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

  

  public int getBattlePoints(String name){
    if (this.name == name){
      return maxBattlePoints;
    }
    return minBattlePoints;
  }
  
  
}