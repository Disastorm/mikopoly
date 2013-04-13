/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.gui;

import coolmonopoly.cards.Chance;
import coolmonopoly.cards.CommunityChest;
import coolmonopoly.util.Utility;
import java.util.Iterator;
import org.fenggui.Display;
import org.fenggui.composite.Window;
import org.fenggui.*;

/**
 *
 * @author Disastorm
 */
public class ChanceCommChestMenu {

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
    public static ChanceCommChestWindow getWindow() {
        return cw;
    }
    Window filesFrame = null;
    Display display;
    private static ChanceCommChestWindow cw;
    Canvas canvas;
    private static Label background = null;
    private static Container contain = new Container();

    public ChanceCommChestMenu() {
    }

    private void buildFrame1(Chance chance, CommunityChest cc) {
        cw = new ChanceCommChestWindow(chance, cc, false);
        //       cw.setX(display.getSize().getWidth() / 2 - 100);
        //       cw.setY(display.getSize().getHeight() / 2 - 75);
        Utility.centerWidgetOnScreen(display, cw, TurnWindow.WIDTH, TurnWindow.HEIGHT);
//    cw.setSize(200, 150);
//    cw.layout();
  //     cw.setTitle();
        display.addWidget(cw);
    }

    public void close() {
        cw.close();
    }

    public void buildGUI(Display g, Chance chance, CommunityChest cc) {
        display = g;
        if (cw == null) {
            buildFrame1(chance, cc);
        } else {
            cw.setChance(chance);
            cw.setCommunityChest(cc);
            display.addWidget(cw);
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
        ChanceCommChestMenu.background = background;
    }
}
