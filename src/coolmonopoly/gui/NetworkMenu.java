/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.gui;

import coolmonopoly.board.Property;
import coolmonopoly.util.Utility;
import java.util.Iterator;
import org.fenggui.Display;
import org.fenggui.composite.Window;
import org.fenggui.*;

/**
 *
 * @author Disastorm
 */
public class NetworkMenu {

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
    public static NetworkWindow getWindow() {
        return cw;
    }

    Window filesFrame = null;
    Display display;
    private static NetworkWindow cw;
    Canvas canvas;
    private static Label background = null;
    private static Container contain = new Container();

    public NetworkMenu() {
    }

    private void buildFrame1() {
        cw = new NetworkWindow(false);
 //       cw.setX(display.getSize().getWidth() / 2 - 100);
 //       cw.setY(display.getSize().getHeight() / 2 - 75);
        Utility.centerWidgetOnScreen(display, cw, TurnWindow.WIDTH, TurnWindow.HEIGHT);
//    cw.setSize(200, 150);
//    cw.layout();
        display.addWidget(cw);
    }
    
    public void close(){
        cw.close();
    }

    public void buildGUI(Display g) {
        display = g;
        if(cw == null){
            buildFrame1();
        }else{
            display.addWidget(cw);
        }
    }

    public void unbuildGUI() {
        cw.close();
    }

    public boolean isBuilt() {
        if(cw==null)return false;
        Iterator<IWidget> it = display.getWidgets().iterator();
        while(it.hasNext()){
            IWidget widget = it.next();
            if(widget.equals(cw))return true;
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
        NetworkMenu.background = background;
    }
}
