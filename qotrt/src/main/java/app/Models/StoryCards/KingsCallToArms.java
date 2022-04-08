package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.AdventureCards.AdventureCard;
import app.Models.AdventureCards.Weapon;
import app.Models.Enums.AdventureCardType;
import app.Models.Enums.Rank;
import app.Models.General.Player;

public class KingsCallToArms implements EventBehaviour {

  @Override
  public String playEvent(ArrayList<Player> players, Player drawer) {
    Rank highestRankValue = Rank.Squire;
    
    // calculate the highest rank value
    for (Player player: players) {
      if (player.getRankInt().compareTo(highestRankValue) == 1) {
        highestRankValue = player.getRankInt();
      }
    }
    
    for (Player player: players) {
      if (player.getRankInt() == highestRankValue) {
        Weapon weapon = null;
        for (AdventureCard card: player.getCards()) {
          if (card.getAdventureCardType() == AdventureCardType.Weapon) {
            weapon = (Weapon) card;
            player.discardCard(weapon.getName());
            break;
          }
        }
        
        if (weapon == null) {
          int amountOfFoes = 0;
          for (AdventureCard card: player.getCards()) {
            if (amountOfFoes >= 2) break;
            
            if (card.getAdventureCardType() == AdventureCardType.Foe) {
              player.discardCard(card.getName());
              amountOfFoes++;
            }
          }
        }
      }
    }
    
    return "The highest ranked player(s) placed 1 weapon in the discard pile. Otherwise, 2 foe cards were discarded";
  }
}
