package Models.AdventureCards;
public class Amour extends AdventureCard {
  
  int battlePoints;
  int bids;

  public Amour(String name, String description, int battlePoints, int bids){
    this.name = name;
    this.description = description;
    this.battlePoints = battlePoints;
    this.bids = bids;
  }

  public int getBattlePoints(){
    return battlePoints;
  }
}