package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card; 
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution; 

class GameSetupTests {

	private static Board board;

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout306.csv", "ClueSetup306.txt");
		board.initialize();
	}

	@Test
	public void testPlayersLoaded() {
		List<Player> players = board.getPlayers();
		
		// Expect 6 total players (1 Human, 5 Computer)
		assertEquals(6, players.size());

		// Test Human player setup
		assertTrue(players.get(0) instanceof HumanPlayer);
		assertEquals("Miss Scarlet", players.get(0).getName());
		assertEquals(Color.RED, players.get(0).getColor());

		// Test Computer player setup
		assertTrue(players.get(1) instanceof ComputerPlayer);
		assertEquals("Colonel Mustard", players.get(1).getName());
	}

	@Test
	public void testDeckLoaded() {
		List<Card> deck = board.getDeck();
		
		// Expect 21 cards total (9 rooms + 6 people + 6 weapons)
		assertEquals(21, deck.size());

		int peopleCount = 0;
		int roomCount = 0;
		int weaponCount = 0;

		for (Card c : deck) {
			if (c.getCardType() == CardType.PERSON) {
				peopleCount++;
			} else if (c.getCardType() == CardType.ROOM) {
				roomCount++;
			} else if (c.getCardType() == CardType.WEAPON) {
				weaponCount++;
			}
		}

		assertEquals(6, peopleCount);
		assertEquals(9, roomCount);
		assertEquals(6, weaponCount);
	}

	@Test
	public void testDeal() {
		board.deal();
		Solution solution = board.getSolution();

		// 1. Check solution composition
		assertNotNull(solution.getPerson());
		assertEquals(CardType.PERSON, solution.getPerson().getCardType());

		assertNotNull(solution.getRoom());
		assertEquals(CardType.ROOM, solution.getRoom().getCardType());

		assertNotNull(solution.getWeapon());
		assertEquals(CardType.WEAPON, solution.getWeapon().getCardType());

		// 2. Check card hands distribution
		List<Player> players = board.getPlayers();
		Set<Card> dealtCards = new HashSet<>();

		int expectedHandSize = (21 - 3) / players.size(); // 18 cards / 6 players = 3 cards per hand

		for (Player p : players) {
			assertEquals(expectedHandSize, p.getHand().size());
			for (Card c : p.getHand()) {
				// Verify no card is dealt to more than one player
				assertFalse(dealtCards.contains(c));
				dealtCards.add(c);
			}
		}

		// Verify all 18 non-solution cards were dealt
		assertEquals(18, dealtCards.size());
	}
}