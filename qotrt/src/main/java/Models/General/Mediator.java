package Models.General;

import Models.AdventureCards.AdventureCard;

public interface Mediator {
  Player registerPlayer(Player player);
  Player removePlayer(Player player);
  void initializeCards();
  void start();
  //void displayDiscardedCards();
  String getCurrentTurnName();
  AdventureCard getLastCard();
}