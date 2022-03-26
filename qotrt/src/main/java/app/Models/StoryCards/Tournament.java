package app.Models.StoryCards;

import app.Models.Enums.StoryCardType;
import app.Models.General.Game;

public class Tournament extends StoryCard {

  private int bonusShields;

  public Tournament(String name, int shields, Game game){

    this.name = name;
    this.bonusShields = shields;
    this.storyCardType = StoryCardType.Tournament;
  }

  public int getBonusShields(){
    return this.bonusShields;
  }
}
