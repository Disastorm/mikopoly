/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.board;

/**
 *
 * @author Disastorm
 */
public class GenericTile extends Tile{
    private TileType type;
    public GenericTile(TileType type){
        this.type=type;
    }
    @Override
    public TileType getType() {
        return type;
    }
    
}
