package app.Models.General;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import app.Models.AdventureCards.*;
import app.Models.StoryCards.*;
import app.Service.GameService;
import org.springframework.stereotype.Component;

@Component
public class Round {
  public String name;

  private GameService gameService;

  @Autowired
  public Round(GameService gameService) {
    this.gameService = gameService;
  }

  StoryCard storyCard; // each turn identified by story card..

  ArrayList<Player> participants; // for quests
  ArrayList<AdventureCard> discardedCards;

  public Round() {
    this.participants = new ArrayList<>();
    this.discardedCards = new ArrayList<>();
  }

  public void addParticipant(Player player) {
    participants.add(player);
  }

  public void withdrawParticipant(int playerId) {
    for (int i = 0; i < participants.size(); i++) {
      if (participants.get(i).getId() == playerId) {
        participants.remove(i);
        return;
      }
    }
  }

  public String getName() {
    return this.name;
  }

  public StoryCard setStoryCard(StoryCard storyCard) {
    this.storyCard = storyCard;
    return this.storyCard;
  }

  /*
   * public void decideTurn(){
   * if (this.storyCard instanceof Quest ){
   * //
   * 
   * }else if (this.storyCard instanceof Tournament){
   * 
   * 
   * }else {
   * this.storyCard.playEvent(game.getPlayers(), game.getCurrentActivePlayer())
   * }
   * 
   * 
   * 
   * 
   * 
   * 
   * }
   */

  /*
   * public drawStoryCard(){
   * 
   * 
   * 
   * }
   * 1) draw random card from the deck
   * 
   * 2) if card is Quest -> Generate quest
   * ask everyone in row to sponsor
   * if sponsor says yes
   * generate stages
   * ask everyone else to participate in sposored Quest ( build participant list)
   * players place cards: No armours of the same kind, one amour card per Quest
   * reveal the results
   * determine winners and award the shields based on number of stages
   * players draw addditional cards
   * 
   * if card is Turnament -> Generate turnament
   * ask everyone in row to participate: (build participant list)
   * players place cards
   * reveal the results
   * determine winner/winners and award shields = bonus shields + # of
   * participants
   * players draw additional cards
   * 
   * 
   * 
   * 
   * if card is Event -> playEvent
   * 
   * 3) Place story card in discarded storyCards
   * 
   * 
   * 
   * 
   * 
   * 
   */

}