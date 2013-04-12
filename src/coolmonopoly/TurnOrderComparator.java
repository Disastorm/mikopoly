/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly;

import java.util.Comparator;

/**
 *
 * @author Disastorm
 */
public class TurnOrderComparator implements Comparator<Player>{

    @Override
    public int compare(Player o1, Player o2) {
        return o1.getFirstRoll() - o2.getFirstRoll();
    }
     
}
