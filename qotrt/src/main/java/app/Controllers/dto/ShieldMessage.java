package app.Controllers.dto;

import lombok.Data;

@Data
public class ShieldMessage {
    int shields;
    int playerId;

    public int getShields() {
        return shields;
    }

    public int getPlayerId(){
        return playerId;
    }
}

