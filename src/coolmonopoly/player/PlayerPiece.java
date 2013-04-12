/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.player;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;

/**
 *
 * @author Disastorm
 */
public class PlayerPiece {

    protected String name;
    protected Point position;
    protected List<Point> destination;
    private PlayerImage image;
    protected Polygon polygon;
    private int movementXOffset;
    private int movementYOffset;
    protected boolean movingRight = false;
    protected boolean movingLeft = false;
    protected boolean movingUp = false;
    protected boolean movingDown = false;
    private PieceType body;

    public PlayerPiece(Image spritesheet, int cellWidth, int cellHeight, PieceType pieceType) {
        image = new PlayerImage(spritesheet, cellWidth, cellHeight);
        body = pieceType;
        position = new Point(0, 0);
        polygon = new Polygon(new float[]{0, cellHeight / 2, cellWidth, cellHeight / 2, cellWidth, cellHeight, 0, cellHeight});
        destination = new LinkedList<Point>();
        name = "";
  //      movementYOffset = cellHeight - 13;
  //      movementXOffset = cellWidth / 2;

    }

    public Image getStaticImage() {
        return image.getImage();
    }

    public Animation getAnimatedImage() {
        return image.getAnimation();
    }

    public void draw(float x, float y) {
        image.getAnimation().draw(x, y);
    }

    public void drawStaticImage(float x, float y, float scale) {
        image.getImage().draw(x, y, scale);
    }

    /**
     * @return the x
     */
    public float getX() {
        return position.getX();
    }

    /**
     * @param x the x to set
     */
    public synchronized void setX(float x) {
        position.setX(x);
    }

    /**
     * @return the y
     */
    public float getY() {
        return position.getY();
    }

    /**
     * @param y the y to set
     */
    public synchronized void setY(float y) {
        position.setY(y);
    }

    /**
     * @return the destination
     */
    public List<Point> getDestination() {
        return destination;
    }

    public synchronized void setDestination(List<Point> destination) {
        this.destination = destination;
    }
    
    public void addDestination(Point dest){
        destination.add(dest);
    }

    public void stopMoving(int moveX, int moveY) {
        destination.clear();
        if (moveX != 0) {
            position.setX(position.getX() + moveX);
            //splt the calls in the 2 if statements more efficient than calling synchPoly
            polygon.setX(position.getX());
        }
        if (moveY != 0) {
            position.setY(position.getY() + moveY);
            polygon.setY(position.getY() + image.getImage().getHeight() / 2);
        }
        image.moveIdle();
    }

    private void synchPoly() {
        polygon.setX(position.getX());
        polygon.setY(position.getY() + image.getImage().getHeight() / 2);
    }

    public boolean translate(int delta) {
        if (!this.destination.isEmpty()) {
            Point destination = this.destination.get(0);

            float xDiff = destination.getX() - (position.getX() + movementXOffset);
            float yDiff = destination.getY() - (position.getY() + movementYOffset);

            float maxVal = 2;
            float minVal = -2f;

            if (xDiff > maxVal) {
                xDiff = maxVal;
            } else if (xDiff < minVal) {
                xDiff = minVal;
            }
            if (yDiff > maxVal) {
                yDiff = maxVal;
            } else if (yDiff < minVal) {
                yDiff = minVal;
            }

            if (delta > 15) {
                delta = 15;
            }
            xDiff = xDiff / 15 * delta;
            yDiff = yDiff / 15 * delta;

            /** Check Movement Direction **/
            movingRight = false;
            movingLeft = false;
            movingUp = false;
            movingDown = false;

            boolean idle = true;
            if (xDiff > 0) {
                movingRight = true;
                image.moveRight();
                idle = false;
            } else if (xDiff < 0) {
                movingLeft = true;
                image.moveLeft();
                idle = false;
            }

            if (yDiff > 0) {
                movingDown = true;
                if (idle) {
                    idle = false;
                    image.move();
                }
            } else if (yDiff < 0) {
                movingUp = true;
                if (idle) {
                    idle = false;
                    image.move();
                }
            }
            if (idle) {
                image.moveIdle();
            }

            /** Set Positions **/
            position.setX(position.getX() + xDiff);
            position.setY(position.getY() + yDiff);
            synchPoly();

            if ((position.getX() + movementXOffset) == destination.getX() && (position.getY() + movementYOffset) == destination.getY()) {
                this.destination.remove(0);
                image.moveIdle();
            }
        return true;
        }
        return false;
    }

    public void speak(String text) {
  /*      int duration = 5000;
        if (text.length() > 50) {
            duration = 10000;
        }
        ChatBubble bubble = new ChatBubble(this, text, Game.getUfont(), duration);
        Game.getInterfaceObjects().add(bubble); */
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public synchronized void setName(String name) {
        this.name = name;
    }

    /**
     * @return the polygon
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * @param polygon the polygon to set
     */
    public synchronized void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    /**
     * @return the movingRight
     */
    public boolean isMovingRight() {
        return movingRight;
    }

    /**
     * @param movingRight the movingRight to set
     */
    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    /**
     * @return the movingLeft
     */
    public boolean isMovingLeft() {
        return movingLeft;
    }

    /**
     * @param movingLeft the movingLeft to set
     */
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    /**
     * @return the movingUp
     */
    public boolean isMovingUp() {
        return movingUp;
    }

    /**
     * @param movingUp the movingUp to set
     */
    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    /**
     * @return the movingDown
     */
    public boolean isMovingDown() {
        return movingDown;
    }

    /**
     * @param movingDown the movingDown to set
     */
    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    /**
     * @return the body
     */
    public PieceType getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public synchronized void setBody(PieceType body) {
        this.body = body;
    }

    /**
     * @return the movementXOffset
     */
    public int getMovementXOffset() {
        return movementXOffset;
    }

    /**
     * @return the movementYOffset
     */
    public int getMovementYOffset() {
        return movementYOffset;
    }

    /**
     * @return the image
     */
    public PlayerImage getImage() {
        return image;
    }
}
