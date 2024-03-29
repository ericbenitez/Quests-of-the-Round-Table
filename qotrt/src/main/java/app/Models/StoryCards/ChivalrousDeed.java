package app.Models.StoryCards;

import java.util.ArrayList;
import app.Models.Enums.Rank;

import app.Models.General.*;

public class ChivalrousDeed implements EventBehaviour {

    public String playEvent(ArrayList<Player> players, Player drawer) {
        if (players.size() <= 0){
            return "Player(s) with both lowest rank and least amount of shields, received 3 shields";}
        // calculate least amount of shields value
        int leastAmountOfShields = players.get(0).getNumShields();
        for (Player player: players) {
            if (player.getNumShields() < leastAmountOfShields) {
                leastAmountOfShields = player.getNumShields();
            }
        }
        
        // find players with this value
        for (Player player: players) {
            if (player.getNumShields() == leastAmountOfShields) {
                player.updateShields(3);
            }
        }
        
        // Player minRanked = players.get(0);

        // Player current = null;

        // Rank lowestRankValue = Rank.Squire;

        // for (int i = 0; i < players.size(); i++) {

        //     current = players.get(i);
        //     if (current.getRankInt().compareTo(lowestRankValue) == -1) {
        //         minRanked = current;
        //     }

        // }

        // Player minShielded = players.get(0);

        // for (int i = 1; i < players.size(); i++) {

        //     current = players.get(i);
        //     if (current.getNumShields() < minShielded.getNumShields()) {
        //         minShielded = current;
        //     }

        // }

        // if (minShielded.equals(minRanked)) {
        //     minShielded.updateShields(3);

        // } else {
        //     minShielded.updateShields(3);
        //     minRanked.updateShields(3);
        // }
        
        return "Player(s) with both lowest rank and least amount of shields, received 3 shields";
    }
}