/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.board;

import coolmonopoly.Player;
import coolmonopoly.util.Utility;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.ITexture;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.decorator.IDecorator;
import org.fenggui.decorator.background.PixmapBackground;

/**
 *
 * @author Disastorm
 */
public class Property extends Tile{
    private PropertyGroup group;
    private Player owner;
    private int houseLevel=0;
    private int houseCost=0;
    private int price;
    private Map<Integer,Integer> rent;
    private IDecorator cardImage;
    
    public Property(String name, int price, PropertyGroup group, int housePrice, int rent, int rent1, int rent2, int rent3, int rent4, int rent5){
        owner=null;
        this.name=name;
        this.price=price;
        this.group=group;
        this.rent = new HashMap<Integer,Integer>();
        this.rent.put(Integer.valueOf(0), rent);
        this.rent.put(Integer.valueOf(1), rent1);
        this.rent.put(Integer.valueOf(2), rent2);
        this.rent.put(Integer.valueOf(3), rent3);
        this.rent.put(Integer.valueOf(4), rent4);
        this.rent.put(Integer.valueOf(5), rent5);
        loadPlaceholderCardImage();
    }
    
    private void loadPlaceholderCardImage(){
        cardImage = Utility.getDecorator("data/cards/card.jpg");
    }
    
    public int getPrice(){
        return price;
    }
    
    public int getRent(){
        return rent.get(Integer.valueOf(houseLevel));
    }
    
    public Player getOwner(){
        return owner;
    }
    public void setOwner(Player owner){
        this.owner=owner;
    }

    @Override
    public TileType getType() {
        return TileType.PROPERTY;
    }

    /**
     * @return the cardImage
     */
    public IDecorator getCardImage() {
        return cardImage;
    }

    /**
     * @param cardImage the cardImage to set
     */
    public void setCardImage(IDecorator cardImage) {
        this.cardImage = cardImage;
    }
}
