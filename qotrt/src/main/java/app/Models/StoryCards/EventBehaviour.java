package app.Models.StoryCards;
import java.util.ArrayList;

import Models.General.*;
import app.Models.General.Player;


public interface EventBehaviour {

    void playEvent(ArrayList<Player> players, Player drawer);
  
}