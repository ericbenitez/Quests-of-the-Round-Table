package app.Models.StoryCards;

import app.Models.General.Game;
import app.Models.General.Player;

public class Tournament extends StoryCard {

  private int bonusShields;

  public Tournament(String name, int shields, Game game){

    this.name = name;
    this.bonusShields = shields;

  }


  @Override
  protected void draw(Player player) {
    // TODO Auto-generated method stub
    
  }

  public int getBonusShields(){
    return this.bonusShields;
  }




  

}