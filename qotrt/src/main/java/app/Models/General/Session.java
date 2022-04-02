package app.Models.General;
import app.Models.StoryCards.StoryCard;

import java.util.ArrayList;

import app.Models.AdventureCards.*;

public class Session {
/*****
 * We need a session that stores the current game status.
 * the currentActive player,
 * the current quest/tournament/event
 * current stage 
 * the sponsor
 * the participants
 * the bids
 * 
 * 
 */

   public int currentActivePlayer;
   public StoryCard currentStoryCard;
//    public int currentStage;
   // public int sponsorId;
   // public ArrayList<Integer> participantsId;
   public boolean questInPlay;
   public boolean testInPlay; //comes from the Quest.java 
   public Test testCard;
   public ArrayList<Integer> winners;
}
   
 
    
