package app.Models.RankCards;

import app.Models.General.Card;

public class Rank extends Card {
    private int battlePoints;

    public Rank(String name, int battlePts){
        this.name = name;
        this.battlePoints = battlePts;
    }

    public int getBattlePoints(){
        return this.battlePoints;
    }
}
