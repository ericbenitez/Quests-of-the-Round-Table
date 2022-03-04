package Models.General;
import java.util.ArrayList;

import Models.AdventureCards.AdventureCard;
import Models.StoryCards.*;


public class Turn {
  String name = "";
  
  Game game;

  StoryCard storyCard;

  ArrayList<Player> participants;
  ArrayList<AdventureCard> discardedCards;


  public Turn(){
    this.participants = new ArrayList<Player>();
    this.discardedCards = new ArrayList<AdventureCard>();
    
  }


   public Turn(Game g, StoryCard s){
    this.participants = new ArrayList<Player>();
    this.discardedCards = new ArrayList<AdventureCard>();
    this.game = g;
    this.storyCard = s;
    
  }
  
  public String getName() {
    return this.name;
  }


  /*
  public void decideTurn(){
     if (this.storyCard instanceof Quest ){
       //

     }else if (this.storyCard instanceof Tournament){


     }else {
       this.storyCard.playEvent(game.getPlayers(), game.getCurrentActivePlayer())
     }






  }
  */

  /*
  public drawStoryCard(){
    


  }
  1) draw random card from the deck

  2) if card is Quest -> Generate quest
          ask everyone in row to sponsor
          if sponsor says yes
               generate stages 
               ask everyone else to participate in sposored Quest ( build participant list)
               players place cards: No armours of the same kind, one amour card per Quest
               reveal the results
               determine winners and award the shields based on number of stages
               players draw addditional cards

     if card is Turnament  -> Generate turnament
           ask everyone in row to participate: (build participant list) 
           players place cards
           reveal the results
           determine winner/winners and award shields = bonus shields + # of participants
           players draw additional cards


              

     if card is Event  -> playEvent

  3) Place story card in discarded storyCards






  */



  

}