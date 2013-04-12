/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coolmonopoly.player;

import java.io.Serializable;

/**
 *
 * @author Disastorm
 */
public class Point implements Serializable{
    private float x;
    private float y;

    public Point(){
        
    }

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }
}
