package app.Models.General;

import app.Service.GameService;

public abstract class Card {
  protected String name;
  protected String description;
  
  public String getName() {
    return this.name;
  }
  
  // @Autowired
  protected GameService gameService; //mediator ...

  
  @Override
  public String toString() {
    return this.name + "\n";
  }
}
