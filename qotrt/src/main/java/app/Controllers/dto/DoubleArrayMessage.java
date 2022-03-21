package app.Controllers.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class DoubleArrayMessage {
    ArrayList<ArrayList<String>> cards;

    public ArrayList<ArrayList<String>> getCards() {
        return cards;
    }

}
