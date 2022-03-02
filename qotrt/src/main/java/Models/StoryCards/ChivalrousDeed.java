package Models.StoryCards;
import java.util.ArrayList;
import Models.Enums.Rank;

import Models.General.*;

public class ChivalrousDeed implements EventBehaviour {
   
   
    public void playEvent(ArrayList<Player> players, Player drawer) {

        Player minRanked = players.get(0);

        Player current = null;

        Rank lowestRankValue = Rank.Squire;
 
        for(int i = 0; i < players.size(); i++){

            current = players.get(i);            
            if(current.getRankInt().compareTo(lowestRankValue) == -1){
                minRanked = current;
            } 

        }

        Player minShielded = players.get(0);

        for(int i = 1; i < players.size(); i++){

            current = players.get(i);            
            if(current.getNumShields() < minShielded.getNumShields()){
                minShielded = current;
            } 

        }

        if(minShielded.equals(minRanked)){
           minShielded.updateShields(3);

        }else{
            minShielded.updateShields(3);
            minRanked.updateShields(3);
        }
         
       
    }  
        
    

  
}