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
    private static Card steveCard, alexCard, diamondSwordCard, bowCard, enchantmentRoomCard, netherFortressCard;
    private Board board;

    // sets up the cards used for the accusation and suggestion tests
    @BeforeAll
    public static void setupCards() {
    	steveCard = new Card("Steve", CardType.PERSON);
        alexCard = new Card("Alex", CardType.PERSON);
        diamondSwordCard = new Card("Diamond Sword", CardType.WEAPON);
        bowCard = new Card("Bow", CardType.WEAPON);
        enchantmentRoomCard = new Card("Enchantment Room", CardType.ROOM);
        netherFortressCard = new Card("Nether Fortress", CardType.ROOM);
    }

    // sets the board answer before each test
    @BeforeEach
    public void setUp() {
        board = Board.getInstance();
        
        // sets a known answer so each accusation can be checked
        board.setTheAnswer(new Solution(steveCard, diamondSwordCard, enchantmentRoomCard));
    }

    // REQUIREMENT: Check an Accusation Tests

    // tests if an accusation with all correct cards returns true
    @Test
    public void testCheckAccusationCorrect() {
    	Solution correctAccusation = new Solution(steveCard, diamondSwordCard, enchantmentRoomCard);
        assertTrue(board.checkAccusation(correctAccusation), "Accusation with correct person, weapon, and room should return true.");
    }

    // tests if an accusation with the wrong person returns false
    @Test
    public void testCheckAccusationWrongPerson() {
        Solution wrongPerson = new Solution(alexCard, diamondSwordCard, enchantmentRoomCard);
        assertFalse(board.checkAccusation(wrongPerson), "Accusation with wrong person should return false.");
    }

    // tests if an accusation with the wrong weapon returns false
    @Test
    public void testCheckAccusationWrongWeapon() {
        Solution wrongWeapon = new Solution(steveCard, bowCard, enchantmentRoomCard);
        assertFalse(board.checkAccusation(wrongWeapon), "Accusation with wrong weapon should return false.");
    }

    // tests if an accusation with the wrong room returns false
    @Test
    public void testCheckAccusationWrongRoom() {
        Solution wrongRoom = new Solution(steveCard, diamondSwordCard, netherFortressCard);
        assertFalse(board.checkAccusation(wrongRoom), "Accusation with wrong room should return false.");
    }

    // REQUIREMENT: Player Disproves Suggestion Tests
    
    // tests if a player returns the only card that matches a suggestion
    @Test
    public void testDisproveSuggestionOneMatchingCard() {
        Player player = new ComputerPlayer("Test Player", "Red", 0, 0);
        player.updateHand(steveCard);
        player.updateHand(bowCard);
        Solution suggestion = new Solution(steveCard, diamondSwordCard, enchantmentRoomCard);
        Card disproved = player.disproveSuggestion(suggestion);
        assertEquals(steveCard, disproved, "Player with one matching card should return that card.");
    }

    // tests if a player randomly returns either matching card
    @Test
    public void testDisproveSuggestionMultipleMatchingCardsRandom() {
        Player player = new ComputerPlayer("Test Player", "Red", 0, 0);
        player.updateHand(steveCard);
        player.updateHand(diamondSwordCard);
        Solution suggestion = new Solution(steveCard, diamondSwordCard, enchantmentRoomCard);
        boolean returnedSteve = false;
        boolean returnedDiamondSword = false;

        // repeats the test to check that both matching cards can be returned
        for (int i = 0; i < 100; i++) {
            Card disproved = player.disproveSuggestion(suggestion);
            if (disproved.equals(steveCard)) returnedSteve = true;
            if (disproved.equals(diamondSwordCard)) returnedDiamondSword = true;
        }
        assertTrue(returnedSteve && returnedDiamondSword, "Player with multiple matching cards should randomly select between them.");
    }

    // tests if a player returns null when no cards match the suggestion
    @Test
    public void testDisproveSuggestionNoMatchingCards() {
        Player player = new ComputerPlayer("Test Player", "Red", 0, 0);
        player.updateHand(alexCard);
        player.updateHand(bowCard);
        Solution suggestion = new Solution(steveCard, diamondSwordCard, enchantmentRoomCard);
        assertNull(player.disproveSuggestion(suggestion), "Player with no matching cards should return null.");
    }

    // REQUIREMENT: Handle a Suggestion Made Tests
    
    // tests if a suggestion returns null when no players can disprove it
    @Test
    public void testHandleSuggestionNoOneDisproves() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new HumanPlayer("Human", "Red", 0, 0);
        Player p2 = new ComputerPlayer("Comp 1", "Blue", 0, 1);
        p1.updateHand(alexCard);
        p2.updateHand(bowCard);
        players.add(p1);
        players.add(p2);
        board.setPlayers(players);
        Solution suggestion = new Solution(steveCard, diamondSwordCard, enchantmentRoomCard);
        assertNull(board.handleSuggestion(suggestion, p1), "If no player can disprove, return null.");
    }

    // tests if the suggesting player is skipped when checking cards
    @Test
    public void testHandleSuggestionOnlySuggestingPlayerCanDisprove() {
        ArrayList<Player> players = new ArrayList<>();
        Player accuser = new ComputerPlayer("Accuser", "Red", 0, 0);
        Player p2 = new ComputerPlayer("Comp 1", "Blue", 0, 1);
        accuser.updateHand(steveCard);
        p2.updateHand(bowCard);
        players.add(accuser);
        players.add(p2);
        board.setPlayers(players);
        Solution suggestion = new Solution(steveCard, diamondSwordCard, enchantmentRoomCard);
        assertNull(board.handleSuggestion(suggestion, accuser), "Suggesting player's own cards should not disprove their suggestion; return null.");
    }

    // tests if a human player can disprove a suggestion
    @Test
    public void testHandleSuggestionOnlyHumanCanDisprove() {
        ArrayList<Player> players = new ArrayList<>();
        Player accuser = new ComputerPlayer("Accuser", "Red", 0, 0);
        HumanPlayer human = new HumanPlayer("Human", "Blue", 0, 1);
        human.updateHand(steveCard);
        players.add(accuser);
        players.add(human);
        board.setPlayers(players);
        Solution suggestion = new Solution(steveCard, diamondSwordCard, enchantmentRoomCard);
        Card disproved = board.handleSuggestion(suggestion, accuser);
        assertEquals(steveCard, disproved, "Human player returning single matching card should be returned.");
    }

    // tests if the first player in turn order who can disprove is selected
    @Test
    public void testHandleSuggestionQueryOrder() {
        ArrayList<Player> players = new ArrayList<>();
        Player p0 = new ComputerPlayer("P0", "Red", 0, 0);
        Player p1 = new ComputerPlayer("P1", "Blue", 0, 1);
        Player p2 = new ComputerPlayer("P2", "Green", 0, 2);
        p1.updateHand(steveCard);
        p2.updateHand(diamondSwordCard);

        // adds players in the order they should be asked
        players.add(p0);
        players.add(p1);
        players.add(p2);
        board.setPlayers(players);
        Solution suggestion = new Solution(steveCard, diamondSwordCard, enchantmentRoomCard);
        Card disproved = board.handleSuggestion(suggestion, p0);
        assertEquals(steveCard, disproved,  "First eligible player in order (P1) should disprove the suggestion.");
    }
}


