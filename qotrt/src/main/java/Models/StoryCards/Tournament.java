package Models.StoryCards;

import Models.General.Player;

public class Tournament extends StoryCard {

  private int bonusShields;

  public Tournament(String name, int shields){

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
