package app.Models.StoryCards;

import java.util.ArrayList;
import java.util.HashMap;

import app.Models.AdventureCards.Amour;
import app.Models.Enums.StoryCardType;

public class Tournament extends StoryCard {

  private int bonusShields;
  private ArrayList<Integer> participants;
  // the game class seems to store discarded cards all together, so I should add discarded cards
  // from tournaments there as well?
  private int originalNumParticipants;
  private HashMap<Integer, ArrayList<String>> playerCards;
  private int firstParticipantId;
  private int lastParticipantId;
  private boolean tieOccurred; // a tie occurred
  private boolean tieBreakerPlayed; // a tie HAS been played already
  private int round;

  public Tournament(String name, int shields){
    this.name = name;
    this.bonusShields = shields;
    this.storyCardType = StoryCardType.Tournament;
    this.participants = new ArrayList<>();
    this.originalNumParticipants = 0;
    this.playerCards = new HashMap<>();
    this.tieOccurred = false;
    this.tieBreakerPlayed = false;
    this.round = 0; // increments when we loop back to the first participant (in game controller)
  }


  public void resetRound(ArrayList<Integer> participantsRemaining){
    this.participants = participantsRemaining;
    this.playerCards.clear();
  }

  public int getBonusShields(){
    return this.bonusShields;
  }

  public void addParticipant(int playerId){
    if(participants.size()==0){
      firstParticipantId = playerId;
    }
    participants.add(playerId);
  }
  public int getFirstParticipantId(){
    return firstParticipantId;
  }
  public ArrayList<Integer> getParticipants(){
    return participants;
  }

  public int getParticipantSize(){
    return participants.size();
  }

  public void setOriginalNumParticipants(int num){
    originalNumParticipants = num;
  }


  // shields given for 1 player tournament
  public int getAutoAwardSinglePlayer(){
    return 1 + this.bonusShields;
  }

  // shields given for a normal (1 round OR no ties) tournament 
  public int getAward(){
    return this.originalNumParticipants + this.bonusShields;
  }

  // shields given for round 2 (tie breaker, but tie didn't break)
  public int getAwardTie(){
    return this.originalNumParticipants;
  }



  // returns a list of card (strings) given the player id
  public ArrayList<String> getPlayerCards(int playerId){
    for (int i: playerCards.keySet()){
      if (i == playerId){
        return playerCards.get(i);
      }
    }
    return null;
  }

  public HashMap<Integer, ArrayList<String>> getAllPlayerCards(){
    return playerCards;
  }

  
  // adding the cards the player will place
  // new update: returns true if its the last participant
  public boolean addPlacedCards(int playerId, ArrayList<String> cardsToAdd){
    if (playerCards.keySet().contains(playerId)){ 
      playerCards.remove(playerId);
    }
    playerCards.put(playerId, cardsToAdd);

    // check if everyone is ready
    if (allReady()){
      setOriginalNumParticipants(playerCards.keySet().size());
      lastParticipantId = playerId;
      return true;
      // in the session, the client side can see if everyone is ready, 
                            // if so display all the cards, and send request to calc+display winner(s), or maybe 
                            // if client sees everyone is ready, it sends a request to calc winner(s), and when we get that back, we can display cards + winner etc
                            // or if we are letting the players bid one after the other like in quests, we can just figure that out in the turns
    }

    return false;
  }

  // help function: checks if all participants have bid (ready for bid)
  public boolean allReady(){
    for (int playerId : participants){
      if (!(playerCards.keySet().contains(playerId))){
        return false;
      }
    }
    return true;
  }

  public boolean getTieBreakerPlayed(){
      return tieBreakerPlayed;
  }

  public void setTieBreakerPlayed(Boolean bool){
    tieBreakerPlayed = bool;
  }

  public void playingTieBreaker(){
      tieBreakerPlayed = true;
  }

  public boolean getTieOccured(){
      return tieOccurred;
  }

  public void setTieOccurred(Boolean tieValue){
      this.tieOccurred = tieValue;
  }

  public int getLastParticipantId(){
      return lastParticipantId;
  }

  public int incrementRound(){
      round++;
      return round;
  }
  

}
