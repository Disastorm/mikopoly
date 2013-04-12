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

import coolmonopoly.util.Utility;
import org.fenggui.*;
import org.fenggui.layout.FormAttachment;
import org.fenggui.layout.FormData;
import org.fenggui.layout.FormLayout;
import org.fenggui.composite.Window;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
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
public class GenericDialogWindow extends Window {

    private Container loginContainer;
    public final static int WIDTH = 300;
    public final static int HEIGHT = 150;
    private Button okButton;
    private Label text;

    public GenericDialogWindow(String msg, boolean closeBtn) {
        super(closeBtn, false, false, false);//the first parameter is the close Button
        // FIXME: change this soon
        FengGUI.setTheme(GuiConstants.qtCurve);
        this.setupTheme(GenericDialogWindow.class);

        setTitle("Notification");
        setSize(WIDTH, HEIGHT);
        ((Container) getContentContainer()).setLayoutManager(new FormLayout());


        text = new Label(msg);
        GuiConstants.qtCurve.setUp(text);
        Utility.setFontToDefaultStyle(text.getAppearance(), GuiConstants.boldFont, Color.BLACK);
        this.getContentContainer().addWidget(text);
        Utility.setLayoutData(text, 0, 100, 45, 50);
        
        okButton = FengGUI.createButton(getContentContainer(), "OK");
        Utility.setLayoutData(okButton, 35, 55, 10, 20);
        okButton.addButtonPressedListener(new IButtonPressedListener() {
            public void buttonPressed(ButtonPressedEvent e) {
                close();
            }
        });
        layout();
    }

    public Container getLoginContainer() {
        return loginContainer;
    }

    public void setText(String msg) {
        text.setText(msg);
    }

    @Override
    public void close() {
        super.close();
    }
}
