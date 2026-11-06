
package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class GameSetupTests {

    private static Board board;

    // initializes the board before running the setup tests
    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
    }

    // tests that all six players are loaded from the setup file
    @Test
    public void testPlayerCount() {
        assertEquals(6, board.getPlayers().size());
    }

    // tests that HumanPlayer is a subclass of Player
    @Test
    public void testHumanPlayer() {
        Player player = new HumanPlayer("Steve", "Blue", 0, 5);
        assertTrue(player instanceof HumanPlayer);
    }

    // tests that ComputerPlayer inherits from Player
    @Test
    public void testComputerPlayer() {
        Player player = new ComputerPlayer("Alex", "Orange", 0, 17);
        assertTrue(player instanceof ComputerPlayer);
    }

    // tests that a player's name is stored correctly
    @Test
    public void testPlayerName() {
        Player player = new HumanPlayer("Steve", "Blue", 0, 5);
        assertEquals("Steve", player.getName());
    }

    // tests that a player's color and location are stored correctly
    @Test
    public void testPlayerColorAndLocation() {
        Player player = new HumanPlayer("Steve", "Blue", 0, 5);

        assertEquals("Blue", player.getColor());
        assertEquals(0, player.getRow());
        assertEquals(5, player.getColumn());
    }

    // tests that one human and five computer players are loaded
    @Test
    public void testPlayerTypes() {
        int humanCount = 0;
        int computerCount = 0;

        for (Player player : board.getPlayers()) {
            if (player instanceof HumanPlayer) {
                humanCount++;
            } else if (player instanceof ComputerPlayer) {
                computerCount++;
            }
        }

        assertEquals(1, humanCount);
        assertEquals(5, computerCount);
    }

    // tests that player information is loaded from the setup file
    @Test
    public void testPlayerInformation() {
        Player player = board.getPlayers().get(0);

        assertEquals("Steve", player.getName());
        assertEquals("Blue", player.getColor());
        assertEquals(0, player.getRow());
        assertEquals(5, player.getColumn());
        assertTrue(player instanceof HumanPlayer);
    }

    // tests that the correct Minecraft players are loaded
    @Test
    public void testPlayersLoaded() {
        List<Player> players = board.getPlayers();

        assertEquals(6, players.size());

        // check the human player
        assertTrue(players.get(0) instanceof HumanPlayer);
        assertEquals("Steve", players.get(0).getName());
        assertEquals("Blue", players.get(0).getColor());

        // check the computer player
        assertTrue(players.get(1) instanceof ComputerPlayer);
        assertEquals("Alex", players.get(1).getName());
    }

    // tests that the deck contains 21 cards
    @Test

    public void testDeckLoaded() {
        List<Card> deck = board.getDeck();

        assertEquals(21, deck.size());

        int peopleCount = 0;
        int roomCount = 0;
        int weaponCount = 0;

        // count each type of card
        for (Card card : deck) {
            if (card.getCardType() == CardType.PERSON) {
                peopleCount++;
            } else if (card.getCardType() == CardType.ROOM) {
                roomCount++;
            } else if (card.getCardType() == CardType.WEAPON) {
                weaponCount++;
            }
        }

        assertEquals(6, peopleCount);
        assertEquals(9, roomCount);
        assertEquals(6, weaponCount);
    }

    // tests the solution and dealing of cards
    @Test
    public void testDeal() {
        board.deal();
        Solution solution = board.getSolution();

        // check that the solution contains one card of each type
        assertNotNull(solution);
        assertNotNull(solution.getPerson());
        assertEquals(CardType.PERSON, solution.getPerson().getCardType());

        assertNotNull(solution.getRoom());
        assertEquals(CardType.ROOM, solution.getRoom().getCardType());

        assertNotNull(solution.getWeapon());
        assertEquals(CardType.WEAPON, solution.getWeapon().getCardType());

        // check that all remaining cards are dealt
        List<Player> players = board.getPlayers();
        Set<Card> dealtCards = new HashSet<>();

        int expectedHandSize = (21 - 3) / players.size();

        for (Player player : players) {
            assertEquals(expectedHandSize, player.getHand().size());

            for (Card card : player.getHand()) {
                // no card should appear in two hands
                assertFalse(dealtCards.contains(card));
                dealtCards.add(card);
            }
        }

        assertEquals(18, dealtCards.size());
    }
}
