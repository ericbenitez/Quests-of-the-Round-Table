package app.Models.AdventureCards;

import app.Models.General.*;

public abstract class AdventureCard extends Card {
  Player owner;

  public abstract int getBattlePoints(); // not all classes have it but need it for getPersonalBattleScore function in
                                         // player.
  
  /**
   * Returns the card's name
   * @return String
   */
  public String getName() {
    return this.name; 
  }
 
}
