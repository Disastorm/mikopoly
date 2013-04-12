/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (c) 2005-2009 FengGUI Project
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details:
 * http://www.gnu.org/copyleft/lesser.html#TOC3
 * 
 * $Id: ConnectionWindow.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package coolmonopoly.gui;

import coolmonopoly.Player;
import coolmonopoly.board.Property;
import coolmonopoly.board.Tile;
import coolmonopoly.board.TileType;
import coolmonopoly.util.Utility;
import java.util.List;
import java.util.*;
import org.fenggui.*;
import org.fenggui.layout.FormAttachment;
import org.fenggui.layout.FormData;
import org.fenggui.layout.FormLayout;
import org.fenggui.composite.Window;
import org.fenggui.decorator.IDecorator;
import org.fenggui.util.Color;

/**
 * Window that contains TextFields to enter an address, port, user name and
 * password. It also has a label to notify the user about something.
 *
 * @author Johannes Schaback, last edtir by $Author: marcmenghin $, $Date:
 * 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 *
 */
public class PlayerStatsWindow extends Window {

    private Container loginContainer;
    public final static int WIDTH = 300;
    public final static int HEIGHT = 100;
    private Player player;
    private Label balance;
    private List<Label> properties = new LinkedList<Label>();
    private Set<IDecorator> loadedPropertyImages = new HashSet<IDecorator>();

    public PlayerStatsWindow(Player player, boolean closeBtn) {
        super(closeBtn, false, false, false);//the first parameter is the close Button
        // FIXME: change this soon
        FengGUI.setTheme(GuiConstants.qtCurve);
        this.setupTheme(PlayerStatsWindow.class);
        this.player=player;

        setTitle(player.getName());
        setSize(WIDTH, HEIGHT);
        ((Container) getContentContainer()).setLayoutManager(new FormLayout());

        FormData fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        fd.top = new FormAttachment(100, 0);
        
        balance = new Label();
        GuiConstants.qtCurve.setUp(balance);
        Utility.setFontToDefaultStyle(balance.getAppearance(), GuiConstants.boldFont, Color.BLACK);
        this.getContentContainer().addWidget(balance);        
               
        fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        fd.bottom = new FormAttachment(25, 0);
        fd.top=new FormAttachment(0, 0);
        balance.setLayoutData(fd);


        

        fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        fd.bottom = new FormAttachment(0, 0);
   //     buySellButton.setLayoutData(fd);

        refresh();
        
        layout();
    }

    public Container getLoginContainer() {
        return loginContainer;
    }

    @Override
    public void close() {
    /*    if (charSelect != null) {
            charSelect.close();
        } */
        super.close();
    }
    
    public final void refresh(){
        balance.setText("Cash: $"+player.getMoney());
        List<Tile> owned = Utility.getPlayerOwnedProperties(player);
        List<IDecorator> pixmaps = new LinkedList<IDecorator>();
        for(Tile tile : owned){
            if(tile.getType().equals(TileType.PROPERTY)){
                pixmaps.add(((Property)tile).getCardImage());
            }
        }        
        
        int top=25;
        int bottom=25;
        int offset=0;
        for(IDecorator pix : pixmaps){
            offset-=10;
            if(loadedPropertyImages.contains(pix)){
                continue;
            }            
            Label label = new Label();
           GuiConstants.qtCurve.setUp(label);
           
           this.getContentContainer().addWidget(label);
           loadedPropertyImages.add(pix);
           
           label.getAppearance().add(pixmaps.get(0));
   //        label.setSize(50,50);
        
            FormData fd = new FormData();
            fd.left = new FormAttachment(0, 0);
            fd.right = new FormAttachment(20, 0);
            fd.top=new FormAttachment(top, offset);
            fd.bottom = new FormAttachment(bottom, offset);
            label.setLayoutData(fd);            
        }
        layout();
   }
    
    public boolean containsWidget(Widget w, Iterable<IWidget> iterable) {
        Iterator<IWidget> it = iterable.iterator();
        if(w==null)return false;
        while(it.hasNext()){
            IWidget widget = it.next();
            if(widget.equals(w))return true;
        }
        return false;
    }

}
