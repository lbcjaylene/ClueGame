package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Solution;

public class ComputerAITest {
    private static Card mustardCard, scarlettCard, plumCard;
    private static Card ropeCard, knifeCard, wrenchCard;
    private static Card conservatoryCard, kitchenCard, hallCard;

    @BeforeAll
    public static void setupCards() {
        mustardCard = new Card("Colonel Mustard", CardType.PERSON);
        scarlettCard = new Card("Miss Scarlett", CardType.PERSON);
        plumCard = new Card("Professor Plum", CardType.PERSON);

        ropeCard = new Card("Rope", CardType.WEAPON);
        knifeCard = new Card("Knife", CardType.WEAPON);
        wrenchCard = new Card("Wrench", CardType.WEAPON);

        conservatoryCard = new Card("Conservatory", CardType.ROOM);
        kitchenCard = new Card("Kitchen", CardType.ROOM);
        hallCard = new Card("Hall", CardType.ROOM);
    }

    // ==========================================
    // REQUIREMENT 4: Create Suggestion Tests
    // ==========================================

    @Test
    public void testCreateSuggestionRoomMatchesCurrentLocation() {
        ComputerPlayer player = new ComputerPlayer("AI", "Red", 0, 0);
        Board board = Board.getInstance();
        board.setDeck(allDeck());

        Solution suggestion = player.createSuggestion(conservatoryCard);
        assertEquals(conservatoryCard, suggestion.getRoom(), 
            "Suggested room must match current room location.");
    }

    @Test
    public void testCreateSuggestionOneUnseenPersonAndWeapon() {
        ComputerPlayer player = new ComputerPlayer("AI", "Red", 0, 0);
        Board board = Board.getInstance();
        board.setDeck(allDeck());

        player.updateSeen(scarlettCard);
        player.updateSeen(plumCard);
        player.updateSeen(knifeCard);
        player.updateSeen(wrenchCard);

        Solution suggestion = player.createSuggestion(conservatoryCard);
        assertEquals(mustardCard, suggestion.getPerson(), 
            "If only one unseen person remains, it must be selected.");
        assertEquals(ropeCard, suggestion.getWeapon(), 
            "If only one unseen weapon remains, it must be selected.");
    }

    @Test
    public void testCreateSuggestionMultipleUnseenPersonsAndWeapons() {
        ComputerPlayer player = new ComputerPlayer("AI", "Red", 0, 0);
        Board board = Board.getInstance();
        board.setDeck(allDeck());

        player.updateSeen(plumCard);
        player.updateSeen(wrenchCard);

        boolean selectedMustard = false;
        boolean selectedScarlett = false;
        boolean selectedRope = false;
        boolean selectedKnife = false;

        for (int i = 0; i < 100; i++) {
            Solution suggestion = player.createSuggestion(conservatoryCard);
            if (suggestion.getPerson().equals(mustardCard)) selectedMustard = true;
            if (suggestion.getPerson().equals(scarlettCard)) selectedScarlett = true;
            if (suggestion.getWeapon().equals(ropeCard)) selectedRope = true;
            if (suggestion.getWeapon().equals(knifeCard)) selectedKnife = true;
        }

        assertTrue(selectedMustard && selectedScarlett, 
            "Multiple unseen persons must be selected randomly.");
        assertTrue(selectedRope && selectedKnife, 
            "Multiple unseen weapons must be selected randomly.");
    }

    // ==========================================
    // REQUIREMENT 5: Select Target Tests
    // ==========================================

    @Test
    public void testSelectTargetNoRoomsInListSelectRandomly() {
        ComputerPlayer player = new ComputerPlayer("AI", "Red", 0, 0);
        Set<BoardCell> targets = new HashSet<>();
        BoardCell cell1 = new BoardCell(1, 1);
        BoardCell cell2 = new BoardCell(1, 2);
        targets.add(cell1);
        targets.add(cell2);

        boolean selectedCell1 = false;
        boolean selectedCell2 = false;

        for (int i = 0; i < 100; i++) {
            BoardCell target = player.selectTarget(targets);
            if (target.equals(cell1)) selectedCell1 = true;
            if (target.equals(cell2)) selectedCell2 = true;
        }

        assertTrue(selectedCell1 && selectedCell2, 
            "If no rooms present in target list, select target randomly.");
    }

    @Test
    public void testSelectTargetUnseenRoomSelected() {
        ComputerPlayer player = new ComputerPlayer("AI", "Red", 0, 0);
        Set<BoardCell> targets = new HashSet<>();
        BoardCell walkwayCell = new BoardCell(1, 1);
        BoardCell roomCell = new BoardCell(2, 2, conservatoryCard);
        targets.add(walkwayCell);
        targets.add(roomCell);

        BoardCell selected = player.selectTarget(targets);
        assertEquals(roomCell, selected, 
            "If unseen room is in target list, it must be selected.");
    }

    @Test
    public void testSelectTargetSeenRoomSelectedRandomly() {
        ComputerPlayer player = new ComputerPlayer("AI", "Red", 0, 0);
        Set<BoardCell> targets = new HashSet<>();
        BoardCell walkwayCell = new BoardCell(1, 1);
        BoardCell roomCell = new BoardCell(2, 2, conservatoryCard);
        targets.add(walkwayCell);
        targets.add(roomCell);

        player.updateSeen(conservatoryCard);

        boolean selectedWalkway = false;
        boolean selectedRoom = false;

        for (int i = 0; i < 100; i++) {
            BoardCell selected = player.selectTarget(targets);
            if (selected.equals(walkwayCell)) selectedWalkway = true;
            if (selected.equals(roomCell)) selectedRoom = true;
        }

        assertTrue(selectedWalkway && selectedRoom, 
            "If room in target list has been seen, target selection must be random (including room).");
    }

    private HashSet<Card> allDeck() {
        HashSet<Card> deck = new HashSet<>();
        deck.add(mustardCard); deck.add(scarlettCard); deck.add(plumCard);
        deck.add(ropeCard); deck.add(knifeCard); deck.add(wrenchCard);
        deck.add(conservatoryCard); deck.add(kitchenCard); deck.add(hallCard);
        return deck;
    }
}