/*
 * Player Class
 * 
 * Abstract parent class for the human and computer players.
 * Stores player info such as their name, color, location, and the
 * cards that are held by the player
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private String name;
    private String color;
    private int row;
    private int column;
    protected List<Card> hand = new ArrayList<>();

    // constructor initializes the player's info
    public Player(String name, String color, int row, int column) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.column = column;
    }

    // adds a card to the player's hand
    public void updateHand(Card card) {
        hand.add(card);
    }

    // clears all cards from the player's hand
    public void clearHand() {
        hand.clear();
    }

    // returns list of cards held by player
    public List<Card> getHand() {
        return hand;
    }

    // returns the player's name
    public String getName() {
        return name;
    }

    // returns the player's color 
    public String getColor() {
        return color;
    }

    // returns the player's current row
    public int getRow() {
        return row;
    }

    // returns the player's current column
    public int getColumn() {
        return column;
    }
}