package app.Controllers.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ArrayMessage {
    ArrayList<String> bids;

    public ArrayList<String> getBids() {
        return this.bids;
    }

}