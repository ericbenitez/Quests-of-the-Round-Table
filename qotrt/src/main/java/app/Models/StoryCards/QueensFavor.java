package app.Models.StoryCards;

import java.util.ArrayList;
import app.Models.General.Player;
import app.Models.Enums.Rank;

public class QueensFavor implements EventBehaviour {

  // The lowest ranked player(s) immediately receives 2 Aventure Cards
  public String playEvent(ArrayList<Player> players, Player Drawer) {
    if (players.size() < 1) {
      return "The lowest ranked player(s) received 2 Adventure Cards";
    }
    Rank minRank = getMinRank(players);
    for (Player player : players) {
      if (player.getRankInt() == minRank) {
        player.drawCards(2);
      }
    }
    
    return "The lowest ranked player(s) received 2 Adventure Cards";
  }

  // gets the mininum Rank given an array of players
  private Rank getMinRank(ArrayList<Player> players) {
    Rank minRank = players.get(0).getRankInt();

    for (Player player : players) {
      Rank currRank = player.getRankInt();
      if (currRank.compareTo(minRank) < 0) {
        minRank = currRank;
      }
    }
    return minRank;
  }
}