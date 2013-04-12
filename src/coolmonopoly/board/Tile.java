/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.board;

/**
 *
 * @author Disastorm
 */
public abstract class Tile {
    protected String name;
    public String getName(){
        return name;
    }
    public abstract TileType getType();
}
