/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly;

import coolmonopoly.gui.PlayerStatsWindow;
import coolmonopoly.player.PieceType;
import coolmonopoly.player.PlayerPiece;
import coolmonopoly.player.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.NetworkManager;

/**
 *
 * @author Disastorm
 */
public class Player {

    private boolean moving=false;
    private int firstRoll = 0;//used to determine play order
    private int currentTile;
    private String name;
    private int money;
    private int publicId;
    private PlayerPiece piece;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private PlayerStatsWindow statsWindow;

    public Player(Socket socket, Integer publicId) {
        this.name = "Anonymous";
        this.socket = socket;
        if (socket != null) {
            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (publicId == null) {
                this.publicId = socket.hashCode();
            }
        } else {
            if (publicId == null) {
                this.publicId = new Random().nextInt();
            }
        }
        if (publicId != null) {
            this.publicId = publicId;
        }

        currentTile = 0;
        firstRoll = Dice.rollOne() + Dice.rollOne();
        money = 1500;
        this.setPiece(CoolMonopoly.getPlayerPiece(PieceType.MIKU));
    }
    public static final int X_TILE_SIZE = 52;

    public void movePiece(int tiles) {
        int pix = tiles * X_TILE_SIZE;;
        float x = piece.getX();
        float y = piece.getY();
        int destTile = currentTile + tiles;
        if (currentTile >= 0 && currentTile < 10) {

            //start off moving left
            int upPix = 0;
            int rightPix = 0;
            if (destTile > 20) {
                rightPix = (destTile - 20) * X_TILE_SIZE;
                pix -= rightPix;
                upPix = (10) * X_TILE_SIZE;
                pix -= upPix;
           //     upPix += X_TILE_SIZE;
            } else if (destTile > 10) {
                upPix = (destTile - 10) * X_TILE_SIZE;
                pix -= upPix;
         //       upPix += X_TILE_SIZE;
            }
            float newX = x - pix;
            float newY = y;
            piece.addDestination(new Point(newX, y));
            if (upPix > 0) {
                newY = newY - upPix;
                piece.addDestination(new Point(newX, newY));
            }
            if (rightPix > 0) {
                piece.addDestination(new Point(newX + rightPix, newY));
            }
        } else if (currentTile >= 10 && currentTile < 20) {
            //start off moving up
            int rightPix = 0;
            int downPix = 0;
            if (destTile > 30) {
                downPix = (destTile - 30) * X_TILE_SIZE;
                pix -= downPix;
                rightPix = (10) * X_TILE_SIZE;
                pix -= rightPix;
            } else if (destTile > 20) {
                rightPix = (destTile - 20) * X_TILE_SIZE;
                pix -= rightPix;
            }
            float newX = x;
            float newY = y - pix;
            piece.addDestination(new Point(newX, newY));
            if (rightPix > 0) {
                newX = newX + rightPix;
                piece.addDestination(new Point(newX, newY));
            }
            if (downPix > 0) {
                piece.addDestination(new Point(newX, newY + downPix));
            }
        } else if (currentTile >= 20 && currentTile < 30) {
            //start off moving right
            int downPix = 0;
            int leftPix = 0;
            if (destTile > 40) {
                leftPix = (destTile - 40) * X_TILE_SIZE;
                pix -= leftPix;
                downPix = (10) * X_TILE_SIZE;
                pix -= downPix;
            } else if (destTile > 30) {
                downPix = (destTile - 30) * X_TILE_SIZE;
                pix -= downPix;
            }
            float newX = x + pix;
            float newY = y;
            piece.addDestination(new Point(newX, newY));
            if (downPix > 0) {
                newY = newY + downPix;
                piece.addDestination(new Point(newX, newY));
            }
            if (leftPix > 0) {
                piece.addDestination(new Point(newX - leftPix, newY));
            }
        } else {
            //start off moving down
            int leftPix = 0;
            int upPix = 0;
            if (destTile > 50) {
                upPix = (destTile - 50) * X_TILE_SIZE;
                pix -= upPix;
                leftPix = (10) * X_TILE_SIZE;
                pix -= leftPix;
            } else if (destTile > 40) {
                leftPix = (destTile - 40) * X_TILE_SIZE;
                pix -= leftPix;
            }
            float newX = x;
            float newY = y + pix;
            piece.addDestination(new Point(newX, newY));
            if (leftPix > 0) {
                newX = newX - leftPix;
                piece.addDestination(new Point(newX, newY));
            }
            if (upPix > 0) {
                piece.addDestination(new Point(newX, newY - upPix));
            }
        }
    }

    public int getPublicId() {
        return publicId;
    }

    public void setPublicId(int publicId) {
        this.publicId = publicId;
    }

    public PlayerPiece getPiece() {
        return piece;
    }

    public void setPiece(PlayerPiece piece) {
        this.piece = piece;
    }

    public int getFirstRoll() {
        return firstRoll;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setCurrentTile(int i) {
        currentTile = i;
    }

    public int getCurrentTile() {
        return currentTile;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the statsWindow
     */
    public PlayerStatsWindow getStatsWindow() {
        return statsWindow;
    }

    /**
     * @param statsWindow the statsWindow to set
     */
    public void setStatsWindow(PlayerStatsWindow statsWindow) {
        this.statsWindow = statsWindow;
    }

    /**
     * @return the moving
     */
    public boolean isMoving() {
        return moving;
    }

    /**
     * @param moving the moving to set
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
