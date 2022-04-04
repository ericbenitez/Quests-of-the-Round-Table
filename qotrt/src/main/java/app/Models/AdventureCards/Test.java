package app.Models.AdventureCards;
import java.util.ArrayList;

import app.Models.Enums.AdventureCardType;


public class Test extends AdventureCard {
  protected int minBid;
  // Game game;
  protected ArrayList<String> bids = new ArrayList<String>();
  // protected int lastBid = 0;

  //two constructors, one without the min Bid which will set it to a default value of 0
  public Test(String name){
    this.name = name;
    this.description = "";
    this.minBid = 0;
    this.adventureCardType = AdventureCardType.Test;
  }

 
  public Test(String name, int minBid){
    this.name = name;
    this.description = "";
    this.minBid = minBid;
    this.adventureCardType = AdventureCardType.Test;
  }
  
  public int getMinBid(){
    return minBid;
  }
  public void addBid(ArrayList<String> playerBids){
    this.bids = playerBids;
  }
  public int getLastBid(){
    return bids.size();
  }
  public ArrayList<String> getBids(){
    return bids;
  }


  @Override
  public int getBattlePoints() {
    // TODO Auto-generated method stub
    return 0;
  }

}