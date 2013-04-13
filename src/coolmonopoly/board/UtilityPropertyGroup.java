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
public class UtilityPropertyGroup {

    private UtilityProperty[] utilities;

    public void setUtilities(UtilityProperty[] utilities) {
        this.utilities = utilities;
    }

    public int getNumberOwned(Player player) {
        int count = 0;
        for (UtilityProperty utility : utilities) {
            if (utility.getOwner() == player) {
                count++;
            }
        }
        return count;
    }
}
