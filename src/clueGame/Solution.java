// Trisha, Jaylene 

package clueGame;

/*
 * Solution class is the winning answer envelope in Clue,holding exactly one of each card: person, weapon, room.
 */
public class Solution {
    public Card person;
    public Card weapon;
    public Card room;

    // Constructor to set up the game solution envelope
    public Solution(Card person, Card weapon, Card room) {
        this.person = person;
        this.weapon = weapon;
        this.room = room;
    }

    // Getters for testing and checking guesses
    public Card getPerson() {
        return person;
    }

    public Card getWeapon() {
        return weapon;
    }

    public Card getRoom() {
        return room;
    }
}