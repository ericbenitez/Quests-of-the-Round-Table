package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.Enums.StoryCardType;
import app.Models.General.Game;
import app.Models.General.Player;

public class Tournament extends StoryCard {

  private int bonusShields;
  private ArrayList<Integer> participants;
  private int round; // can go up to 2 rounds (tie breaker)
  // the game class seems to store discarded cards all together, so I should add discarded cards
  // from tournaments there as well?

  public Tournament(String name, int shields){
    this.name = name;
    this.bonusShields = shields;
    this.storyCardType = StoryCardType.Tournament;
    participants = new ArrayList<>();
    round = 0; // will increase to 1 only when tournament starts
  }


  @Override
  protected void draw(Player player) {
    // TODO Auto-generated method stub
    
  }

  public int getBonusShields(){
    return this.bonusShields;
  }

  public void addParticipant(int playerId){
    participants.add(playerId);
  }

  public ArrayList<Integer> getParticipants(){
    return participants;
  }




  

}
