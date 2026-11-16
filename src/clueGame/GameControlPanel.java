/*
 * GameControlPanel Class
 * 
 * Creates the control panel displayed at the bottom of the Clue game. The panel displays
 * the current player's turn, die roll, guess, and guess result. It also provides buttons
 * for moving to the next player and making an accusation
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GameControlPanel extends JPanel {
	
	// instanee variables for the GUI components
	private JTextField turnField;
    private JTextField rollField;
    private JTextField guessField;
    private JTextField guessResultField;
    
    // buttons for player actions
    private JButton nextPlayerButton; 
    private JButton makeAccusationButton; 

    // constructor creates the main control panel and adds the top and bottom sections
    public GameControlPanel() {
    	
        // main panel uses a 2-row, 1-column grid layout (Top half and Bottom half)
        setLayout(new GridLayout(2, 1));

        // create the top half and bottom half 
        JPanel topPanel= createTopPanel();
        JPanel bottomPanel = createBottomPanel();

        // add both sections to the main panel
        add(topPanel);
        add(bottomPanel);
    }
    
    // creates the top section containing the current player, die roll, accusation button, and next player button
    private JPanel createTopPanel() {
    	
        // 1 row by 4 columns grid layout
        JPanel panel= new JPanel(new GridLayout(1, 4, 5, 5));
        
        // Create the panel displaying whose turn it is    
        JPanel turnPanel= new JPanel(new GridLayout(2, 1));
        JLabel turnLabel = new JLabel("Whose turn?", SwingConstants.CENTER);
        turnField = new JTextField();
        turnField.setEditable(false); // Make it read-only so the player can't type into it
        
        // add the label and text field to the turn panel
        turnPanel.add(turnLabel);
        turnPanel.add(turnField);

        // create the panel displaying the die roll
        JPanel rollPanel = new JPanel(new FlowLayout());
        JLabel rollLabel= new JLabel("Roll:");
        rollField = new JTextField(5); // 5 columns wide
        rollField.setEditable(false); // read-only
        
        // add the roll label and field
        rollPanel.add(rollLabel);
        rollPanel.add(rollField);

        // create the buttons for player actions
        makeAccusationButton= new JButton("Make Accusation");
        nextPlayerButton = new JButton("NEXT!");

        // add all 4 sub-sections to the top panel from left to right
        panel.add(turnPanel);
        panel.add(rollPanel);
        panel.add(makeAccusationButton);
        panel.add(nextPlayerButton);
        return panel;
    }
    
    // creates the bottom section containing the guess and guess result text fields
    private JPanel createBottomPanel() {
    	
        // 1 row by 2 columns grid layout 
        JPanel panel = new JPanel(new GridLayout(1, 2));

        // create the guess panel with a titled border
        JPanel guessPanel = new JPanel(new GridLayout(1, 1));
        guessPanel.setBorder(new TitledBorder("Guess"));     
        guessField= new JTextField();
        guessField.setEditable(false);
        guessPanel.add(guessField);

        // create the guess result panel with a titled border
        JPanel guessResultPanel =new JPanel(new GridLayout(1, 1));
        guessResultPanel.setBorder(new TitledBorder("Guess Result")); 
        guessResultField = new JTextField();
        guessResultField.setEditable(false);
        guessResultPanel.add(guessResultField);

        // add both sub-sections to the bottom panel
        panel.add(guessPanel);
        panel.add(guessResultPanel);
        return panel;
    }

    
    // updates the current player's name and background color
    public void setTurn(String playerName, Color bgColor) {
        turnField.setText(playerName);
        turnField.setBackground(bgColor);
    }
    
    // updates the current player and die roll using a Player object
    public void setTurn(Player player, int roll) {
    	Color playerColor = getPlayerColor(player.getColor());
    	setTurn(player.getName(), playerColor);
    	setRoll(roll);
    }
    
    // converts a player's color name into a Java Color object
    private Color getPlayerColor(String colorName) {
    	if (colorName == null) {
    		return Color.WHITE;
    	}
    	switch (colorName.toLowerCase()) {
    	case "red":
    		return Color.RED;
    	case "blue":
    		return Color.BLUE;
    	case "green":
    		return Color.GREEN;
    	case "yellow":
            return Color.YELLOW;

        case "orange":
            return Color.ORANGE;

        case "pink":
            return Color.PINK;

        case "cyan":
            return Color.CYAN;

        case "magenta":
            return Color.MAGENTA;

        case "white":
            return Color.WHITE;

        case "black":
            return Color.BLACK;

        default:
            return Color.WHITE;		
    	}
    }
    
    // updates the die roll displayed in the control panel
    public void setRoll(int rollValue) {
        rollField.setText(Integer.toString(rollValue));
    }

    
    // updates the text in the Guess section
    public void setGuess(String guessText) {
        guessField.setText(guessText);
    }

    // updates the text in the Guess Result section
    public void setGuessResult(String resultText) {
        guessResultField.setText(resultText);
    }

    // MAIN: Used to preview and test GUI component standalone
    public static void main(String[] args) {
    	
        // run Swing components on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {

            // create an instance of the control panel.
            GameControlPanel panel = new GameControlPanel();

            // create a frame to display the panel.
            JFrame frame = new JFrame("Clue Game Control Panel");

            // Add the control panel to the frame.
            frame.setContentPane(panel);

            // set the frame size and closing behavior.
            frame.setSize(750, 180);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // center the frame on the screen.
            frame.setLocationRelativeTo(null);

            // display the frame.
            frame.setVisible(true);

            // test updating the current player and background color
            panel.setTurn("Steve", Color.BLUE);

            // test updating the die roll
            panel.setRoll(5);

            // test updating the guess
            panel.setGuess("I have no guess!");

            // test updating the guess result
            panel.setGuessResult("So you have nothing?");
        });
    }
}




