/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.gui;

import coolmonopoly.util.Utility;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.theme.ITheme;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamableException;

/**
 *
 * @author Disastorm
 */
public class GuiConstants {
    public final static char c = java.io.File.separatorChar;    //animations

    public static ImageFont boldFont = null;
    public static ITheme qtCurve = null;
    static{
        try {
            qtCurve = new XMLTheme("data" + c + "themes" + c + "QTCurve" + c + "QTCurve.xml");
        } catch (IOException ex) {
            Logger.getLogger(GuiConstants.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IXMLStreamableException ex) {
            Logger.getLogger(GuiConstants.class.getName()).log(Level.SEVERE, null, ex);
        }
        boldFont = Utility.createAntiAliasedFont();
    }
}
