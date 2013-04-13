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
import coolmonopoly.cards.Chance;
import coolmonopoly.cards.CommunityChest;
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
public class ChanceCommChestWindow extends Window {

    private Container loginContainer;
    private Label card;
    private Button okButton;
    public final static int WIDTH = 600;
    public final static int HEIGHT = 500;
    private Chance chance;
    private CommunityChest communityChest;

    public void setChance(Chance chance) {
        this.chance = chance;
        if (chance != null) {
            IDecorator cardPic = this.chance.getCardImage();
            if (cardPic != null) {
                card.getAppearance().removeAll();
                card.getAppearance().add(cardPic);
            }
        }
    }

    public void setCommunityChest(CommunityChest communityChest) {
        this.communityChest = communityChest;
        if (communityChest != null) {
            IDecorator cardPic = this.communityChest.getCardImage();
            if (cardPic != null) {
                card.getAppearance().removeAll();
                card.getAppearance().add(cardPic);
            }
        }
    }

    public ChanceCommChestWindow(Chance chance, CommunityChest communityChest, boolean closeBtn) {
        super(closeBtn, false, false, false);//the first parameter is the close Button
        this.chance = chance;
        this.communityChest = communityChest;
        // FIXME: change this soon
        FengGUI.setTheme(GuiConstants.qtCurve);
        this.setupTheme(ChanceCommChestWindow.class);
        setSize(WIDTH, HEIGHT);
        ((Container) getContentContainer()).setLayoutManager(new FormLayout());
        FormData fd = new FormData();
        okButton = FengGUI.createButton(getContentContainer(), "OK");
        card = new Label();
        GuiConstants.qtCurve.setUp(card);

        IDecorator cardPic;
        if (chance != null) {
            cardPic = this.chance.getCardImage();
        } else {
            cardPic = this.communityChest.getCardImage();
        }
        this.getContentContainer().addWidget(card);
        if (cardPic != null) {
            card.getAppearance().add(cardPic);
        }

        fd = new FormData();
        fd.left = new FormAttachment(25, 0);
        fd.right = new FormAttachment(75, 0);
        fd.bottom = new FormAttachment(10, 0);
        fd.top = new FormAttachment(90, 0);
        okButton.setLayoutData(fd);

        fd = new FormData();
        fd.left = new FormAttachment(05, 0);
        fd.right = new FormAttachment(95, 0);
        fd.top = new FormAttachment(25, 0);//this is basically the vertical size, 25 means 100-25=75%
        fd.bottom = new FormAttachment(25, 0);//this is offset from the bottom of the window
        card.setLayoutData(fd);

        okButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                close();
                if (CoolMonopoly.isMyTurn()) {
                    CoolMonopoly.showTurnMenu();
                }
            }
        });
        layout();
    }

    public Container getLoginContainer() {
        return loginContainer;
    }

    @Override
    public void close() {
        /*
         * if (charSelect != null) { charSelect.close(); }
         */
        super.close();
    }
}
