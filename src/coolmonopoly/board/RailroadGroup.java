/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.board;

import coolmonopoly.Player;

/**
 *
 * @author Disastorm
 */
public class RailroadGroup {
    private Railroad[] railroads;
     
     public void setRailroads(Railroad[] railroads){
         this.railroads=railroads;
     }
     
     public int getNumberOwned(Player player){
         int count=0;
         for(Railroad railroad : railroads){
             if(railroad.getOwner()==player){
                 count++;
             }
         }
         return count;
     }
}
