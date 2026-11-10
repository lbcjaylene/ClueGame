/*
 * ComputerPlayer class
 * 
 * Represents a computer controlled player in the game. Inherits 
 * player info and functionality from Player. It handles the computer
 * player decisions and movements
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
    private Random rand = new Random();

    // constructor passes player info to the parent class
    public ComputerPlayer(String name, String color, int row, int column) {
        super(name, color, row, column);
    }

    // creates a suggestion using the current room and unseen cards
    public Solution createSuggestion(Card roomCard) {
        List<Card> unseenPeople = new ArrayList<>();
        List<Card> unseenWeapons = new ArrayList<>();

        // checks the deck for person and weapon cards that have not been seen
        for (Card card : Board.getInstance().getDeck()) {
            if (!seenCards.contains(card)) {
                if (card.getCardType() == CardType.PERSON) {
                    unseenPeople.add(card);
                } else if (card.getCardType() == CardType.WEAPON) {
                    unseenWeapons.add(card);
                }
            }
        }

        // returns null if there are no unseen people or weapons
        if (unseenPeople.isEmpty() || unseenWeapons.isEmpty()) {
            return null;
        }

        // randomly selects an unseen person and weapon
        Card person = unseenPeople.get(rand.nextInt(unseenPeople.size()));
        Card weapon = unseenWeapons.get(rand.nextInt(unseenWeapons.size()));
        return new Solution(person, weapon, roomCard);
    }

    // selects a movement target and prioritizes unseen rooms
    public BoardCell selectTarget(Set<BoardCell> targets) {
        List<BoardCell> unseenRooms = new ArrayList<>();

        // checks if any target is an unseen room
        for (BoardCell cell : targets) {
            if (cell.isRoomCenter() && cell.getRoomCard() != null
                    && !seenCards.contains(cell.getRoomCard())) {
                unseenRooms.add(cell);
            }
        }

        // randomly selects an unseen room if one is available
        if (!unseenRooms.isEmpty()) {
            return unseenRooms.get(rand.nextInt(unseenRooms.size()));
        }

        // otherwise randomly selects from all possible targets
        if (targets.isEmpty()) {
            return null;
        }

        List<BoardCell> allTargets = new ArrayList<>(targets);
        return allTargets.get(rand.nextInt(allTargets.size()));
    }
}
