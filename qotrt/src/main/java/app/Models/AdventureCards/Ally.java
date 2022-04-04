package app.Models.AdventureCards;

import java.util.ArrayList;

import app.Models.Enums.AdventureCardType;

public class Ally extends AdventureCard {
  int battlePoints;
  int bids;

  // -1 is passed into battlePts and bids when no value exists for it
  public Ally(String name, int battlePts, int bids) {
    this.name = name;
    this.description = "";
    this.battlePoints = battlePts;
    this.bids = bids;
    this.adventureCardType = AdventureCardType.Ally;
  }

  public int getBattlePoints() {
    // if (this.name == "Lancelot" && ) return 25;
    // if (this.name == "Sir Gawain" && this.gameService.getCurrentGame().getCurrentTurnName() == "Test of the Green Knight Quest") {
    //   return this.battlePoints + 20;
    // }

    // if (this.name == "Sir Gawain" && this.gameService.getCurrentGame().getCurrentTurnName() == "Test of the Green Knight Quest") {
    //   return this.battlePoints + 20;
    // }

    // if (this.name == "Sir Percival" && this.gameService.getCurrentGame().getCurrentTurnName() == "Search for the Holy Grail Quest") {
    //   return this.battlePoints + 20;
    // }

    // if (this.name == "Sir Lancelot" && this.gameService.getCurrentGame().getCurrentTurnName() == "Quest to Defend the Queen's Honor") {
    //   return this.battlePoints + 25;
    // }

    // if (this.name == "Sir Tristan" && (this.owner != null) && (this.owner.getHandCard("Queen Iseult") != null)) {
    //   return this.battlePoints + 20;
    // }
    // return this.battlePoints;
    // TODO FIX BATTLEPOINTS LATER
    return battlePoints; // so that the client has it (for tournaments)
  }

  // return the correct battle points given the current quest, and the allies at play 
  public int getBattlePoints(String questName, ArrayList<Ally> alliesInPlay){
    if (this.name == "Sir Gawain" && questName == "Test of the Green Knight Quest") {
      return this.battlePoints + 20;
    }

    if (this.name == "Sir Percival" && questName == "Search for the Holy Grail Quest") {
      return this.battlePoints + 20;
    }
  
    if (this.name == "Sir Lancelot" && questName == "Quest to Defend the Queen's Honor") {
        return this.battlePoints + 25;
    }

    if (this.name == "Sir Tristan"){
      for (Ally ally: alliesInPlay){
        if (ally.getName() == "Queen Iseult"){
          return this.battlePoints + 20;
        }
      }
    }

    return this.battlePoints;
  }

  public int getBids() {
    if (this.name == "Queen Iseult" && (this.owner != null) && (this.owner.getHandCard("Tristan") != null)) {
      return 4;
    }

    return this.bids;
  }


  public String getName() {
    return name;
  }

}
