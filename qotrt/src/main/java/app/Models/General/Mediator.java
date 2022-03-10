package app.Models.General;

import app.Models.AdventureCards.AdventureCard;

public interface Mediator {
  Player registerPlayer(Player player);
  Player removePlayer(Player player);
  void initializeCards();
  String getCurrentTurnName();
  AdventureCard getLastCard();
}