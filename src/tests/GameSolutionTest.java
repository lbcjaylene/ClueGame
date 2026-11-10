package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class GameSolutionTest {
    private static Card mustardCard, scarlettCard, ropeCard, knifeCard, conservatoryCard, kitchenCard;
    private Board board;

    @BeforeAll
    public static void setupCards() {
        mustardCard = new Card("Colonel Mustard", CardType.PERSON);
        scarlettCard = new Card("Miss Scarlett", CardType.PERSON);
        ropeCard = new Card("Rope", CardType.WEAPON);
        knifeCard = new Card("Knife", CardType.WEAPON);
        conservatoryCard = new Card("Conservatory", CardType.ROOM);
        kitchenCard = new Card("Kitchen", CardType.ROOM);
    }

    @BeforeEach
    public void setUp() {
        board = Board.getInstance();
        // Clear previous state and set known answer
        board.setTheAnswer(new Solution(mustardCard, ropeCard, conservatoryCard));
    }

    // ==========================================
    // REQUIREMENT 1: Check an Accusation Tests
    // ==========================================

    @Test
    public void testCheckAccusationCorrect() {
        Solution correctAccusation = new Solution(mustardCard, ropeCard, conservatoryCard);
        assertTrue(board.checkAccusation(correctAccusation), 
            "Accusation with correct person, weapon, and room should return true.");
    }

    @Test
    public void testCheckAccusationWrongPerson() {
        Solution wrongPerson = new Solution(scarlettCard, ropeCard, conservatoryCard);
        assertFalse(board.checkAccusation(wrongPerson), 
            "Accusation with wrong person should return false.");
    }

    @Test
    public void testCheckAccusationWrongWeapon() {
        Solution wrongWeapon = new Solution(mustardCard, knifeCard, conservatoryCard);
        assertFalse(board.checkAccusation(wrongWeapon), 
            "Accusation with wrong weapon should return false.");
    }

    @Test
    public void testCheckAccusationWrongRoom() {
        Solution wrongRoom = new Solution(mustardCard, ropeCard, kitchenCard);
        assertFalse(board.checkAccusation(wrongRoom), 
            "Accusation with wrong room should return false.");
    }

    // ==========================================
    // REQUIREMENT 2: Player Disproves Suggestion Tests
    // ==========================================

    @Test
    public void testDisproveSuggestionOneMatchingCard() {
        Player player = new ComputerPlayer("Test Player", "Red", 0, 0);
        player.updateHand(mustardCard);
        player.updateHand(knifeCard);

        Solution suggestion = new Solution(mustardCard, ropeCard, conservatoryCard);
        Card disproved = player.disproveSuggestion(suggestion);

        assertEquals(mustardCard, disproved, 
            "Player with one matching card should return that card.");
    }

    @Test
    public void testDisproveSuggestionMultipleMatchingCardsRandom() {
        Player player = new ComputerPlayer("Test Player", "Red", 0, 0);
        player.updateHand(mustardCard);
        player.updateHand(ropeCard);

        Solution suggestion = new Solution(mustardCard, ropeCard, conservatoryCard);

        boolean returnedMustard = false;
        boolean returnedRope = false;

        for (int i = 0; i < 100; i++) {
            Card disproved = player.disproveSuggestion(suggestion);
            if (disproved.equals(mustardCard)) returnedMustard = true;
            if (disproved.equals(ropeCard)) returnedRope = true;
        }

        assertTrue(returnedMustard && returnedRope, 
            "Player with multiple matching cards should randomly select between them.");
    }

    @Test
    public void testDisproveSuggestionNoMatchingCards() {
        Player player = new ComputerPlayer("Test Player", "Red", 0, 0);
        player.updateHand(scarlettCard);
        player.updateHand(knifeCard);

        Solution suggestion = new Solution(mustardCard, ropeCard, conservatoryCard);
        assertNull(player.disproveSuggestion(suggestion), 
            "Player with no matching cards should return null.");
    }

    // ==========================================
    // REQUIREMENT 3: Handle a Suggestion Made Tests
    // ==========================================

    @Test
    public void testHandleSuggestionNoOneDisproves() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new HumanPlayer("Human", "Red", 0, 0);
        Player p2 = new ComputerPlayer("Comp 1", "Blue", 0, 1);
        p1.updateHand(scarlettCard);
        p2.updateHand(knifeCard);
        players.add(p1);
        players.add(p2);
        board.setPlayers(players);

        Solution suggestion = new Solution(mustardCard, ropeCard, conservatoryCard);
        assertNull(board.handleSuggestion(suggestion, p1), 
            "If no player can disprove, return null.");
    }

    @Test
    public void testHandleSuggestionOnlySuggestingPlayerCanDisprove() {
        ArrayList<Player> players = new ArrayList<>();
        Player accuser = new ComputerPlayer("Accuser", "Red", 0, 0);
        Player p2 = new ComputerPlayer("Comp 1", "Blue", 0, 1);
        accuser.updateHand(mustardCard);
        p2.updateHand(knifeCard);
        players.add(accuser);
        players.add(p2);
        board.setPlayers(players);

        Solution suggestion = new Solution(mustardCard, ropeCard, conservatoryCard);
        assertNull(board.handleSuggestion(suggestion, accuser), 
            "Suggesting player's own cards should not disprove their suggestion; return null.");
    }

    @Test
    public void testHandleSuggestionOnlyHumanCanDisprove() {
        ArrayList<Player> players = new ArrayList<>();
        Player accuser = new ComputerPlayer("Accuser", "Red", 0, 0);
        HumanPlayer human = new HumanPlayer("Human", "Blue", 0, 1);
        human.updateHand(mustardCard);
        players.add(accuser);
        players.add(human);
        board.setPlayers(players);

        Solution suggestion = new Solution(mustardCard, ropeCard, conservatoryCard);
        Card disproved = board.handleSuggestion(suggestion, accuser);

        assertEquals(mustardCard, disproved, 
            "Human player returning single matching card should be returned.");
    }

    @Test
    public void testHandleSuggestionQueryOrder() {
        ArrayList<Player> players = new ArrayList<>();
        Player p0 = new ComputerPlayer("P0", "Red", 0, 0);
        Player p1 = new ComputerPlayer("P1", "Blue", 0, 1);
        Player p2 = new ComputerPlayer("P2", "Green", 0, 2);

        p1.updateHand(mustardCard);
        p2.updateHand(ropeCard);

        players.add(p0);
        players.add(p1);
        players.add(p2);
        board.setPlayers(players);

        Solution suggestion = new Solution(mustardCard, ropeCard, conservatoryCard);
        Card disproved = board.handleSuggestion(suggestion, p0);

        assertEquals(mustardCard, disproved, 
            "First eligible player in order (P1) should disprove the suggestion.");
    }
}