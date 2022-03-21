package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.Enums.StoryCardType;
import app.Models.General.Player;

public class EventCard extends StoryCard {
  public EventBehaviour eventBehaviour;
  
  public EventCard(EventBehaviour eventBehaviour, String name, String description) {
    this.eventBehaviour = eventBehaviour;
    this.name = name;
    this.description = description;
    this.storyCardType = StoryCardType.Event;
  }

  protected void setEventBehaviour(EventBehaviour e){
    this.eventBehaviour = e;
  }

  @Override
  protected void draw(Player player) {
    // TODO Auto-generated method stub
    
  }
  
  
  public void performEvent(ArrayList<Player> players, Player drawer) {
		eventBehaviour.playEvent(players, drawer);
	}
}
