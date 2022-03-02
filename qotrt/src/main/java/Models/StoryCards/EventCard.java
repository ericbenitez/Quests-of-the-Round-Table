package Models.StoryCards;

import java.util.ArrayList;

import Models.General.Player;

public class EventCard extends StoryCard {
  protected EventBehaviour eventBehaviour;
  
  public EventCard(EventBehaviour eventBehaviour, String name, String description) {
    this.eventBehaviour = eventBehaviour;
    this.name = name;
    this.description = description;
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
