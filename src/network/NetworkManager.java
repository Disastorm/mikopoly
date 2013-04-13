/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import coolmonopoly.CoolMonopoly;
import coolmonopoly.Player;
import coolmonopoly.board.Actions;
import coolmonopoly.board.Tile;
import coolmonopoly.cards.Chance;
import coolmonopoly.cards.CommunityChest;
import coolmonopoly.player.PieceType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Disastorm
 */
public class NetworkManager {

    public final static byte PLAYERS_TURN = 0x01;
    public final static byte PLAYERS_INIT = 0x02;
    public final static byte CHAT = 0x03;
    public final static byte PLAYER_BUY = 0x04;
    public final static byte PLAYER_MOVE = 0x05;
    public final static byte TURN_OVER = 0x06;
    public final static byte DRAW_CHANCE = 0x07;
    public final static byte DRAW_CHANCE_TO_HOST = 0x08;
    public final static byte DRAW_COMMUNITY_CHEST = 0x09;
    public final static byte DRAW_COMMUNITY_CHEST_TO_HOST = 0x0A;
    public final static byte PLAYER_AUCTION_BEGIN = 0x0B;
    public final static byte PLAYER_AUCTION_REQUEST_BID = 0x0C;
    public final static byte PLAYER_AUCTION_SEND_BID = 0x0D;
    public final static byte PLAYER_AUCTION_SEND_RESULT = 0x0E;

    public static void mainLoop(final Player playerToReadFrom, final boolean isHost) {
        final ObjectInputStream in = playerToReadFrom.getInputStream();
        new Thread() {

            public void run() {
                int tileIndex;
                Tile property;
                try {
                    while (true) {
                        byte command = in.readByte();
                        System.out.println("byte=" + command);
                        switch (command) {
                            case CHAT:
                                String s = in.readUTF();
                                CoolMonopoly.getChatBox().append(s);
                                if (isHost) {
                                    for (Player oPlayer : CoolMonopoly.getOtherPlayers()) {
                                        if (oPlayer != playerToReadFrom) {
                                            sendChat(oPlayer, s);
                                        }
                                    }
                                }
                                break;
                            case PLAYER_BUY:
                                int buyingPlayerId = in.readInt();
                                Player bPlayer = CoolMonopoly.getPlayer(buyingPlayerId);
                                tileIndex = in.readInt();
                                property = CoolMonopoly.getBoard().getTile(tileIndex);
                                Actions.buyProperty(bPlayer, property);
                                CoolMonopoly.showMessageDialog(bPlayer.getName() + " has bought " + property.getName());
                                if (isHost) {
                                    for (Player oPlayer : CoolMonopoly.getOtherPlayers()) {
                                        if (oPlayer != playerToReadFrom) {
                                            sendPlayerBuy(oPlayer, bPlayer, tileIndex);
                                        }
                                    }
                                }
                                break;
                            case PLAYER_AUCTION_BEGIN:
                                //only host should receive this
                                tileIndex = in.readInt();
                                property = CoolMonopoly.getBoard().getTile(tileIndex);
                                Actions.beginAuction(property);
                                if (isHost) { //should always be the case
                                    for (Player oPlayer : CoolMonopoly.getOtherPlayers()) {
                                        //send to everyone including the original sender
                                        sendPlayerRequestBid(oPlayer, tileIndex);
                                    }
                                }
                                CoolMonopoly.showAuctionMenu(property);
                                break;
                            case PLAYER_AUCTION_REQUEST_BID:
                                //only client should receive this
                                tileIndex = in.readInt();
                                property = CoolMonopoly.getBoard().getTile(tileIndex);
                                CoolMonopoly.showAuctionMenu(property);
                                break;
                            case PLAYER_MOVE:
                                int movingPlayerId = in.readInt();
                                Player mPlayer = CoolMonopoly.getPlayer(movingPlayerId);
                                int dice1 = in.readInt();
                                int dice2 = in.readInt();
                                //TODO: display dice roll animation
                                CoolMonopoly.movePlayer(mPlayer, dice1 + dice2);
                                if (isHost) {
                                    for (Player oPlayer : CoolMonopoly.getOtherPlayers()) {
                                        if (oPlayer != playerToReadFrom) {
                                            sendPlayerMove(oPlayer, mPlayer, dice1, dice2);
                                        }
                                    }
                                }
                                break;
                            case PLAYERS_TURN:
                                int turnPlayerId = in.readInt();
                                System.out.println("turnPlayerId:" + turnPlayerId);
                                Player tPlayer = CoolMonopoly.getPlayer(turnPlayerId);
                                if (tPlayer == CoolMonopoly.getPlayer()) {
                                    //its currentPlayers turn
                                    CoolMonopoly.setMyTurn();
                                    CoolMonopoly.toggleTurnMenu();
                                } else {
                                    System.out.println("player is not me");
                                }
                                CoolMonopoly.displayCurrentPlayerTurn(tPlayer);
                                //TODO: display players turn on UI
                                //the host will never receive this so dont need to check for that.
                                break;
                            case TURN_OVER:
                                //only host receives this
                                Player nPlayer = CoolMonopoly.advanceTurnOnHost();
                                if (nPlayer == CoolMonopoly.getPlayer()) {
                                    //its currentPlayers turn
                                    CoolMonopoly.setMyTurn();
                                    CoolMonopoly.toggleTurnMenu();
                                }
                                for (Player oPlayer : CoolMonopoly.getOtherPlayers()) {
                                    if (oPlayer != playerToReadFrom) {
                                        notifyTurn(oPlayer, nPlayer);
                                    }
                                }
                                break;
                            case DRAW_CHANCE_TO_HOST:
                                //only receive this if you are the host
                                int receivingPlayerId = in.readInt();
                                Player receivingPlayer = CoolMonopoly.getPlayer(receivingPlayerId);
                                Chance chance = CoolMonopoly.getBoard().drawChance();
                                for (Player oPlayer : CoolMonopoly.getOtherPlayers()) {
                                    sendChanceDraw(oPlayer, receivingPlayer, chance.getType());
                                }
                                //display chance card
                                CoolMonopoly.toggleChanceCommChestDisplay(chance, null);
                                Chance.activate(chance.getType(), receivingPlayer);
                                break;
                            case DRAW_CHANCE:
                                //only receive this if you are client
                                int drawingPlayerId = in.readInt();
                                Player drawingPlayer = CoolMonopoly.getPlayer(drawingPlayerId);
                                Chance.Type chanceType = (Chance.Type) in.readObject();
                                //display chance card
                                CoolMonopoly.toggleChanceCommChestDisplay(Chance.getCard(chanceType), null);
                                Chance.activate(chanceType, drawingPlayer);
                                break;
                            case DRAW_COMMUNITY_CHEST_TO_HOST:
                                //only receive this if you are the host
                                int receivingPlayerIdCc = in.readInt();
                                Player receivingPlayerCc = CoolMonopoly.getPlayer(receivingPlayerIdCc);
                                CommunityChest cChest = CoolMonopoly.getBoard().drawCommunityChest();
                                for (Player oPlayer : CoolMonopoly.getOtherPlayers()) {
                                    sendCommunityChestDraw(oPlayer, receivingPlayerCc, cChest.getType());
                                }
                                //display cChest card
                                CoolMonopoly.toggleChanceCommChestDisplay(null, cChest);
                                CommunityChest.activate(cChest.getType(), receivingPlayerCc);
                                break;
                            case DRAW_COMMUNITY_CHEST:
                                //only receive this if you are client
                                int drawingPlayerIdCc = in.readInt();
                                Player drawingPlayerCc = CoolMonopoly.getPlayer(drawingPlayerIdCc);
                                CommunityChest.Type cChestType = (CommunityChest.Type) in.readObject();
                                //display cChest card
                                CoolMonopoly.toggleChanceCommChestDisplay(null, CommunityChest.getCard(cChestType));
                                CommunityChest.activate(cChestType, drawingPlayerCc);
                                break;
                            default:
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    //disconnect here
                    try {
                        playerToReadFrom.getSocket().close();
                    } catch (IOException ex) {
                        Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }

    public synchronized static void sendPlayerMove(Player toPlayer, Player movingPlayer, int dice1, int dice2) {
        try {
            ObjectOutputStream out = toPlayer.getOutputStream();
            out.writeByte(PLAYER_MOVE);
            out.writeInt(movingPlayer.getPublicId());
            out.writeInt(dice1);
            out.writeInt(dice2);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void sendChat(Player toPlayer, String msg) {
        try {
            ObjectOutputStream out = toPlayer.getOutputStream();
            out.writeByte(CHAT);
            out.writeUTF(msg);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void sendPlayerBuy(Player toPlayer, Player buyingPlayer, int tileIndex) {
        try {
            ObjectOutputStream out = toPlayer.getOutputStream();
            out.writeByte(PLAYER_BUY);
            out.writeInt(buyingPlayer.getPublicId());
            out.writeInt(tileIndex);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void sendPlayerRequestBid(Player toPlayer, int tileIndex) {
        try {
            ObjectOutputStream out = toPlayer.getOutputStream();
            out.writeByte(PLAYER_BUY);
            out.writeInt(tileIndex);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void sendChanceDraw(Player toPlayer, Player receivingPlayer, Chance.Type cardType) {
        try {
            ObjectOutputStream out = toPlayer.getOutputStream();
            out.writeByte(DRAW_CHANCE);
            out.writeInt(receivingPlayer.getPublicId());
            out.writeObject(cardType);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void sendChanceDrawToHost(Player toPlayer, Player receivingPlayer) {
        try {
            ObjectOutputStream out = toPlayer.getOutputStream();
            out.writeByte(DRAW_CHANCE_TO_HOST);
            out.writeInt(receivingPlayer.getPublicId());
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void sendCommunityChestDraw(Player toPlayer, Player receivingPlayer, CommunityChest.Type cardType) {
        try {
            ObjectOutputStream out = toPlayer.getOutputStream();
            out.writeByte(DRAW_COMMUNITY_CHEST);
            out.writeInt(receivingPlayer.getPublicId());
            out.writeObject(cardType);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void sendCommunityChestDrawToHost(Player toPlayer, Player receivingPlayer) {
        try {
            ObjectOutputStream out = toPlayer.getOutputStream();
            out.writeByte(DRAW_COMMUNITY_CHEST_TO_HOST);
            out.writeInt(receivingPlayer.getPublicId());
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void notifyTurn(Player toPlayer, Player playersTurn) {
        try {
            ObjectOutputStream out = toPlayer.getOutputStream();
            out.writeByte(PLAYERS_TURN);
            out.writeInt(playersTurn.getPublicId());
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void notifyEndTurn(Player host) {
        try {
            ObjectOutputStream out = host.getOutputStream();
            out.writeByte(TURN_OVER);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void sendPreferredPieceTypesToHost(Player host, List<PieceType> pieceTypes) {
        try {
            ObjectOutputStream out = host.getOutputStream();
            out.writeObject(pieceTypes);//send PieceType        
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static List<PieceType> receivePreferredPieceTypes(Player receiveFrom) {
        try {
            ObjectInputStream in = receiveFrom.getInputStream();
            List<PieceType> pieceTypes = (List<PieceType>) in.readObject();
            return pieceTypes;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public synchronized static void sendPlayerInit(Player playerToSendTo, Player me, List<Player> otherPlayers) {
        try {
            ObjectOutputStream out = playerToSendTo.getOutputStream();
            out.writeByte(PLAYERS_INIT);
            out.writeInt(otherPlayers.size() - 1);
            out.writeInt(playerToSendTo.getPublicId());
            out.writeInt(me.getPublicId());
            out.writeObject(playerToSendTo.getPiece().getBody());//send PieceType
            for (Player oPlayer : otherPlayers) {
                if (oPlayer != playerToSendTo) {
                    out.writeInt(oPlayer.getPublicId());
                }
            }
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void readPlayerInit(Player host) {
        try {
            ObjectInputStream in = host.getInputStream();
            byte b = in.readByte();
            switch (b) {
                case PLAYERS_INIT:
                    int otherPlayersSize = in.readInt();
                    int myPublicId = in.readInt();
                    int hostPublicId = in.readInt();
                    PieceType pieceType = (PieceType) in.readObject();
                    Player me = new Player(null, myPublicId);
                    me.setPiece(CoolMonopoly.getPlayerPiece(pieceType));
                    host.setPublicId(hostPublicId);
                    List<Player> otherPlayers = new LinkedList<Player>();
                    for (int i = 0; i < otherPlayersSize; i++) {
                        Player oPlayer = new Player(null, in.readInt());
                        otherPlayers.add(oPlayer);
                    }
                    otherPlayers.add(host);
                    CoolMonopoly.setupPlayers(me, otherPlayers);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "error");
                    break;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static void waitForPlayerEndTurn(Player player) {
        try {
            ObjectInputStream in = player.getInputStream();
            while (true) {
                boolean chat = false;
                byte b = in.readByte();
                switch (b) {
                    case CHAT:
                        chat = true;
                        break;
                    default:
                        break;
                }
                if (chat == false) {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
