/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.gui;

import coolmonopoly.Player;
import java.util.Collection;
import java.util.Iterator;
import org.fenggui.Display;
import org.fenggui.composite.Window;
import org.fenggui.*;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.util.Color;

/**
 *
 * @author Disastorm
 */
public class PlayerStatsMenu {

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
    public PlayerStatsWindow getWindow() {
        return cw1;
    }

    public void refreshWindows() {
        if (cw1 != null) {
            refreshWindow(cw1);
        }
        if (cw2 != null) {
            refreshWindow(cw2);
        }
        if (cw3 != null) {
            refreshWindow(cw3);
        }
        if (cw4 != null) {
            refreshWindow(cw4);
        }
    }

    private static void refreshWindow(PlayerStatsWindow cw) {
        cw.refresh();
    }
    Window filesFrame = null;
    Display display;
    private static PlayerStatsWindow cw1, cw2, cw3, cw4;
    Canvas canvas;
    private static Label background = null;
    private static Container contain = new Container();

    public PlayerStatsMenu() {
    }

    private void buildFrame1(Collection<Player> players) {
        for (Player player : players) {
            PlayerStatsWindow cw = getAndCreateNextStatusWindow(player);
            player.setStatsWindow(cw);
            display.addWidget(cw);
            cw.setX(10);
            cw.setY(display.getSize().getHeight() / 2 - 75);
            cw.layout();

            cw.getTitleBar().getAppearance().add(new PlainBackground(Color.DARK_GREEN));
            cw.setTitle("" + player.getPublicId());
            //      Utility.centerWidgetOnScreen(display, cw1, TurnWindow.WIDTH, TurnWindow.HEIGHT);
        }

//    cw.setSize(200, 150);
//    cw.layout();
//        cw.setTitle("Menu");

    }

    private PlayerStatsWindow getAndCreateNextStatusWindow(Player player) {
        if (cw1 == null) {
            cw1 = new PlayerStatsWindow(player, false);
            return cw1;
        } else if (cw2 == null) {
            cw2 = new PlayerStatsWindow(player, false);
            return cw2;
        } else if (cw3 == null) {
            cw3 = new PlayerStatsWindow(player, false);
            return cw3;
        } else if (cw4 == null) {
            cw4 = new PlayerStatsWindow(player, false);
            return cw4;
        }
        return null;
    }

    public void close() {
        cw1.close();
    }

    public void buildGUI(Collection<Player> players, Display g) {
        display = g;
        if (cw1 == null) {
            buildFrame1(players);
        } else {
            display.addWidget(cw1);
        }
    }

    public void unbuildGUI() {
        cw1.close();
    }

    public boolean isBuilt() {
        if (cw1 == null) {
            return false;
        }
        Iterator<IWidget> it = display.getWidgets().iterator();
        while (it.hasNext()) {
            IWidget widget = it.next();
            if (widget.equals(cw1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the background
     */
    public Label getBackground() {
        return background;
    }

    /**
     * @param background the background to set
     */
    public void setBackground(Label background) {
        PlayerStatsMenu.background = background;
    }
}
