/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import org.fenggui.FengGUI;
import org.fenggui.actor.ScreenshotActor;
import org.fenggui.binding.render.lwjgl.EventHelper;
import org.fenggui.binding.render.lwjgl.LWJGLBinding;
import org.fenggui.example.Everything;
import org.fenggui.example.ExampleBasisLWJGL;
import org.fenggui.theme.DefaultTheme;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;


/**
 *
 * @author Disastorm
 */
public class TestFengGui{
    org.fenggui.Display     desk           = null;
  private int             lastButtonDown = -1;
  private ScreenshotActor screenshotActor;

  public static void main(String[] args)
  {
  //    System.err.println("fired mouseMoved");
    new TestFengGui().execute();
    System.exit(0);
  }

  private void readBufferedKeyboard()
  {

    //check keys, buffered
    Keyboard.poll();

    while (Keyboard.next())
    {
      if (Keyboard.getEventKeyState()) // if pressed
      {
        desk.fireKeyPressedEvent(EventHelper.mapKeyChar(), EventHelper.mapEventKey());

        //	    		 XXX: dirty hack to make TextEditor usable again on LWJGL. This needs to be solved nicer in the future!
        desk.fireKeyTypedEvent(EventHelper.mapKeyChar());
      }
      else
      {
        desk.fireKeyReleasedEvent(EventHelper.mapKeyChar(), EventHelper.mapEventKey());
      }
    }

  }

  /**
   * reads a mouse in buffered mode
   */
  private void readBufferedMouse()
  {
    int x = Mouse.getX();
    int y = Mouse.getY();
//    System.err.println(x + " " + y);

    boolean hitGUI = false;

    // @todo the click count is not considered in LWJGL! #

    if (lastButtonDown != -1 && Mouse.isButtonDown(lastButtonDown))
    {
      hitGUI |= desk.fireMouseDraggedEvent(x, y, EventHelper.getMouseButton(lastButtonDown), 1);
    }
    else
    {
      if (Mouse.getDX() != 0 || Mouse.getDY() != 0){
        hitGUI |= desk.fireMouseMovedEvent(x, y, EventHelper.getMouseButton(lastButtonDown), 1);
      }
      if (lastButtonDown != -1)
      {
        desk.fireMouseClickEvent(x, y, EventHelper.getMouseButton(lastButtonDown), 1);
        hitGUI |= desk.fireMouseReleasedEvent(x, y, EventHelper.getMouseButton(lastButtonDown), 1);
                              System.out.println("fireMouseReleasedEvent="+hitGUI + " " + x + " " + y);

        lastButtonDown = -1;
      }
      while (Mouse.next())
      {
   //       System.err.println(Mouse.getEventButton());
        if (Mouse.getEventButton() != -1 && Mouse.getEventButtonState())
        {
          lastButtonDown = Mouse.getEventButton();
          hitGUI |= desk.fireMousePressedEvent(x, y, EventHelper.getMouseButton(lastButtonDown), 1);
                      System.out.println("hitgui="+hitGUI + " " + x + " " + y + " " + EventHelper.getMouseButton(lastButtonDown));

        }
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0)
        {
          hitGUI |= desk.fireMouseWheel(x, y, wheel > 0, 1, 1);
        }
      }
    }
  }

  /**
   * 
   */
  public void execute()
  {
      FengGUI.setTheme(new DefaultTheme());
    try
    {
      initEverything();
    }
    catch (LWJGLException le)
    {
      le.printStackTrace();
      System.out.println("Failed to initialize Gears.");
      return;
    }

    mainLoop();

    destroy();
  }

  /**
   * 
   */
  private void destroy()
  {
    Display.destroy();
  }

  private void mainLoop()
  {
    while (!Display.isCloseRequested())
    {

      readBufferedKeyboard();
      readBufferedMouse();

      glRender();
      Display.update();
    }
  }

  /**
   * 
   */
  private void glRender()
  {
    GL11.glLoadIdentity();
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

    GLU.gluLookAt(10, 8, 8, 0, 0, 0, 0, 0, 1);

    // GL11.glRotatef(rotAngle, 1f, 1f, 1);
    //  GL11.glColor3f(0.42f, 0.134f, 0.44f);

    // rotAngle += 0.5;

    // draw GUI stuff
    desk.display();

    screenshotActor.renderToDos(desk.getBinding().getOpenGL(), desk.getWidth(), desk.getHeight());

    // hmm, i think we need to query the mouse pointer and
    // keyboard here and call the according
    // desk.mouseMoved, desk.keyPressed, etc.
    // methods...

  }

  /**
   * 
   */
  public final void initEverything() throws LWJGLException
  {
    Display.setDisplayMode(new DisplayMode(800, 600));
    Display.setTitle("Gears");
    Display.create();

    glInit(800, 600);

    // initialize keyboard
    Keyboard.create();

    // build the gui
    buildGUI();

    screenshotActor = new ScreenshotActor();
    screenshotActor.hook(desk);
  }

  public void buildGUI()
  {
    // init. the LWJGL Binding
    LWJGLBinding binding = new LWJGLBinding();

    // init the root Widget, that spans the whole
    // screen (i.e. the OpenGL context within the
    // Microsoft XP Window)
    desk = new org.fenggui.Display(binding);
    // build a simple test FengGUI-Window
    Everything everything = new Everything();
    everything.buildGUI(desk);

//    gui.GLChatBox chatbox = new gui.GLChatBox(null);
//    chatbox.buildGUI(desk);
    
//   LoginMenu login = new LoginMenu(null);
//    login.buildGUI(desk);




//   CharSelectionMenu charSelect = new CharSelectionMenu(null, new java.util.ArrayList<game.networking.pojo.NetUser>());
////   charSelect.buildGUI(desk);
 //   FengGUI.setTheme(new DefaultTheme());
//   game.gui.DialogueMenu dialogue = new game.gui.DialogueMenu(Interact.instance.getInteraction("Miku"), "Miku");
       
//   dialogue.buildGUI(desk);

 //       ConnectionMenu conn = new ConnectionMenu(null, "asdf");
 //       conn.buildGUI(desk);
   
//    BasicStatsMenu menu = new BasicStatsMenu();
 //   menu.buildGUI(desk);
    
    
  }

  private void glInit(int width, int height)
  {
    // Go into orthographic projection mode.
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GLU.gluOrtho2D(0, width, 0, height);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity();
    GL11.glViewport(0, 0, width, height);

    //set clear color to ... ugly
    GL11.glClearColor(0.1f, 0.5f, 0.2f, 0.0f);
    //sync frame (only works on windows )   => THAT IS NOT TRUE (ask Rainer)
    Display.setVSyncEnabled(false);
  }
}
