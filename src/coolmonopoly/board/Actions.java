/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.board;

import coolmonopoly.CoolMonopoly;
import coolmonopoly.Player;
import coolmonopoly.util.Utility;
import network.NetworkManager;

/**
 *
 * @author Disastorm
 */
public class Actions {

    public static void buyProperty(Player player, Tile property) {
        int price = 0;
        switch (property.getType()) {
            case PROPERTY:
                Property prop = (Property) property;
                price = prop.getPrice();
                prop.setOwner(player);
                break;
            case RAILROAD:
                Railroad rail = (Railroad) property;
                price = rail.getPrice();
                rail.setOwner(player);
                break;
            case UTILITY:
                UtilityProperty util = (UtilityProperty) property;
                price = util.getPrice();
                util.setOwner(player);
                break;
            default:
                break;
        }
        player.setMoney(player.getMoney() - price);
        CoolMonopoly.displayFloatingTextOnPlayerStats("-$"+price, player, 4000);
        Utility.refreshPlayersStatsOnGui();
    }
    
    public static void beginAuction(Tile property){
        
    }
}
