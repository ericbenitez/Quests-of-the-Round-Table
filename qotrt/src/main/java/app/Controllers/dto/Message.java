package app.Controllers.dto;

import lombok.Data;

@Data
public class Message {
    String message = "";

    public String getMessage() {
        return message;
    }

    public int getMessageInt(){
        return Integer.parseInt(message);
    }
}
