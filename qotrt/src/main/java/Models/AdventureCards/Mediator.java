package Models.AdventureCards;

public interface Mediator {
  Player registerPlayer(Player player);
  Player removePlayer(Player player);
  void initializeCards();
  void start();
  //void displayDiscardedCards();
  String getCurrentTurnName();
  AdventureCard getLastCard();
}