package Models.AdventureCards;
import java.util.ArrayList;

public class Turn {
  String name;
  boolean isQuest = false;
  boolean isTournament = false;
  boolean isEvent = false;

  ArrayList<Player> participants;
  ArrayList<AdventureCard> discardedCards;


  public Turn(){
    this.participants = new ArrayList<Player>();
    this.discardedCards = new ArrayList<AdventureCard>();
  }
}