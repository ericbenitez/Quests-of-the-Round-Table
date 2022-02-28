public class Ally extends AdventureCard {
  int battlePoints;
  int bids;
  Game game;
  
  // -1 is passed into battlePts and bids when no value exists for it
  public Ally(String name, int battlePts, int bids, Game game) {
    this.name = name;
    this.description = "";
    this.battlePoints = battlePts;
    this.bids = bids;
    this.game = game;
  }

 

  public int getBattlePoints() {
    // if (this.name == "Lancelot" && ) return 25;
    if (this.name == "Sir Gawain" && this.game.getCurrentTurnName() == "Test of the Green Knight Quest") {
      return this.battlePoints + 20; 
    }
    
    if (this.name == "Sir Gawain" && this.game.getCurrentTurnName() == "Test of the Green Knight Quest") {
      return this.battlePoints + 20; 
    }
    
    if (this.name == "Sir Percival" && this.game.getCurrentTurnName() == "Search for the Holy Grail Quest") {
      return this.battlePoints + 20;
    }
    
    if (this.name == "Sir Lancelot" && this.game.getCurrentTurnName() == "Quest to Defend the Queen's Honor") {
      return this.battlePoints + 25;
    }
    
    if (this.name == "Sir Tristan" && (this.owner != null) && (this.owner.getHandCard("Queen Iseult") != null)) {
      return this.battlePoints + 20;
    }

    return this.battlePoints;
  }

public int getBids() {
    if (this.name =="Queen Iseult" && (this.owner != null) && (this.owner.getHandCard("Tristan") != null)) {
      return 4;
    }
    
    return this.bids;
  }
}

