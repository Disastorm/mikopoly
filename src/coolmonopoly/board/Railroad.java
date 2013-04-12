/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.board;

import coolmonopoly.Player;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Disastorm
 */
public class Railroad extends Tile{
    private RailroadGroup group;
    private Player owner;
    private int price;
    private Map<Integer,Integer> rent;
    public Railroad(String name, int price, RailroadGroup group, int rent1, int rent2, int rent3, int rent4){
        owner=null;
        this.name=name;
        this.price=price;
        this.group=group;
        this.rent = new HashMap<Integer,Integer>();
        this.rent.put(Integer.valueOf(1), rent1);
        this.rent.put(Integer.valueOf(2), rent2);
        this.rent.put(Integer.valueOf(3), rent3);
        this.rent.put(Integer.valueOf(4), rent4);
    }
    
    public int getPrice(){
        return price;
    }
    public int getRent(){
        return rent.get(Integer.valueOf(group.getNumberOwned(owner)));
    }
    
    public Player getOwner(){
        return owner;
    }
    public void setOwner(Player owner){
        this.owner=owner;
    }
    
    @Override
    public TileType getType() {
        return TileType.RAILROAD;
    }
}
