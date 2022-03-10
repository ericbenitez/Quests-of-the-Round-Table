package app.Models.StoryCards;
import java.util.ArrayList;

import Models.General.*;
import app.Models.General.Player;


public interface QuestBehaviour {

    void playEvent(ArrayList<Player> players, Player drawer);
  
}