package Models.StoryCards;
import java.util.ArrayList;

import Models.General.*;

public class Pox implements EventBehaviour {
   
   
    public void playEvent(ArrayList<Player> players, Player drawer) {

        for(int i = 0; i < players.size(); i++){
            if(!players.get(i).equals(drawer)){
                players.get(i).updateShields(-1);
            }
        }   
        
    }

  
}