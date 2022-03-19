package app.Controllers.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class CardsMessage {
    String playerId;
    ArrayList<String> cards;

    public String getPlayerId(){return playerId;}
    public ArrayList<String> getCards() {
        return cards;
    }

}
