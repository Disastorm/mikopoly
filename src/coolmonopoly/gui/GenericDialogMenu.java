/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.gui;

import coolmonopoly.util.Utility;
import java.util.Iterator;
import org.fenggui.Display;
import org.fenggui.composite.Window;
import org.fenggui.*;

/**
 *
 * @author Disastorm
 */
public class GenericDialogMenu {

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
    public GenericDialogWindow getWindow() {
        return cw1;
    }
    Window filesFrame = null;
    Display display;
    private static GenericDialogWindow cw1;
    Canvas canvas;
    private static Label background = null;
    private static Container contain = new Container();

    public GenericDialogMenu() {
    }

    private void buildFrame1(String msg) {
        cw1 = new GenericDialogWindow(msg, true);
        display.addWidget(cw1);
        Utility.centerWidgetOnScreen(display, cw1, TurnWindow.WIDTH, TurnWindow.HEIGHT);
        cw1.layout();
    }

    public void close() {
        cw1.close();
    }

    public void buildGUI(String msg, Display g) {
        display = g;
        if (cw1 == null) {
            buildFrame1(msg);
        } else {
            cw1.setText(msg);
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
        GenericDialogMenu.background = background;
    }
}
