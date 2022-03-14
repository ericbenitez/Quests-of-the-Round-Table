package app.Models.AdventureCards;

import app.Models.General.*;

public class Test extends AdventureCard {
  int minBid;
  Game game;

  //three constructors, one without the min Bid which will set it to a default value of 0
  public Test(String name){
    this.name = name;
    this.description = "";
    this.minBid = 0;
  }

 

  public Test(String name, int minBid){
    this.name = name;
    this.description = "";
    this.minBid = minBid;
  }
  
  public int getMinBid(){
    if (this.name == "Test of the Questing Beast" && this.game.getTurns().get(this.game.getTurns().size()-1).getName() == "Search for the Questing Beast") {
      return 4;
    }
    return this.minBid;
  }

  public int getBattlePoints(){
    return 0;
  }
}