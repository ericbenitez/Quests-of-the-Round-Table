package Models.General;
import java.util.ArrayList;

import Models.AdventureCards.AdventureCard;

public class Turn {

  Game game;

  boolean isQuest = false;
  boolean isTournament = false;
  boolean isEvent = false;

  ArrayList<Player> participants;
  ArrayList<AdventureCard> discardedCards;


  public Turn(Game g){
    this.participants = new ArrayList<Player>();
    this.discardedCards = new ArrayList<AdventureCard>();
    this.game = g;
  }



  public drawStoryCard(){
    


  }



  

}