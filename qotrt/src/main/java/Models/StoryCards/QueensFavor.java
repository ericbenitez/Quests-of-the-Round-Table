package Models.StoryCards;

import java.util.ArrayList;
import Models.General.Player;
import Models.Enums.Rank;

public class QueensFavor implements EventBehaviour {

  // The lowest ranked player(s) immediately receives 2 Aventure Cards
  public void playEvent(ArrayList<Player> players, Player Drawer) {
    if (players.size() < 1) {
      return;
    }
    Rank minRank = getMinRank(players);
    for (Player player : players) {
      if (player.getRankInt() == minRank) {
        player.drawCards(2);
      }
    }
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