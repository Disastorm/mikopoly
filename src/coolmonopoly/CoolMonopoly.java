/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly;

import coolmonopoly.board.*;
import coolmonopoly.cards.Chance;
import coolmonopoly.cards.CommunityChest;
import coolmonopoly.gui.*;
import coolmonopoly.player.PieceType;
import coolmonopoly.player.PlayerPiece;
import coolmonopoly.util.Utility;
import java.awt.Font;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.NetworkManager;
import org.fenggui.binding.render.lwjgl.EventHelper;
import org.fenggui.binding.render.lwjgl.LWJGLBinding;
import org.fenggui.event.mouse.MouseButton;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 *
 * @author Disastorm
 */
public class CoolMonopoly extends BasicGame {

    /**
     * Settings *
     */
    private static AppGameContainer app;
    public static int height = 768;
    public static int width = 1024;
    private static boolean fullscreen = false;
    private static boolean showFPS = false;
    private static String title = "Vocalopoly";
    private static int fpslimit = 60;
    private static int lastRoll = 0;
    /**
     * Host properties *
     */
    private static Map<Socket, Player> otherPlayers = null;
    private static List<Player> playOrder;
    private static int playOrderIndex = -1;
    /**
     * Client properties *
     */
    private static Player host = null;
    /**
     * Common properties *
     */
    private static List<Player> otherPlayersList = null; //this includes the host as well
    private static Player player;
    private static boolean myTurn = false;
    private static boolean rolled = false;
    private static Board board;
    /**
     * GUI variables *
     */
    private static Font font;
    private static UnicodeFont ufont;
    private static boolean processingLMBPress = false;
    private static Map<PieceType, PlayerPiece> selectablePlayerPieces = new HashMap<PieceType, PlayerPiece>();
    private static final Set<FloatingText> floatingText = Collections.synchronizedSet(new HashSet<FloatingText>());

    public CoolMonopoly() {
        super(title);
    }

    /**
     * Host methods *
     */
    public static void setupHost(int numPlayers) {
        otherPlayers = new HashMap<Socket, Player>();
        try {
            ServerSocket server = new ServerSocket(10052);
            for (int i = 0; i < numPlayers - 1; i++) {
                System.out.println("waiting for " + (numPlayers - 1 - i) + "connections...");
                Socket sock = server.accept();

                Player oPlayer = new Player(sock, null);
                otherPlayers.put(sock, oPlayer);
            }
        } catch (IOException ex) {
            Logger.getLogger(CoolMonopoly.class.getName()).log(Level.SEVERE, null, ex);
        }

        otherPlayersList = new LinkedList<Player>(otherPlayers.values());
        player = new Player(null, null);
        //TODO: set hosts preffered piece here
        Map<PieceType, Boolean> pieceTaken = new HashMap<PieceType, Boolean>();
        pieceTaken.put(player.getPiece().getBody(), true);
        for (Player oPlayer : otherPlayersList) {
            List<PieceType> preferred = NetworkManager.receivePreferredPieceTypes(oPlayer);
            for (PieceType type : preferred) {
                if (pieceTaken.get(type) == null) {
                    oPlayer.setPiece(getPlayerPiece(type));
                    pieceTaken.put(oPlayer.getPiece().getBody(), true);
                    break;
                }
            }
        }
        playOrder = determinePlayOrder();
        Player currentTurnPlayer = advanceTurnOnHost();
        if (currentTurnPlayer == player) {
            myTurn = true;
            rolled = false;
        }

        board = new Board();
        List<Player> allPlayers = new LinkedList<Player>(otherPlayersList);
        allPlayers.add(player);
        playerStatsMenu.buildGUI(allPlayers, desk);
        for (Player oPlayer : otherPlayersList) {
            NetworkManager.sendPlayerInit(oPlayer, player, otherPlayersList);
            NetworkManager.notifyTurn(oPlayer, currentTurnPlayer);
            NetworkManager.mainLoop(oPlayer, true);
        }

        if (myTurn) {
            setupMyTurn();
        }
    }
    private static List<PieceType> debugList = new LinkedList<PieceType>();

    static {
        debugList.add(PieceType.MIKU);
        debugList.add(PieceType.RIN);
    }

    public static void setupClient(String ip) {
        otherPlayers = new HashMap<Socket, Player>();
        try {
            Socket sock = new Socket(ip, 10052);
            host = new Player(sock, null);
            NetworkManager.sendPreferredPieceTypesToHost(host, debugList);
            NetworkManager.readPlayerInit(host);
            //at this point player and otherPlayersList should be set up
            System.out.println("piece=" + player.getPiece().getBody());
        } catch (IOException ex) {
            Logger.getLogger(CoolMonopoly.class.getName()).log(Level.SEVERE, null, ex);
        }
        board = new Board();
        List<Player> allPlayers = new LinkedList<Player>(otherPlayersList);
        allPlayers.add(player);
        playerStatsMenu.buildGUI(allPlayers, desk);
        NetworkManager.mainLoop(host, false);
    }

    public static Player advanceTurnOnHost() {
        playOrderIndex++;
        if (playOrderIndex == playOrder.size()) {
            playOrderIndex = 0;
        }
        return playOrder.get(playOrderIndex);
    }

    public static void displayCurrentPlayerTurn(Player turnPlayer) {
    }

    public static boolean isHost() {
        return host == null;
    }

    public static List<Player> determinePlayOrder() {
        TreeSet<Player> set = new TreeSet<Player>(new TurnOrderComparator());
        set.add(player);
        for (Player oPlayer : otherPlayers.values()) {
            set.add(oPlayer);
        }
        return new LinkedList<Player>(set);
    }

    /**
     * End Host Methods *
     */
    public static void setupPlayers(Player player, List<Player> otherPlayersList) {
        CoolMonopoly.player = player;
        CoolMonopoly.otherPlayersList = otherPlayersList;
    }

    public static void beginTurn() {
        try {
            Player currentTurn = playOrder.get(playOrderIndex);

            if (currentTurn == player) {
                setupMyTurn();
            } else {
                //notify player of their turn                
                NetworkManager.notifyTurn(currentTurn, currentTurn);
                //wait for result of their turn, and then send to everyone else

            }
            if (playOrderIndex == playOrder.size() - 1) {
                playOrderIndex = 0;
            } else {
                playOrderIndex++;
            }
        } finally {
            if (host != null) {
                try {
                    host.getSocket().close();
                } catch (IOException ex) {
                    Logger.getLogger(CoolMonopoly.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                for (Player oPlayer : otherPlayers.values()) {
                    try {
                        oPlayer.getSocket().close();
                    } catch (IOException ex) {
                        Logger.getLogger(CoolMonopoly.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public static void setupMyTurn() {
        turnMenu.buildGUI(desk);
    }

    public static void doTurn(Player player) {
        rollAndMove(player);
        System.out.println("Player: " + player + " is now on tile: " + board.getTile(player.getCurrentTile()).getName());
    }

    public static void rollAndMove(Player player) {
        int dice1 = Dice.rollOne();
        int dice2 = Dice.rollOne();

        System.out.println("dice1=" + dice1);
        System.out.println("dice2=" + dice2);

        rolled = true;
        movePlayer(player, dice1 + dice2);
        for (Player oPlayer : otherPlayersList) {
            NetworkManager.sendPlayerMove(oPlayer, player, dice1, dice2);
        }
    }

    public static void movePlayer(Player player, int numOfTiles) {
        player.setMoving(true);
        int newTileIndex = player.getCurrentTile() + numOfTiles;
        if (newTileIndex > 39) {
            newTileIndex -= 40;
            //passed go
            player.setMoney(player.getMoney() + 200);
            CoolMonopoly.displayFloatingTextOnPlayerStats("+$200", player, 4000);
        }
        player.movePiece(numOfTiles);
        player.setCurrentTile(newTileIndex);
    }

    public static void handleTile(Player player, boolean isMyPlayer) {
        Tile tile = board.getTile(player.getCurrentTile());
        switch (tile.getType()) {
            case PROPERTY:
                Property prop = (Property) tile;
                if (prop.getOwner() != null) {
                    Utility.handlePayment(prop.getOwner(), player, prop.getRent());
                } else {
                    boolean ableToBuy = false;
                    if (isMyPlayer) {
                        ableToBuy = true;
                    }
                    buyPropertyMenu.buildGUI(desk, prop, ableToBuy);
                }
                break;
            case RAILROAD:
                Railroad rail = (Railroad) tile;
                if (rail.getOwner() != null) {
                    Utility.handlePayment(rail.getOwner(), player, rail.getRent());
                } else{
                    boolean ableToBuy = false;
                    if (isMyPlayer) {
                        ableToBuy = true;
                    }
                    buyPropertyMenu.buildGUI(desk, rail, ableToBuy);
                }
                break;
            case UTILITY:
                UtilityProperty uProp = (UtilityProperty) tile;
                if (uProp.getOwner() != null) {
                    Utility.handlePayment(uProp.getOwner(), player, uProp.getRent(lastRoll));
                } else {
                    boolean ableToBuy = false;
                    if (isMyPlayer) {
                        ableToBuy = true;
                    }
                    buyPropertyMenu.buildGUI(desk, uProp, ableToBuy);
                }
                break;
            case CHANCE:
                if (isMyPlayer) {
                    if (!isHost()) {
                        NetworkManager.sendChanceDrawToHost(host, player);
                    } else {
                        Chance chance = board.drawChance();
                        for (Player oPlayer : getOtherPlayers()) {
                            NetworkManager.sendChanceDraw(oPlayer, player, chance.getType());
                        }
                        chanceCommChestMenu.buildGUI(desk, chance, null);
                        Chance.activate(chance.getType(), player);
                    }
                }
                break;
            case COMMUNITY_CHEST:
                if (isMyPlayer) {
                    if (!isHost()) {
                        NetworkManager.sendCommunityChestDrawToHost(host, player);
                    } else {
                        CommunityChest chest = board.drawCommunityChest();
                        for (Player oPlayer : getOtherPlayers()) {
                            NetworkManager.sendCommunityChestDraw(oPlayer, player, chest.getType());
                        }
                        chanceCommChestMenu.buildGUI(desk, null, chest);
                        CommunityChest.activate(chest.getType(), player);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void init(GameContainer gc) throws SlickException {
        initFengGui();
        //add all of the pieces here            
        Image spritesheet = new Image("data/pieces/charWalk.png", Color.white);
        PlayerPiece mikuPiece = new PlayerPiece(spritesheet, 50, 100, PieceType.MIKU);
        mikuPiece.setX(gc.getWidth() - 125);
        mikuPiece.setY(gc.getHeight() - 150 - Player.X_TILE_SIZE);
        mikuPiece.setName("null");
        selectablePlayerPieces.put(PieceType.MIKU, mikuPiece);
        PlayerPiece rinPiece = new PlayerPiece(spritesheet, 50, 100, PieceType.RIN);
        rinPiece.setX(gc.getWidth() - 125);
        rinPiece.setY(gc.getHeight() - 150 - Player.X_TILE_SIZE);
        rinPiece.setName("null");
        selectablePlayerPieces.put(PieceType.RIN, rinPiece);

        gc.getInput().enableKeyRepeat();
        gc.getInput().addKeyListener(new KeyHandler());
        gc.getInput().addMouseListener(new MouseHandler(gc));

        /**
         * Init Font *
         */
        font = new Font("Arial", Font.BOLD, 20);
        ufont = new UnicodeFont(font);
        ufont.addAsciiGlyphs();   //Add Glyphs
        ufont.addGlyphs(400, 600); //Add Glyphs
        ufont.getEffects().add(new ColorEffect(java.awt.Color.WHITE)); //Add Effects
        ufont.loadGlyphs();  //Load Glyphs

        /**
         * DEBUG TESTING - COMMENT OUT IN FINAL GAME*
         */
        /*
         * player = new Player(null,null); player.setPiece(mikuPiece); board =
         * new Board(); List<Player> plist = new LinkedList<Player>();
         * plist.add(player);
         */
        //      playerStatsMenu.buildGUI(plist, desk);        
    }

    private void processInput(GameContainer gc) {
        Input input = gc.getInput();
        /*
         * if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
         * System.out.println("testing123 "+input.getMouseX() + " " +
         * input.getMouseY()); hitGUI
         * |=desk.fireMousePressedEvent(input.getMouseX(),gc.getHeight()-input.getMouseY(),MouseButton.LEFT,1);
         * System.out.println("hitgui="+hitGUI); }
         */

        if (input.isKeyPressed(Input.KEY_ENTER)) {
        }

        if (input.isKeyPressed(Input.KEY_I)) {
            toggleTurnMenu();
        }
        if (input.isKeyPressed(Input.KEY_O)) {
            //         Interface.toggleInventory(root);
            if (networkMenu.isBuilt()) {
                networkMenu.unbuildGUI();
            } else {
                networkMenu.buildGUI(desk);
            }
        }
        if (input.isKeyPressed(Input.KEY_P)) {
            //         Interface.toggleInventory(root);
            if (chanceCommChestMenu.isBuilt()) {
                chanceCommChestMenu.unbuildGUI();
            } else {
                chanceCommChestMenu.buildGUI(desk, board.drawChance(), null);
            }
        }

        if (input.isKeyPressed(Input.KEY_P)) {
            //        Interface.toggleEquipment(root);
        }

    }

    public static void setMyTurn() {
        myTurn = true;
        rolled = false;
    }

    public static boolean isMyTurn() {
        return myTurn;
    }

    public static boolean getDiceRolled() {
        return rolled;
    }

    public static void endTurn() {
        myTurn = false;
        rolled = true;
        if (coolmonopoly.CoolMonopoly.isHost()) {
            Player turnPlayer = coolmonopoly.CoolMonopoly.advanceTurnOnHost();
            for (Player oPlayer : coolmonopoly.CoolMonopoly.getOtherPlayers()) {
                NetworkManager.notifyTurn(oPlayer, turnPlayer);
            }
        } else {
            NetworkManager.notifyEndTurn(coolmonopoly.CoolMonopoly.getHost());
        }
    }

    public static void showTurnMenu() {
        if (!turnMenu.isBuilt()) {
            turnMenu.buildGUI(desk);
        }
    }

    public static void showMessageDialog(String msg) {
        if (!genericDialogMenu.isBuilt()) {
            genericDialogMenu.buildGUI(msg, desk);
        } else {
            genericDialogMenu.getWindow().setText(msg);
        }
    }

    public static void showAuctionMenu(Tile property) {
    }

    public static void toggleTurnMenu() {
        if (turnMenu.isBuilt()) {
            turnMenu.unbuildGUI();
        } else {
            turnMenu.buildGUI(desk);
        }
    }

    public void update(GameContainer gc, int delta) throws SlickException {
        if (delta > 15) {
            delta = 15;
        }
        //   textField.setFocus(true);

        processInput(gc);

        if (board == null || player == null) {
            return;
        }
        if (!player.getPiece().translate(delta) && player.isMoving() == true) {
            player.setMoving(false);
            handleTile(player, true);
        }
        for (Player oPlayer : otherPlayersList) {
            if (!oPlayer.getPiece().translate(delta) && oPlayer.isMoving() == true) {
                oPlayer.setMoving(false);
                handleTile(oPlayer, false);
            }
        }

        /**
         * Update interface position *
         */
        //  limitTextField();
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        if (board != null && player != null) {
            board.render(325, 50, 0.75f);

            player.getPiece().draw(player.getPiece().getX(), player.getPiece().getY());

            for (Player oPlayer : otherPlayersList) {
                oPlayer.getPiece().draw(oPlayer.getPiece().getX(), oPlayer.getPiece().getY());
            }

            Iterator<FloatingText> fTextIt = floatingText.iterator();
            synchronized (floatingText) {
                while (fTextIt.hasNext()) {
                    FloatingText fText = fTextIt.next();
                    if (!fText.render()) {
                        fTextIt.remove();
                    }
                }
            }
        }

        /*
         * TODO: Interface Objects Iterator<InterfaceObject> it =
         * interfaceObjects.iterator(); while (it.hasNext()) { InterfaceObject
         * obj = it.next(); boolean success = obj.render(); if (!success) {
         * it.remove(); } }
         */

        /**
         * Draw Text *
         */
        //       int xOff = Util.getXOffsetForString(player, player.getName());
        //     ufont.drawString(player.getX() + xOff, player.getY() + Util.getYOffsetForPlayerName(), player.getName(), Color.white);
        desk.display();
    }
    public static org.fenggui.Display desk = null;
    public static GLChatBox chatbox = null;
    private static TurnMenu turnMenu = null;
    private static BuyPropertyMenu buyPropertyMenu = null;
    private static ChanceCommChestMenu chanceCommChestMenu = null;
    private static PlayerStatsMenu playerStatsMenu = null;
    private static NetworkMenu networkMenu = null;
    private static GenericDialogMenu genericDialogMenu = null;

    private void initFengGui() {
        //FENGGUI STUFF
        LWJGLBinding binding = new LWJGLBinding();

        // init the root Widget, that spans the whole
        // screen (i.e. the OpenGL context within the
        // Microsoft XP Window)
        desk = new org.fenggui.Display(binding);
        // build a simple test FengGUI-Window
        org.fenggui.example.Everything everything = new org.fenggui.example.Everything();
        everything.buildGUI(desk);

        /**
         * CONSTRUCT GUI OBJECTS HERE *
         */
        chatbox = new GLChatBox();
        chatbox.buildGUI(desk);
        turnMenu = new TurnMenu();
        buyPropertyMenu = new BuyPropertyMenu();
        chanceCommChestMenu = new ChanceCommChestMenu();
        playerStatsMenu = new PlayerStatsMenu();
        networkMenu = new NetworkMenu();
        genericDialogMenu = new GenericDialogMenu();

        //      basicStatsMenu = new BasicStatsMenu();
        //       basicStatsMenu.buildGUI(desk);

        //END FENGGUI STUFF
    }

    public static void toggleChanceCommChestDisplay(Chance chance, CommunityChest commChest) {
        if (chance != null) {
            chanceCommChestMenu.buildGUI(desk, chance, null);
        } else {
            chanceCommChestMenu.buildGUI(desk, null, commChest);
        }
    }

    public static void main(String[] args) throws SlickException {
        app = new AppGameContainer(new CoolMonopoly());
        app.setDisplayMode(width, height, fullscreen);
        app.setSmoothDeltas(true);
        app.setTargetFrameRate(fpslimit);
        app.setShowFPS(showFPS);
        app.setUpdateOnlyWhenVisible(false);
        app.start();

        //test code
        //    setup(4);
        //    doTurn(players.get(0));
    }

    public static Player getPlayer() {
        return player;
    }

    public static List<Player> getOtherPlayers() {
        return otherPlayersList;
    }

    public static Board getBoard() {
        return board;
    }

    public static GLChatBox getChatBox() {
        return chatbox;
    }

    public static PlayerStatsMenu getPlayerStatsMenu() {
        return playerStatsMenu;
    }

    public static Player getHost() {
        return host;
    }

    public static PlayerPiece getPlayerPiece(PieceType type) {
        return selectablePlayerPieces.get(type);
    }

    public static Player getPlayer(int publicId) {
        for (Player oPlayer : otherPlayersList) {
            if (oPlayer.getPublicId() == publicId) {
                return oPlayer;
            }
        }
        if (player.getPublicId() == publicId) {
            return player;
        }
        return null;
    }

    public static void setLastRoll(int lastRoll) {
        CoolMonopoly.lastRoll = lastRoll;
    }

    public static void displayFloatingText(String message, int x, int y, int duration) {
        floatingText.add(new FloatingText(x, y, message, ufont, duration));
    }

    public static void displayFloatingTextOnPlayerStats(String message, Player thePlayer, int duration) {
        CoolMonopoly.displayFloatingText(message, thePlayer.getStatsWindow().getX() + 50, height - (thePlayer.getStatsWindow().getY()), duration);
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyPressed(int i, char c) {
            //        System.out.println("keyPressed"+i+" " + c);
            desk.fireKeyPressedEvent(EventHelper.mapKeyChar(), EventHelper.mapEventKey());

            //XXX: dirty hack to make TextEditor usable again on LWJGL. This needs to be solved nicer in the future!
            boolean chatting = desk.fireKeyTypedEvent(EventHelper.mapKeyChar());
        }

        @Override
        public void keyReleased(int i, char c) {
            //       System.out.println("keyReleased"+i+" " + c);
        }

        @Override
        public void setInput(Input input) {
        }

        @Override
        public boolean isAcceptingInput() {
            return true;
        }

        @Override
        public void inputEnded() {
        }

        @Override
        public void inputStarted() {
        }
    }

    private class MouseHandler implements MouseListener {

        private int lastButtonDown = -1;
        private GameContainer gc;

        public MouseHandler(GameContainer gc) {
            this.gc = gc;
        }

        @Override
        public void mouseWheelMoved(int i) {
        }

        @Override
        public void mouseClicked(int button, int x, int y, int count) {
            y = gc.getHeight() - y;
            boolean hitGUI = false;

            hitGUI |= desk.fireMouseClickEvent(x, y, getMouseButton(button), count);
        }

        @Override
        public void mousePressed(int button, int x, int y) {
            //          System.err.println(x + " " + y);
            y = gc.getHeight() - y;
            boolean hitGUI = false;

            hitGUI |= desk.fireMousePressedEvent(x, y, getMouseButton(button), 1);
            //      if(hitGUI){
            //         player.movePiece(5);
            //    } 
        }

        @Override
        public void mouseReleased(int button, int x, int y) {
            y = gc.getHeight() - y;
            boolean hitGUI = false;

            hitGUI |= desk.fireMouseReleasedEvent(x, y, getMouseButton(button), 1);
        }

        @Override
        public void mouseMoved(int oldX, int oldY, int x, int y) {
            y = gc.getHeight() - y;
            boolean hitGUI = false;

            hitGUI |= desk.fireMouseMovedEvent(x, y, MouseButton.LEFT, 1);
        }

        @Override
        public void mouseDragged(int oldXi, int oldY, int x, int y) {
            y = gc.getHeight() - y;
            boolean hitGUI = false;
            MouseButton mButton = null;

            hitGUI |= desk.fireMouseDraggedEvent(x, y, MouseButton.LEFT, 1);
        }

        @Override
        public void setInput(Input input) {
        }

        @Override
        public boolean isAcceptingInput() {
            return true;
        }

        @Override
        public void inputEnded() {
        }

        @Override
        public void inputStarted() {
        }

        private MouseButton getMouseButton(int button) {
            MouseButton mButton = null;
            switch (button) {
                case Input.MOUSE_LEFT_BUTTON:
                    mButton = MouseButton.LEFT;
                    break;
                case Input.MOUSE_MIDDLE_BUTTON:
                    mButton = MouseButton.MIDDLE;
                    break;
                case Input.MOUSE_RIGHT_BUTTON:
                    mButton = MouseButton.RIGHT;
                    break;
            }
            return mButton;
        }
    }
}
