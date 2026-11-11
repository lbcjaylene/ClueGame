package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Solution;

public class ComputerAITest {
	private static Card steveCard, alexCard, villagerCard;
    private static Card diamondSwordCard, bowCard, tridentCard;
    private static Card enchantmentRoomCard, netherFortressCard, trialChambersCard;

    // sets up the person, weapon, and room cards used in the AI tests
    @BeforeAll
    public static void setupCards() {
    	steveCard = new Card("Steve", CardType.PERSON);
        alexCard = new Card("Alex", CardType.PERSON);
        villagerCard = new Card("Villager", CardType.PERSON);
        diamondSwordCard = new Card("Diamond Sword", CardType.WEAPON);
        bowCard = new Card("Bow", CardType.WEAPON);
        tridentCard = new Card("Trident", CardType.WEAPON);
        enchantmentRoomCard = new Card("Enchantment Room", CardType.ROOM);
        netherFortressCard = new Card("Nether Fortress", CardType.ROOM);
        trialChambersCard = new Card("Trial Chambers", CardType.ROOM);
    }

<<<<<<< Updated upstream
    // Requirement 4: Create Suggestion Tests
    
    // tests if the suggested room matches the room the computer entered
=======
    // REQUIREMENT 4: Create Suggestion Tests

>>>>>>> Stashed changes
    @Test
    public void testCreateSuggestionRoomMatchesCurrentLocation() {
        ComputerPlayer player = new ComputerPlayer("Alex", "Orange", 0, 17);
        Board board = Board.getInstance();
        board.setDeck(allDeck());
        Solution suggestion = player.createSuggestion(enchantmentRoomCard);
        assertEquals(enchantmentRoomCard, suggestion.getRoom(), "Suggested room must match current room location.");
    }

    // tests if the computer chooses the only unseen person and weapon
    @Test
    public void testCreateSuggestionOneUnseenPersonAndWeapon() {
        ComputerPlayer player = new ComputerPlayer("Alex", "Orange", 0, 17);
        Board board = Board.getInstance();
        board.setDeck(allDeck());

        // marks the other cards as seen so only one option remains
        player.updateSeen(alexCard);
        player.updateSeen(villagerCard);
        player.updateSeen(bowCard);
        player.updateSeen(tridentCard);

        Solution suggestion = player.createSuggestion(enchantmentRoomCard);
        assertEquals(steveCard, suggestion.getPerson(), "If only one unseen person remains, it must be selected.");
        assertEquals(diamondSwordCard, suggestion.getWeapon(), "If only one unseen weapon remains, it must be selected.");
    }

    // tests if the computer randomly chooses between unseen people and weapons
    @Test
    public void testCreateSuggestionMultipleUnseenPersonsAndWeapons() {
        ComputerPlayer player = new ComputerPlayer("Alex", "Orange", 0, 17);
        Board board = Board.getInstance();
        board.setDeck(allDeck());
        player.updateSeen(villagerCard);
        player.updateSeen(tridentCard);
        boolean selectedSteve = false;
        boolean selectedAlex = false;
        boolean selectedDiamondSword = false;
        boolean selectedBow = false;

        // repeats the selection to check that each possible option gets chosen
        for (int i = 0; i < 100; i++) {
            Solution suggestion = player.createSuggestion(enchantmentRoomCard);
            if (suggestion.getPerson().equals(steveCard)) selectedSteve = true;
            if (suggestion.getPerson().equals(alexCard)) selectedAlex = true;
            if (suggestion.getWeapon().equals(diamondSwordCard)) selectedDiamondSword = true;
            if (suggestion.getWeapon().equals(bowCard)) selectedBow = true;
        }
        assertTrue(selectedSteve && selectedAlex, "Multiple unseen persons must be selected randomly.");
        assertTrue(selectedDiamondSword && selectedBow, "Multiple unseen weapons must be selected randomly.");
    }

<<<<<<< Updated upstream
    // Requirement 5: Select Target Tests
    
    // tests if the computer randomly selects a walkway when no rooms are targets
=======
    // REQUIREMENT: Select Target Tests

>>>>>>> Stashed changes
    @Test
    public void testSelectTargetNoRoomsInListSelectRandomly() {
        ComputerPlayer player = new ComputerPlayer("Alex", "Orange", 0, 17);
        Set<BoardCell> targets = new HashSet<>();
        BoardCell cell1 = new BoardCell(1, 1, 'W');
        BoardCell cell2 = new BoardCell(1, 2, 'W');
        targets.add(cell1);
        targets.add(cell2);
        boolean selectedCell1 = false;
        boolean selectedCell2 = false;

        // repeats the selection to check that each possible option gets chosen
        for (int i = 0; i < 100; i++) {
            BoardCell target = player.selectTarget(targets);
            if (target.equals(cell1)) selectedCell1 = true;
            if (target.equals(cell2)) selectedCell2 = true;
        }
        assertTrue(selectedCell1 && selectedCell2, "If no rooms present in target list, select target randomly.");
    }

    // tests if the computer chooses an unseen room over a walkway
    @Test
    public void testSelectTargetUnseenRoomSelected() {
        ComputerPlayer player = new ComputerPlayer("Alex", "Orange", 0, 17);
        Set<BoardCell> targets = new HashSet<>();
        BoardCell walkwayCell = new BoardCell(1, 1, 'W');
        BoardCell roomCell = new BoardCell(2, 2, enchantmentRoomCard);
        targets.add(walkwayCell);
        targets.add(roomCell);
        BoardCell selected = player.selectTarget(targets);
        assertEquals(roomCell, selected, "If unseen room is in target list, it must be selected.");
    }

    // tests if the computer randomly selects a target when the room has been seen
    @Test
    public void testSelectTargetSeenRoomSelectedRandomly() {
        ComputerPlayer player = new ComputerPlayer("Alex", "Orange", 0, 17);
        Set<BoardCell> targets = new HashSet<>();
        BoardCell walkwayCell = new BoardCell(1, 1, 'W');
        BoardCell roomCell = new BoardCell(2, 2, enchantmentRoomCard);
        targets.add(walkwayCell);
        targets.add(roomCell);
        player.updateSeen(enchantmentRoomCard);
        boolean selectedWalkway = false;
        boolean selectedRoom = false;

        // repeats the selection to check that each possible option gets chosen
        for (int i = 0; i < 100; i++) {
            BoardCell selected = player.selectTarget(targets);
            if (selected.equals(walkwayCell)) selectedWalkway = true;
            if (selected.equals(roomCell)) selectedRoom = true;
        }
        assertTrue(selectedWalkway && selectedRoom, "If room in target list has been seen, target selection must be random (including room).");
    }

    // creates a small deck of cards for testing the suggestions
    private ArrayList<Card> allDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        deck.add(steveCard); deck.add(alexCard); deck.add(villagerCard);
        deck.add(diamondSwordCard); deck.add(bowCard); deck.add(tridentCard);
        deck.add(enchantmentRoomCard); deck.add(netherFortressCard); deck.add(trialChambersCard);
        return deck;
    }
}

