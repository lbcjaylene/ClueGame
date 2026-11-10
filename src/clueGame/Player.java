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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class Player {
    private String name;
    private String color;
    private int row;
    private int column;

    // cards currently in player's hand
    protected List<Card> hand = new ArrayList<>();
    
    // cards seen by player throughout the game
    protected Set<Card> seenCards = new HashSet<>();

    public Player(String name, String color, int row, int column) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.column = column;
    }
    
    // adds a card to the player hand and marks it as seen
    public void updateHand(Card card) {
        hand.add(card);
        updateSeen(card);
    }

    // adds a card to the list of cards the player has seen
    public void updateSeen(Card card) {
        seenCards.add(card);
    }

    public Set<Card> getSeenCards() {
        return seenCards;
    }

    // removes all cards from the player hand
    public void clearHand() {
        hand.clear();
    }
    
    // clears the cards the player has seen for a new game
    public void clearSeenCards() {
        seenCards.clear();
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    // disproves a suggestion using matching cards in the player's hand.
    public Card disproveSuggestion(Solution suggestion) {
    	
    	// stores all cards in the hand that match the suggestion
        List<Card> matchingCards = new ArrayList<>();
        for (Card card : hand) {
            if (card.equals(suggestion.getPerson()) ||
                card.equals(suggestion.getWeapon()) ||
                card.equals(suggestion.getRoom())) {
                matchingCards.add(card);
            }
        }

        // returns null if there is no match or randomly picks if there are multiple
        if (matchingCards.isEmpty()) {
            return null;
        } else if (matchingCards.size() == 1) {
            return matchingCards.get(0);
        } else {
            Random rand = new Random();
            return matchingCards.get(rand.nextInt(matchingCards.size()));
        }
    }
}

