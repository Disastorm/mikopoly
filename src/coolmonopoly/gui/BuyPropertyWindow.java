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
import coolmonopoly.Player;
import coolmonopoly.board.*;
import network.NetworkManager;
import org.fenggui.*;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.FormAttachment;
import org.fenggui.layout.FormData;
import org.fenggui.layout.FormLayout;
import org.fenggui.composite.Window;
import org.fenggui.decorator.IDecorator;

/**
 * Window that contains TextFields to enter an address, port, user name and
 * password. It also has a label to notify the user about something.
 *
 * @author Johannes Schaback, last edtir by $Author: marcmenghin $, $Date:
 * 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 *
 */
public class BuyPropertyWindow extends Window {

    private Container loginContainer;
    private Label card, priceLabel;
    private Button buyButton, auctionButton;
    public final static int WIDTH = 600;
    public final static int HEIGHT = 500;
    private Tile property;

    public void setProperty(Tile property) {
        this.property = property;
        IDecorator cardPic=null;
        int price=0;
        switch (property.getType()) {
            case PROPERTY:
                price = ((Property) property).getPrice();
                cardPic = ((Property) property).getCardImage();
                break;
            case RAILROAD:
                price = ((Railroad) property).getPrice();
                break;
            case UTILITY:
                price = ((UtilityProperty) property).getPrice();
                break;
            default:
                break;
        }
        priceLabel.setText("Price: $" + price);
        card.getAppearance().removeAll();
        if(cardPic != null){
            card.getAppearance().add(cardPic);
        }
    }

    public BuyPropertyWindow(Tile property, boolean closeBtn) {
        super(closeBtn, false, false, false);//the first parameter is the close Button
        this.property = property;
        // FIXME: change this soon
        FengGUI.setTheme(GuiConstants.qtCurve);
        this.setupTheme(BuyPropertyWindow.class);
        setSize(WIDTH, HEIGHT);
        ((Container) getContentContainer()).setLayoutManager(new FormLayout());

        int price = 0;
        IDecorator cardPic=null;
        switch (property.getType()) {
            case PROPERTY:
                price = ((Property) property).getPrice();
                 cardPic = ((Property) property).getCardImage();
                break;
            case RAILROAD:
                price = ((Railroad) property).getPrice();
                break;
            case UTILITY:
                price = ((UtilityProperty) property).getPrice();
                break;
            default:
                break;
        }

        FormData fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        fd.top = new FormAttachment(100, 0);

        priceLabel = FengGUI.createLabel(getContentContainer(), "Price: $" + price);
        // buyButton.getAppearance().setMargin(new Spacing(2, 2));


        buyButton = FengGUI.createButton(getContentContainer(), "Buy");

        auctionButton = FengGUI.createButton(getContentContainer(), "Auction");
        
        card = new Label();
        GuiConstants.qtCurve.setUp(card);

        this.getContentContainer().addWidget(card);
        if(cardPic != null){
            card.getAppearance().add(cardPic);
        }

        fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(50, 0);
        fd.bottom = new FormAttachment(10, 0);
        fd.top = new FormAttachment(90, 0);
        buyButton.setLayoutData(fd);


        fd = new FormData();
        fd.left = new FormAttachment(50, 0);
        fd.right = new FormAttachment(100, 0);
        fd.bottom = new FormAttachment(10, 0);
        fd.top = new FormAttachment(90, 0);
        auctionButton.setLayoutData(fd);
        
        
        fd = new FormData();
        fd.left = new FormAttachment(25, 0);
        fd.right = new FormAttachment(75, 0);
        fd.top=new FormAttachment(25, 0);
        fd.bottom = new FormAttachment(25, 0);
        card.setLayoutData(fd);        

        buyButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                close();
                Actions.buyProperty(CoolMonopoly.getPlayer(), BuyPropertyWindow.this.property);

                /**
                 * Send to Network *
                 */
                if (CoolMonopoly.isHost()) {
                    for (Player oPlayer : CoolMonopoly.getOtherPlayers()) {
                        NetworkManager.sendPlayerBuy(oPlayer, CoolMonopoly.getPlayer(), CoolMonopoly.getPlayer().getCurrentTile());
                    }
                } else {
                    NetworkManager.sendPlayerBuy(CoolMonopoly.getHost(), CoolMonopoly.getPlayer(), CoolMonopoly.getPlayer().getCurrentTile());
                }
                
                CoolMonopoly.toggleTurnMenu();
            }
        });

        auctionButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
            }
        });

        layout();
    }

    public Container getLoginContainer() {
        return loginContainer;
    }
    
    public void hideButtons(){
        buyButton.setVisible(false);
        auctionButton.setVisible(false);
    }
    
    public void showButtons(){
        buyButton.setVisible(true);
        auctionButton.setVisible(true);
    }

    @Override
    public void close() {
        /*
         * if (charSelect != null) { charSelect.close(); }
         */
        super.close();
    }
}
