package clueGame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;


 // GameControlPanel turns the control panel at the bottom of the Clue game GUI. This has turn info, roll result, buttons, guess displays, and guess results. 
 
public class GameControlPanel extends JPanel {

    // Instance Variables (GUI Components)
    // Declared here so our setter methods can update their values later

	private JTextField turnField;
    private JTextField rollField;
    private JTextField guessField;
    private JTextField guessResultField;
    private JButton nextPlayerButton; 
    private JButton makeAccusationButton; 

    
     // Constructor: Builds the user interface layout and with all the sub-components.
  
    public GameControlPanel() {
        // Main panel uses a 2-row, 1-column grid layout (Top half and Bottom half)
        setLayout(new GridLayout(2, 1));

        // Create the top half and bottom half 
        JPanel topPanel= createTopPanel();
        JPanel bottomPanel = createBottomPanel();

        add(topPanel);
        add(bottomPanel);
    }

    // Break down panel construction .    

     // Creates the top half of the control panel (Turn, Roll, Accuse, Next).

    private JPanel createTopPanel() {
        // 1 row by 4 columns grid layout
        JPanel panel= new JPanel();
        panel.setLayout(new GridLayout(1, 4));

        // Whose turn?     
        JPanel turnPanel= new JPanel();
        turnPanel.setLayout(new GridLayout(2, 1)); // 2 rows: top is label, bottom is text field
        
        JLabel turnLabel = new JLabel("Whose turn?", SwingConstants.CENTER);
        turnField = new JTextField();
        turnField.setEditable(false); // Make it read-only so the player can't type into it
        
        turnPanel.add(turnLabel);
        turnPanel.add(turnField);

        // "Roll:" 
        JPanel rollPanel = new JPanel();
        rollPanel.setLayout(new FlowLayout()); // FlowLayout places elements side-by-side naturally
        
        JLabel rollLabel= new JLabel("Roll:");
        rollField = new JTextField(5); // 5 columns wide
        rollField.setEditable(false); // read-only
        
        rollPanel.add(rollLabel);
        rollPanel.add(rollField);

        // 3, Accusation Button
        makeAccusationButton= new JButton("Make Accusation");

        // 4: Next Player Button
        nextPlayerButton = new JButton("NEXT!");

        // Add all 4 sub-sections to the top panel from left to right
        panel.add(turnPanel);
        panel.add(rollPanel);
        panel.add(makeAccusationButton);
        panel.add(nextPlayerButton);

        return panel;
    }

 
     // Creates the bottom half of the control panel (2 columns: Guess and Guess Result).
 
    private JPanel createBottomPanel() {
        // 1 row by 2 columns grid layout 
        JPanel panel= new JPanel();
        panel.setLayout(new GridLayout(1, 2));

        // Sub-section: Guess Panel 
        JPanel guessPanel = new JPanel(new GridLayout(1, 1));
        guessPanel.setBorder(new TitledBorder("Guess"));     
        
        guessField= new JTextField();
        guessField.setEditable(false);
        guessPanel.add(guessField);

        // Guess Result Panel (with a titled border around it)
        JPanel guessResultPanel =new JPanel(new GridLayout(1, 1));
        guessResultPanel.setBorder(new TitledBorder("Guess Result")); // Draws a line box around panel with title
        
        guessResultField = new JTextField();
        guessResultField.setEditable(false);
        guessResultPanel.add(guessResultField);

        // Add both sub-sections to the bottom panel
        panel.add(guessPanel);
        panel.add(guessResultPanel);

        return panel;
    }

    
     // Updates the player name display and changes the background color to match .
 
    public void setTurn(String playerName, Color bgColor) {
        turnField.setText(playerName);
        turnField.setBackground(bgColor);
    }

    
     
    public void setRoll(int rollValue) {
        rollField.setText(Integer.toString(rollValue));
    }

    
     // Updates the text in the Guess section.
     
    public void setGuess(String guessText) {
        guessField.setText(guessText);
    }

    
     // Updates the text in the Guess Result section.
    public void setGuessResult(String resultText) {
        guessResultField.setText(resultText);
    }

    // MAIN: Used to preview and test GUI component standalone . 
    public static void main(String[] args) {
        // Create an instance of our panel
        GameControlPanel panel = new GameControlPanel();

        panel.setTurn("Steve", Color.BLUE);
        panel.setRoll(5);
        panel.setGuess("I have no guess!");
        panel.setGuessResult("So you have nothing?");


        JFrame frame = new JFrame("Clue Game Control Panel");
        frame.setContentPane(panel);  // Put panel inside window 
        frame.setSize(750, 180);                               
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setVisible(true);   // Make window visible
    }
}