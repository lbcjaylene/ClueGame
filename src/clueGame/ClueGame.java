/*
 * ClueGame Class
 *
 * Main JFrame window for the Clue game. Initializes And arranges
 * Board, GameControlPanel, and CardsPanel And components.
 *
 * @author Jaylene Khourn, Trisha Varadaraj
 */

package clueGame;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class ClueGame extends JFrame {

    public ClueGame() {
        // Initialize 
        Board board= Board.getInstance();
        board.setConfigFiles("ClueLayout.csv","ClueSetup.txt");
        board.initialize();
        board.deal();

        // Configure JFrame window
        setTitle("Clue Game");
        setSize(900, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the side and bottom panels
        GameControlPanel controlPanel =new GameControlPanel();
        CardsPanel cardsPanel = new CardsPanel();

        // Add panels to the JFrame
        add(board,BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(cardsPanel, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        ClueGame game = new ClueGame();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }
}