/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.gui;

/**
 *
 * @author Disastorm
 */
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
 * Created on Jul 15, 2005
 * $Id: TextAreaExample.java 621 2009-04-13 12:46:43Z marcmenghin $
 */
import coolmonopoly.CoolMonopoly;
import coolmonopoly.Player;
import network.NetworkManager;
import org.fenggui.ScrollContainer;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.event.key.KeyTypedEvent;
import org.fenggui.example.IExample;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.TextEditor;
import org.fenggui.composite.Window;
import org.fenggui.event.key.IKeyListener;
import org.fenggui.event.key.Key;
import org.fenggui.layout.RowExLayout;
import org.fenggui.layout.RowExLayoutData;

/**
 * Displays a text area with a rather stupid text.
 * @author Johannes Schaback ($Author: marcmenghin $)
 */
public class GLChatBox implements IExample {

    private Window filesFrame = null;
    private Display display;

    TextEditor tf;
    TextEditor textArea;
    public GLChatBox(){

    }
    private void buildFileFrame() {
     //   FengGUI.setTheme(new DefaultTheme());//set old style rather than XP style
        FengGUI.setTheme(GuiConstants.qtCurve);
        filesFrame = FengGUI.createDialog(display, "Chat Window");
        filesFrame.setX(20);
        filesFrame.setY(50);
        filesFrame.setSize(300, 150);
        

   //     GLTextField field = new GLTextField();
   //     field.buildGUI(display);

        filesFrame.getContentContainer().setLayoutManager(new RowExLayout(false));

        ScrollContainer sc = FengGUI.createScrollContainer(filesFrame.getContentContainer());
        sc.setLayoutData(new RowExLayoutData(true, true));

        textArea = FengGUI.createTextEditor(sc);

        final String text = "";
        textArea.setText(text);
        textArea.setReadonly(true);
        textArea.setWordWarping(true);
        textArea.setEnabled(false);

   //     Container container = FengGUI.createContainer(filesFrame.getContentContainer());
   //     container.setLayoutManager(new RowExLayout(true));
  //     container.setLayoutData(new RowExLayoutData(true, false));

//        Container container2 = FengGUI.createContainer(filesFrame.getContentContainer());
 //       container2.setLayoutManager(new RowExLayout(true));
 //       container2.setLayoutData(new RowExLayoutData(true, false));

   //     btn.setLayoutData(new RowExLayoutData(true, true));
                
        tf = FengGUI.createTextEditor(filesFrame.getContentContainer());
         //   tf.setEmptyText("Enter Some Text Here...");
            tf.setLayoutData(new RowExLayoutData(true, false));
            tf.setSize(200, 26);
            tf.setMultiline(false);
            tf.setWordWarping(false);
            tf.setUnicodeRestrict(false);
            tf.setHeight(10);

            tf.addKeyListener((new IKeyListener() {
            
                public void keyPressed(KeyPressedEvent e) {
                                
                    
                }

                public void keyReleased(KeyReleasedEvent e) {
               //     System.err.println(arg0);
                //    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void keyTyped(KeyTypedEvent e) {
                if(e.isPressed(Key.ENTER)){
                        
                     if(!tf.getText().equals("\n")){   
                    textArea.addContentAtEnd(CoolMonopoly.getPlayer().getName()+": "+tf.getText());//only append when received back from server            
                    
                    if(CoolMonopoly.isHost()){
                        for(Player oPlayer : CoolMonopoly.getOtherPlayers()){
                            NetworkManager.sendChat(oPlayer, CoolMonopoly.getPlayer().getName()+": "+tf.getText());
                        }
                    }else{
                        NetworkManager.sendChat(CoolMonopoly.getHost(), CoolMonopoly.getPlayer().getName()+": "+tf.getText());
                    }
                    tf.setText("");
                     }
                    
                    }else if(e.isPressed(Key.BACKSPACE)){
                        
                    }
                }
            }));
/*
        Button btn2 = FengGUI.createWidget(Button.class);
        btn2.setText("add to end");
        btn2.setLayoutData(new RowExLayoutData(true, true));
        btn2.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                textArea.addContentAtEnd("New Text At end\neven with a second line here");
            }
        }); */

       

   //     container2.addWidget(tf);


        filesFrame.layout();
    }

    public void buildGUI(Display g) {
        display = g;
        //		buildOtherFrame(display);
        buildFileFrame();
    }

    public String getExampleName() {
        return "Text Area Example";
    }

    public String getExampleDescription() {
        return "Text Area Example";
    }
    
    public void setFocused(boolean focused){
        tf.setFocused(focused);
    }
    
    public boolean isFocused(){
        return tf.isFocused();
    }

    public synchronized void append(String s){
        textArea.addContentAtEnd(s);        
    }
}
