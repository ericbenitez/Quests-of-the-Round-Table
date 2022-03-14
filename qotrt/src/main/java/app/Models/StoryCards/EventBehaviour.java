package app.Models.StoryCards;
import java.util.ArrayList;

import app.Models.General.*;


public interface EventBehaviour {

    void playEvent(ArrayList<Player> players, Player drawer);
  
}