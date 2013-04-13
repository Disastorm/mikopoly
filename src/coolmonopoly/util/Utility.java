/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.util;

import coolmonopoly.CoolMonopoly;
import coolmonopoly.Player;
import coolmonopoly.board.Property;
import coolmonopoly.board.Railroad;
import coolmonopoly.board.Tile;
import coolmonopoly.board.UtilityProperty;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fenggui.Display;
import org.fenggui.Widget;
import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.*;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.decorator.IDecorator;
import org.fenggui.decorator.background.PixmapBackground;
import org.fenggui.layout.FormAttachment;
import org.fenggui.layout.FormData;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.text.content.factory.simple.TextStyleEntry;
import org.fenggui.util.Alphabet;
import org.fenggui.util.Color;
import org.fenggui.util.fonttoolkit.FontFactory;

/**
 *
 * @author Disastorm
 */
public class Utility {

    public static void centerWidgetOnScreen(Display display, Widget widget, int widgetWidth, int widgetHeight) {
        //    widget.setWidth(widgetWidth);
        //    widget.setHeight(widgetHeight);

        widget.setX(display.getSize().getWidth() / 2 - (widgetWidth / 2));
        widget.setY(display.getSize().getHeight() / 2 - (widgetHeight / 2));


    }
    
    public static void setLayoutData(Widget widget, int xStartPercent, int xEndPercent, int yStartPercentFromBottom, int yScalePercent){
        FormData fd = new FormData();
        fd.left = new FormAttachment(xStartPercent, 0);
        fd.right = new FormAttachment(xEndPercent, 0);
        fd.bottom = new FormAttachment(yStartPercentFromBottom, 0);
        fd.top = new FormAttachment(100-yScalePercent, 0);
        widget.setLayoutData(fd);
    }
    
    public static void setLayoutData(Widget widget, int xStartPercent, int xStartOffset, int xEndPercent, int xEndOffset, int yStartPercentFromBottom, int yStartOffset, int yScalePercent){
        FormData fd = new FormData();
        fd.left = new FormAttachment(xStartPercent, xStartOffset);
        fd.right = new FormAttachment(xEndPercent, xEndOffset);
        fd.bottom = new FormAttachment(yStartPercentFromBottom, yStartOffset);
        fd.top = new FormAttachment(100-yScalePercent, 0);
        widget.setLayoutData(fd);
    }

    public static void setFontToDefaultStyle(TextAppearance appearance, IFont font, Color color) {
        TextStyle def = appearance.getStyle(TextStyle.DEFAULTSTYLEKEY);
        def.getTextStyleEntry(TextStyleEntry.DEFAULTSTYLESTATEKEY).setColor(color);

        ITextRenderer renderer = appearance.getRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY).copy();
        renderer.setFont(font);
        appearance.addRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY, renderer);
    }

    private static void setColorToDefaultStyle(TextAppearance appearance, Color color) {
        TextStyle def = appearance.getStyle(TextStyle.DEFAULTSTYLEKEY);
        def.getTextStyleEntry(TextStyleEntry.DEFAULTSTYLESTATEKEY).setColor(color);
    }

    public static ImageFont createAntiAliasedFont() {
        return FontFactory.renderStandardFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20), true, Alphabet.getDefaultAlphabet());
    }

    public static List<Tile> getPlayerOwnedProperties(Player player) {
        List<Tile> tiles = new LinkedList<Tile>();
        for (Tile tile : CoolMonopoly.getBoard().getTiles()) {
            switch (tile.getType()) {
                case PROPERTY:
                    if (((Property) tile).getOwner() == player) {
                        tiles.add(tile);
                    }
                    break;
                case RAILROAD:
                    if (((Railroad) tile).getOwner() == player) {
                        tiles.add(tile);
                    }
                    break;
                case UTILITY:
                    if (((UtilityProperty) tile).getOwner() == player) {
                        tiles.add(tile);
                    }
                    break;
                default:
                    break;
            }
        }
        return tiles;
    }

    public static void refreshPlayersStatsOnGui() {
        CoolMonopoly.getPlayerStatsMenu().refreshWindows();
    }

    public static void handlePayment(Player toPlayer, Player fromPlayer, int amount) {
        /**
         * show some animations *
         */
        CoolMonopoly.displayFloatingTextOnPlayerStats("+$" + amount, toPlayer, 4000);
        CoolMonopoly.displayFloatingTextOnPlayerStats("-$" + amount, fromPlayer, 4000);
        //TODO:

        toPlayer.setMoney(toPlayer.getMoney() + amount);
        fromPlayer.setMoney(fromPlayer.getMoney() - amount);
    }

    public static IDecorator getDecorator(String path) {
        ITexture tex = null;
        try {
            tex = Binding.getInstance().getTexture(path);
        } catch (IOException ex) {
            Logger.getLogger(Property.class.getName()).log(Level.SEVERE, null, ex);
        }
        Pixmap pixmap = new Pixmap(tex);
        return new PixmapBackground(pixmap, true);
    }
}
