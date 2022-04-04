package app.Models.StoryCards;
import java.util.ArrayList;

import app.Models.General.*;

public class Pox implements EventBehaviour {
   
   
    public String playEvent(ArrayList<Player> players, Player drawer) {

        for(int i = 0; i < players.size(); i++){
            if(!players.get(i).equals(drawer)){
                players.get(i).updateShields(-1);
            }
        }   
        return "All players except the player drawing this card lost 1 shield";
    }
}