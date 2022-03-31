package app.Models.AdventureCards;
import java.util.ArrayList;


public class Test extends AdventureCard {
  int minBid;
  // Game game;
  ArrayList<String> bids = new ArrayList<String>();
  int lastBid;

  //two constructors, one without the min Bid which will set it to a default value of 0
  public Test(String name){
    this.name = name;
    this.description = "";
    this.minBid = 0;
  }

 
  public Test(String name, int minBid){
    this.name = name;
    this.description = "";
    this.minBid = minBid;
  }
  
  public int getMinBid(){
    return minBid;
  }
  public void addBid(ArrayList<String> bids){
    this.bids = bids;
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