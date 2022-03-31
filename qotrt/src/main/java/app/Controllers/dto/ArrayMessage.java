package app.Controllers.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ArrayMessage {
    ArrayList<String> cards;

    public ArrayList<String> getCards() {
        return cards;
    }

}