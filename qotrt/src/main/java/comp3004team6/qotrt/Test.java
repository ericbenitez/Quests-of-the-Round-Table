public class Test extends AdventureCard {
  int minBid;
  Main game;

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
    if (this.name == "Test of the Questing Beast" && this.game.turns.get(this.game.turns.size()-1).name == "Search for the Questing Beast") {
      return 4;
    }
    return this.minBid;
  }
}