/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.gui;

import coolmonopoly.util.Ticker;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

/**
 *
 * @author Disastorm
 */
public class FloatingText{

    private String message;
    private UnicodeFont ufont;
    private Ticker ticker = null;
    private int x;
    private int y;

    public FloatingText(int x, int y, String text, UnicodeFont font, int displayTimeMs) {
        ufont = font;
        this.x=x;
        this.y=y;
        ticker = new Ticker(displayTimeMs);
        message = text;
    }

    public boolean render() {
        return render(x, y);
    }

    public boolean render(float x, float y) {
        if (ticker == null || ticker.getTicks() > 0) {
            ticker = null;
            return false;
        }

        ufont.drawString(x, y, message, Color.green);

        return true;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
