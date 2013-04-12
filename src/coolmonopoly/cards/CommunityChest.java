/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coolmonopoly.cards;

import coolmonopoly.Player;
import coolmonopoly.util.Utility;
import java.io.Serializable;
import java.util.*;
import org.fenggui.decorator.IDecorator;

/**
 *
 * @author Disastorm
 */
public class CommunityChest implements Comparable<CommunityChest> {
    private final static Random randomGen = new Random();
    private final static Map<CommunityChest.Type, CommunityChest> cardMap = new HashMap<CommunityChest.Type, CommunityChest>();

    static {
        cardMap.put(Type.ADVANCE_TO_GO, new CommunityChest(Type.ADVANCE_TO_GO));
        cardMap.put(Type.COLLECT_10, new CommunityChest(Type.COLLECT_10));
        cardMap.put(Type.COLLECT_100, new CommunityChest(Type.COLLECT_100));
        cardMap.put(Type.COLLECT_100_2, new CommunityChest(Type.COLLECT_100_2));
        cardMap.put(Type.COLLECT_100_3, new CommunityChest(Type.COLLECT_100_3));
        cardMap.put(Type.COLLECT_10_2, new CommunityChest(Type.COLLECT_10_2));
        cardMap.put(Type.COLLECT_20, new CommunityChest(Type.COLLECT_20));
        cardMap.put(Type.COLLECT_200, new CommunityChest(Type.COLLECT_200));
        cardMap.put(Type.COLLECT_25, new CommunityChest(Type.COLLECT_25));
        cardMap.put(Type.COLLECT_50, new CommunityChest(Type.COLLECT_50));
        cardMap.put(Type.COLLECT_50_FROM_PLAYERS, new CommunityChest(Type.COLLECT_50_FROM_PLAYERS));
        cardMap.put(Type.GET_OUT_OF_JAIL_FREE, new CommunityChest(Type.GET_OUT_OF_JAIL_FREE));
        cardMap.put(Type.GO_TO_JAIL, new CommunityChest(Type.GO_TO_JAIL));
        cardMap.put(Type.PAY_100, new CommunityChest(Type.PAY_100));
        cardMap.put(Type.PAY_150, new CommunityChest(Type.PAY_150));
        cardMap.put(Type.PAY_40_PER_HOUSE_AND_115_PER_HOTEL, new CommunityChest(Type.PAY_40_PER_HOUSE_AND_115_PER_HOTEL));
        cardMap.put(Type.PAY_50, new CommunityChest(Type.PAY_50));
    }
    
    public static CommunityChest getCard(Type type){
        return cardMap.get(type);
    }
    
    @Override
    public int compareTo(CommunityChest o) {
        return rand - o.rand;
    }

    /**
     * @return the cardImage
     */
    public IDecorator getCardImage() {
        return cardImage;
    }

    /**
     * @param cardImage the cardImage to set
     */
    public void setCardImage(IDecorator cardImage) {
        this.cardImage = cardImage;
    }
    
    public enum Type implements Serializable {
        
        ADVANCE_TO_GO, COLLECT_200, COLLECT_100, COLLECT_100_2, COLLECT_100_3, COLLECT_50, COLLECT_50_FROM_PLAYERS, COLLECT_25, COLLECT_20, COLLECT_10, COLLECT_10_2,
        GET_OUT_OF_JAIL_FREE, GO_TO_JAIL,
        PAY_150, PAY_100, PAY_50, PAY_40_PER_HOUSE_AND_115_PER_HOTEL;
    }
    private Type type;
    private int rand;
    private IDecorator cardImage;
    
    private CommunityChest(Type type) {
        this.type = type;
        this.rand = randomGen.nextInt();
        String image;
        switch (type) {
            case ADVANCE_TO_GO:
                image = "data/cards/cc.jpg";
                break;
            case COLLECT_10:
                image = "data/cards/cc.jpg";
                break;
            case COLLECT_10_2:
                image = "data/cards/cc.jpg";
                break;
            case COLLECT_100:
                image = "data/cards/cc.jpg";
                break;
            case COLLECT_100_2:
                image = "data/cards/cc.jpg";
                break;
            case COLLECT_100_3:
                image = "data/cards/cc.jpg";
                break;
            case COLLECT_20:
                image = "data/cards/cc.jpg";
                break;
            case COLLECT_200:
                image = "data/cards/cc.jpg";
                break;
            case COLLECT_25:
                image = "data/cards/cc.jpg";
                break;
            case COLLECT_50:
                image = "data/cards/cc.jpg";
                break;
            case COLLECT_50_FROM_PLAYERS:
                image = "data/cards/cc.jpg";
                break;
            case GET_OUT_OF_JAIL_FREE:
                image = "data/cards/cc.jpg";
                break;
            case GO_TO_JAIL:
                image = "data/cards/cc.jpg";
                break;
            case PAY_100:
                image = "data/cards/cc.jpg";
                break;
            case PAY_150:
                image = "data/cards/cc.jpg";
                break;
            case PAY_40_PER_HOUSE_AND_115_PER_HOTEL:
                image = "data/cards/cc.jpg";
                break;
            case PAY_50:
                image = "data/cards/cc.jpg";
                break;
            default:
                image = "data/cards/cc.jpg";
                break;
        }
        cardImage = Utility.getDecorator(image);
    }
    
    public static Stack<CommunityChest> getNewDeck() {
        LinkedList<CommunityChest> list = new LinkedList<CommunityChest>();
        for (CommunityChest card : cardMap.values()) {
            list.add(card);
        }
        Collections.sort(list);
        Stack<CommunityChest> stack = new Stack<CommunityChest>();
        stack.addAll(list);
        return stack;
    }
    
    public Type getType() {
        return type;
    }
    
    public static void activate(Type type, Player onPlayer) {
        switch (type) {
            case ADVANCE_TO_GO:
                break;
            case COLLECT_10:
            case COLLECT_10_2:
                break;
            case COLLECT_100:
            case COLLECT_100_2:
            case COLLECT_100_3:
                break;
            case COLLECT_20:
                break;
            case COLLECT_200:
                break;
            case COLLECT_25:
                break;
            case COLLECT_50:
                break;
            case COLLECT_50_FROM_PLAYERS:
                break;
            case GET_OUT_OF_JAIL_FREE:
                break;
            case GO_TO_JAIL:
                break;
            case PAY_100:
                break;
            case PAY_150:
                break;
            case PAY_40_PER_HOUSE_AND_115_PER_HOTEL:
                break;
            case PAY_50:
                break;
            default:
                break;
        }
    }
}
