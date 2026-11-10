/*
 * Card Class
 * 
 * Represents a card that is apart of the board/game. 
 * It stores the name of the card and it's type
 * 
 * @authors Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;

import java.util.Objects;

public class Card {
    private String cardName;
    private CardType cardType;

    // constructor for card name and card type (PERSON, WEAPON, ROOM)
    public Card(String cardName, CardType cardType) {
        this.cardName = cardName;
        this.cardType = cardType;
    }

    // getter for the name
    public String getCardName() {
        return cardName;
    }

    // getter for type
    public CardType getCardType() {
        return cardType;
    }

    // custom equals method to compare cards by name and type instead of reference
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card other = (Card) obj;
        return Objects.equals(cardName, other.cardName) && cardType == other.cardType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardName, cardType);
    }

    // useful for printing card details during debugging/testing
    @Override
    public String toString() {
        return cardName + " (" + cardType + ")";
    }
}