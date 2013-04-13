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
public class UtilityProperty extends Tile{
    private UtilityPropertyGroup group;
    private Player owner;
    private int price;
    private Map<Integer,Integer> rent;
    public UtilityProperty(String name, int price, UtilityPropertyGroup group, int multiple1, int multiple2){
        owner=null;
        this.name=name;
        this.price=price;
        this.group=group;
        this.rent = new HashMap<Integer,Integer>();
        this.rent.put(Integer.valueOf(1), multiple1);
        this.rent.put(Integer.valueOf(2), multiple2);
    }
    
    public int getPrice(){
        return price;
    }
    public int getRent(int roll){
        return rent.get(Integer.valueOf(group.getNumberOwned(owner)))*roll;
    }
    
    public Player getOwner(){
        return owner;
    }
    public void setOwner(Player owner){
        this.owner=owner;
    }
    
    @Override
    public TileType getType() {
        return TileType.UTILITY;
    }
}
