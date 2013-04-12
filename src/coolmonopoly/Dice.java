/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly;

import java.util.Random;

/**
 *
 * @author Disastorm
 */
public class Dice {
    private static Random rand = new Random();
    public static int rollOne(){
        return rand.nextInt(6)+1;
    }
}
