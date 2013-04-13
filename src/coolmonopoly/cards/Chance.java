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
public class Chance implements Comparable<Chance> {
    private final static Random randomGen = new Random();
    private final static Map<Chance.Type, Chance> cardMap = new HashMap<Chance.Type, Chance>();

    static {
        cardMap.put(Chance.Type.ADVANCE_TO_GO, new Chance(Chance.Type.ADVANCE_TO_GO));
        cardMap.put(Chance.Type.ADVANCE_TO_BOARDWALK, new Chance(Chance.Type.ADVANCE_TO_BOARDWALK));
        cardMap.put(Chance.Type.ADVANCE_TO_ILLIONOIS, new Chance(Chance.Type.ADVANCE_TO_ILLIONOIS));
        cardMap.put(Chance.Type.ADVANCE_TO_RAILROAD, new Chance(Chance.Type.ADVANCE_TO_RAILROAD));//pay double
        cardMap.put(Chance.Type.ADVANCE_TO_RAILROAD, new Chance(Chance.Type.ADVANCE_TO_RAILROAD));
        cardMap.put(Chance.Type.ADVANCE_TO_READING_RAILROAD, new Chance(Chance.Type.ADVANCE_TO_READING_RAILROAD));
        cardMap.put(Chance.Type.ADVANCE_TO_ST_CHARLES, new Chance(Chance.Type.ADVANCE_TO_ST_CHARLES));
        cardMap.put(Chance.Type.ADVANCE_TO_UTILITY, new Chance(Chance.Type.ADVANCE_TO_UTILITY));//pay 10X dice
        cardMap.put(Chance.Type.COLLECT_100, new Chance(Chance.Type.COLLECT_100));
        cardMap.put(Chance.Type.COLLECT_150, new Chance(Chance.Type.COLLECT_150));
        cardMap.put(Chance.Type.COLLECT_50, new Chance(Chance.Type.COLLECT_50));
        cardMap.put(Chance.Type.GET_OUT_OF_JAIL_FREE, new Chance(Chance.Type.GET_OUT_OF_JAIL_FREE));
        cardMap.put(Chance.Type.GO_BACK_3_SPACES, new Chance(Chance.Type.GO_BACK_3_SPACES));
        cardMap.put(Chance.Type.GO_TO_JAIL, new Chance(Chance.Type.GO_TO_JAIL));
        cardMap.put(Chance.Type.PAY_15, new Chance(Chance.Type.PAY_15));
        cardMap.put(Chance.Type.PAY_25_PER_HOUSE_100_PER_HOTEL, new Chance(Chance.Type.PAY_25_PER_HOUSE_100_PER_HOTEL));
        cardMap.put(Chance.Type.PAY_EACH_PLAYER_50, new Chance(Chance.Type.PAY_EACH_PLAYER_50));
    }

    public static Chance getCard(Type type) {
        return cardMap.get(type);
    }

    @Override
    public int compareTo(Chance o) {
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

        ADVANCE_TO_GO, ADVANCE_TO_ILLIONOIS, ADVANCE_TO_ST_CHARLES, ADVANCE_TO_UTILITY,//pass go, collect 200
        ADVANCE_TO_RAILROAD,//2 of these
        COLLECT_50,
        GET_OUT_OF_JAIL_FREE,
        GO_BACK_3_SPACES, GO_TO_JAIL,
        PAY_15, PAY_25_PER_HOUSE_100_PER_HOTEL,
        ADVANCE_TO_READING_RAILROAD, ADVANCE_TO_BOARDWALK,
        PAY_EACH_PLAYER_50, COLLECT_150,
        COLLECT_100
    }
    private Chance.Type type;
    private int rand;
    private IDecorator cardImage;

    private Chance(Chance.Type type) {
        this.type = type;
        this.rand = randomGen.nextInt();
        String image;
        switch (type) {
            case ADVANCE_TO_BOARDWALK:
                image = "data/cards/chance.jpg";
                break;
            case ADVANCE_TO_GO:
                image = "data/cards/chance.jpg";
                break;
            case ADVANCE_TO_ILLIONOIS:
                image = "data/cards/chance.jpg";
                break;
            case ADVANCE_TO_RAILROAD:
                image = "data/cards/chance.jpg";
                break;
            case ADVANCE_TO_READING_RAILROAD:
                image = "data/cards/chance.jpg";
                break;
            case ADVANCE_TO_ST_CHARLES:
                image = "data/cards/chance.jpg";
                break;
            case ADVANCE_TO_UTILITY:
                image = "data/cards/chance.jpg";
                break;
            case COLLECT_100:
                image = "data/cards/chance.jpg";
                break;
            case COLLECT_150:
                image = "data/cards/chance.jpg";
                break;
            case COLLECT_50:
                image = "data/cards/chance.jpg";
                break;
            case GET_OUT_OF_JAIL_FREE:
                image = "data/cards/chance.jpg";
                break;
            case GO_BACK_3_SPACES:
                image = "data/cards/chance.jpg";
                break;
            case GO_TO_JAIL:
                image = "data/cards/chance.jpg";
                break;
            case PAY_15:
                image = "data/cards/chance.jpg";
                break;
            case PAY_25_PER_HOUSE_100_PER_HOTEL:
                image = "data/cards/chance.jpg";
                break;
            default:
                image = "data/cards/chance.jpg";
                break;
        }
        cardImage = Utility.getDecorator(image);
    }

    public static Stack<Chance> getNewDeck() {
        LinkedList<Chance> list = new LinkedList<Chance>();
        for (Chance card : cardMap.values()) {
            list.add(card);
        }
        Collections.sort(list);
        Stack<Chance> stack = new Stack<Chance>();
        stack.addAll(list);
        return stack;
    }

    public Type getType() {
        return type;
    }

    public static void activate(Type type, Player onPlayer) {
        System.out.println("activating " + type + " on player: " + onPlayer.getPublicId());
        switch (type) {
            case ADVANCE_TO_BOARDWALK:
                break;
            case ADVANCE_TO_GO:
                break;
            case ADVANCE_TO_ILLIONOIS:
                break;
            case ADVANCE_TO_RAILROAD:
                break;
            case ADVANCE_TO_READING_RAILROAD:
                break;
            case ADVANCE_TO_ST_CHARLES:
                break;
            case ADVANCE_TO_UTILITY:
                break;
            case COLLECT_100:
                break;
            case COLLECT_150:
                break;
            case COLLECT_50:
                break;
            case GET_OUT_OF_JAIL_FREE:
                break;
            case GO_BACK_3_SPACES:
                break;
            case GO_TO_JAIL:
                break;
            case PAY_15:
                break;
            case PAY_25_PER_HOUSE_100_PER_HOTEL:
                break;
            default:
                break;
        }
    }
}
