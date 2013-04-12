/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.util;

/**
 *
 * @author Disastorm
 */
public class Ticker implements Cloneable {

    private int rate;
    private long s2;

    public static long getTime() {
        return System.currentTimeMillis();
    }

    public Ticker(int tickrateMS) {
        rate = tickrateMS;
        s2 = Ticker.getTime();
    }

    public void reset(){
        s2 = Ticker.getTime();
    }

    public int getTicks() {
        long i = Ticker.getTime();
        if (i - s2 > rate) {
            int ticks = (int) ((i - s2) / (long) rate);
            s2 += (long) rate * ticks;
            return ticks;
        }
        return 0;
    }

    @Override
    public Ticker clone() throws CloneNotSupportedException {

        Ticker clone = (Ticker) super.clone();

        return clone;

    }
}
