/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.player;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Disastorm
 */
public class PlayerImage {

    private SpriteSheet spritesheet;
    private Animation animation;
    private Animation animationWalkRight;
    private Animation animationWalkLeft;
    private Animation animationIdleRight;
    private Animation animationIdleLeft;
    private boolean facingRight = true;

    public PlayerImage(Image image, int cellWidth, int cellHeight) {
        spritesheet = new SpriteSheet(image, cellWidth, cellHeight);

        animationWalkRight = new Animation();
        animationWalkRight.addFrame(spritesheet.getSprite(0, 0), 200);
        animationWalkRight.addFrame(spritesheet.getSprite(1, 0), 200);
        animationWalkRight.addFrame(spritesheet.getSprite(2, 0), 200);
        animationWalkRight.addFrame(spritesheet.getSprite(3, 0), 200);
        animationWalkRight.stop();

        animationWalkLeft = new Animation();
        animationWalkLeft.addFrame(spritesheet.getSprite(0, 0).getFlippedCopy(true, false), 200);
        animationWalkLeft.addFrame(spritesheet.getSprite(1, 0).getFlippedCopy(true, false), 200);
        animationWalkLeft.addFrame(spritesheet.getSprite(2, 0).getFlippedCopy(true, false), 200);
        animationWalkLeft.addFrame(spritesheet.getSprite(3, 0).getFlippedCopy(true, false), 200);
        animationWalkLeft.stop();

        animationIdleLeft = new Animation();
        animationIdleLeft.addFrame(spritesheet.getSprite(2, 0).getFlippedCopy(true, false), 200);
        animationIdleLeft.stop();

        animationIdleRight = new Animation();
        animationIdleRight.addFrame(spritesheet.getSprite(2, 0), 200);
        animationIdleRight.start();

        animation = animationIdleRight;

    }

    public synchronized void moveRight() {
        facingRight = true;
        animation.stop();
        animation = animationWalkRight;
        animation.start();
    }

    public synchronized void moveLeft() {
        facingRight = false;
        animation.stop();
        animation = animationWalkLeft;
        animation.start();
    }

    public synchronized void move() {
        animation.stop();
        if (facingRight) {
            animation = animationWalkRight;
        } else {
            animation = animationWalkLeft;
        }
        animation.start();
    }

    public synchronized void moveIdle() {
        animation.stop();
        if (facingRight) {
            animation = animationIdleRight;
        } else {
            animation = animationIdleLeft;
        }
        animation.start();
    }

    public Image getImage() {
        return spritesheet.getSprite(2, 0);
    }

    public synchronized Animation getAnimation() {
        return animation;
    }

    /**
     * @return the facingRight
     */
    public boolean isFacingRight() {
        return facingRight;
    }

    /**
     * @param facingRight the facingRight to set
     */
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
}
