package app.Controllers.dto;

import lombok.Data;

@Data
public class CreateGameRequest {
  private String player;
  private int numOfPlayers;

  public int getNumOfPlayers() {
    return numOfPlayers;
  }

  public String getPlayerName() {
    return player;
  }
}
