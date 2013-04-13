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
import javax.swing.JOptionPane;
import network.NetworkManager;
import org.fenggui.*;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.FormAttachment;
import org.fenggui.layout.FormData;
import org.fenggui.layout.FormLayout;
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
public class TurnWindow extends Window {

    private Container loginContainer;
    private Button rollButton, tradeButton, buySellButton,endTurnButton;
    public final static int WIDTH = 300;
    public final static int HEIGHT = 100;

    public TurnWindow(boolean closeBtn) {
        super(closeBtn, false, false, false);//the first parameter is the close Button
        // FIXME: change this soon
        FengGUI.setTheme(GuiConstants.qtCurve);
        this.setupTheme(TurnWindow.class);


        setSize(WIDTH, HEIGHT);
        ((Container) getContentContainer()).setLayoutManager(new FormLayout());

        FormData fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        fd.top = new FormAttachment(100, 0);


        /*
         * passwordTextField.addKeyPressedListener(new IKeyPressedListener() {
         *
         * public void keyPressed(KeyPressedEvent keyPressedEvent) { if
         * (keyPressedEvent.getKeyClass() == Key.BACKSPACE) {
         *
         * if (passwordTextField.getText().length() != 0) {
         * passwordTextField.setText(passwordTextField.getText().substring(0,
         * passwordTextField.getText().length() - 1)); } } } });
         */


        rollButton = FengGUI.createButton(getContentContainer(), "Roll");

        tradeButton = FengGUI.createButton(getContentContainer(), "Trade with Player");

        buySellButton = FengGUI.createButton(getContentContainer(), "Buy/Sell/Mortgage Property");

        endTurnButton = FengGUI.createButton(getContentContainer(), "End Turn");

//    statusLabel.getAppearance().setAlignment(Alignment.MIDDLE);


        fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(50, 0);
        fd.bottom = new FormAttachment(25, 0);
        fd.top=new FormAttachment(0, 0);
        rollButton.setLayoutData(fd);


        fd = new FormData();
        fd.left = new FormAttachment(50, 0);
        fd.right = new FormAttachment(100, 0);
        fd.bottom = new FormAttachment(25, 0);
        fd.top=new FormAttachment(0, 0);
        tradeButton.setLayoutData(fd);

        fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(50, 0);
        fd.bottom = new FormAttachment(0, 0);
        buySellButton.setLayoutData(fd);
        
        fd = new FormData();
        fd.left = new FormAttachment(50, 0);
        fd.right = new FormAttachment(100, 0);
        fd.bottom = new FormAttachment(0, 0);
        endTurnButton.setLayoutData(fd);

        rollButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                if(coolmonopoly.CoolMonopoly.getDiceRolled()){
                    JOptionPane.showMessageDialog(null, "You already rolled!");
                    return;
                }
                close();
                coolmonopoly.CoolMonopoly.doTurn(coolmonopoly.CoolMonopoly.getPlayer());
            }
        });

        tradeButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                
            }
        });

        buySellButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                
            }
        });
        
        endTurnButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                if(!coolmonopoly.CoolMonopoly.getDiceRolled()){
                    JOptionPane.showMessageDialog(null, "You have not rolled yet!");
                    return;
                }
                coolmonopoly.CoolMonopoly.endTurn();
                close();
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
