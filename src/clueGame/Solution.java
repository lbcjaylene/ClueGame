/*
 * Solution Class
 * 
 * Represents the winning answer in Clue. It holds exactly one of each cards: person, weapon, room
 * 
 * @authors Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;

public class Solution {
    public Card person;
    public Card weapon;
    public Card room;

    // constructor to set up the game solution envelope
    public Solution(Card person, Card weapon, Card room) {
        this.person = person;
        this.weapon = weapon;
        this.room = room;
    }
    
    // returns the person card
    public Card getPerson() {
        return person;
    }
    
    // returns the weapon card
    public Card getWeapon() {
        return weapon;
    }
    
    // returns the room card
    public Card getRoom() {
        return room;
    }
}