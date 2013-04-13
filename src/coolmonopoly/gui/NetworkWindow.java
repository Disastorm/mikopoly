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

import coolmonopoly.CoolMonopoly;
import org.fenggui.Button;
import org.fenggui.Container;
import org.fenggui.FengGUI;
import org.fenggui.TextEditor;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.FormAttachment;
import org.fenggui.layout.FormData;
import org.fenggui.layout.FormLayout;
import org.fenggui.util.Spacing;
import org.fenggui.composite.Window;

/**
 * Window that contains TextFields to enter an address, port, user name and
 * password. It also has a label to notify the user about something.
 *
 * @author Johannes Schaback, last edtir by $Author: marcmenghin $, $Date:
 * 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 *
 */
public class NetworkWindow extends Window {

    private Container loginContainer;
    private Button buyButton, auctionButton;
    private TextEditor numberOfPlayers;
    public final static int WIDTH = 400;
    public final static int HEIGHT = 250;
    
    public NetworkWindow(boolean closeBtn) {
        super(closeBtn, false, false, false);//the first parameter is the close Button
        // FIXME: change this soon
        FengGUI.setTheme(GuiConstants.qtCurve);
        this.setupTheme(NetworkWindow.class);
        setSize(WIDTH, HEIGHT);
        ((Container) getContentContainer()).setLayoutManager(new FormLayout());
        setTitle("Host or Join a Game");

        FormData fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        fd.top = new FormAttachment(100, 0);
        
        FengGUI.createLabel(getContentContainer(), "IP:");
       // buyButton.getAppearance().setMargin(new Spacing(2, 2));


        buyButton = FengGUI.createButton(getContentContainer(), "Wait");

        auctionButton = FengGUI.createButton(getContentContainer(), "Connect");
        
        numberOfPlayers = FengGUI.createTextField(getContentContainer(),"2");


//    statusLabel.getAppearance().setAlignment(Alignment.MIDDLE);


        fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(50, 0);
        fd.bottom = new FormAttachment(50, -60);
        fd.top=new FormAttachment(75, -60);
        buyButton.setLayoutData(fd);


        fd = new FormData();
        fd.left = new FormAttachment(50, 0);
        fd.right = new FormAttachment(100, 0);
        fd.bottom = new FormAttachment(50, -60);
        fd.top=new FormAttachment(75, -60);
        auctionButton.setLayoutData(fd);
        
        fd = new FormData();
        fd.left = new FormAttachment(25, 0);
        fd.right = new FormAttachment(40, 0);
        fd.bottom = new FormAttachment(50, 0);
        fd.top=new FormAttachment(75, 0);
        numberOfPlayers.setLayoutData(fd);

        buyButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                int numPlayers;
                try{
                    numPlayers = Integer.parseInt(numberOfPlayers.getText().trim());
                }catch(NumberFormatException ex){
                    ex.printStackTrace();
                    return;
                }
                close();
                CoolMonopoly.setupHost(numPlayers);
            }
        });

        auctionButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                close();
                CoolMonopoly.setupClient("127.0.0.1");
            }
        });

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

}
