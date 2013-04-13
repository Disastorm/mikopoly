/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.gui;

import coolmonopoly.board.Tile;
import coolmonopoly.util.Utility;
import java.util.Iterator;
import org.fenggui.Display;
import org.fenggui.composite.Window;
import org.fenggui.*;

/**
 *
 * @author Disastorm
 */
public class BuyPropertyMenu {

    /**
     * @return the contain
     */
    public static Container getContain() {
        return contain;
    }

    /**
     * @param aContain the contain to set
     */
    public static void setContain(Container aContain) {
        contain = aContain;
    }

    /**
     * @return the cw
     */
    public static BuyPropertyWindow getWindow() {
        return cw;
    }
    Window filesFrame = null;
    Display display;
    private static BuyPropertyWindow cw;
    Canvas canvas;
    private static Label background = null;
    private static Container contain = new Container();

    public BuyPropertyMenu() {
    }

    private void buildFrame1(Tile property) {
        cw = new BuyPropertyWindow(property, false);
        //       cw.setX(display.getSize().getWidth() / 2 - 100);
        //       cw.setY(display.getSize().getHeight() / 2 - 75);
        Utility.centerWidgetOnScreen(display, cw, TurnWindow.WIDTH, TurnWindow.HEIGHT);
//    cw.setSize(200, 150);
//    cw.layout();
        cw.setTitle(property.getName());
        display.addWidget(cw);
    }

    public void close() {
        cw.close();
    }

    public void buildGUI(Display g, Tile property, boolean ableToBuy) {
        display = g;
        if (cw == null) {
            buildFrame1(property);
        } else {
            cw.setTitle(property.getName());
            cw.setProperty(property);
            display.addWidget(cw);
        }
        if (ableToBuy) {
            cw.showButtons();
        } else {
            cw.hideButtons();
        }
    }

    public void unbuildGUI() {
        cw.close();
    }

    public boolean isBuilt() {
        if (cw == null) {
            return false;
        }
        Iterator<IWidget> it = display.getWidgets().iterator();
        while (it.hasNext()) {
            IWidget widget = it.next();
            if (widget.equals(cw)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the background
     */
    public static Label getBackground() {
        return background;
    }

    /**
     * @param background the background to set
     */
    public static void setBackground(Label background) {
        BuyPropertyMenu.background = background;
    }
}
