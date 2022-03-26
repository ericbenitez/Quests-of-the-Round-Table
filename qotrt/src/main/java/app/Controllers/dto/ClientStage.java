package app.Controllers.dto;

import lombok.Data;

@Data
public class ClientStage {
    String message = "";

    public String getMessage() {
        return message;
    }

    public int getMessageInt(){
        return Integer.parseInt(message);
    }
}
