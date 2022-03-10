package app.Models.AdventureCards;
public class Weapon extends AdventureCard {
  int battlePoints;
 

  public Weapon(String name, int battlePoints){
    this.name = name;
    this.description = "";
    this.battlePoints = battlePoints;
  }
  public int getBattlePoints(){
    return this.battlePoints;
  }
}