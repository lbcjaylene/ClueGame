package clueGame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;


 // CardsPanel shows the player's known cards arranged by categories  People, Rooms, Weapons. 
// shows both cards in hand, and seen cards.

public class CardsPanel extends JPanel {

    // Sub-panels used to hold and dynamically update card lists
    private JPanel handPeoplePanel, seenPeoplePanel; // Instance Variables . 
    private JPanel handRoomsPanel, seenRoomsPanel;
    private JPanel handWeaponsPanel, seenWeaponsPanel;

    public CardsPanel() {
        // 3x1 grid so People, Rooms, and Weapons stack vertically
        setLayout(new GridLayout(3, 1, 5, 5));
        setBorder(new TitledBorder("Known Cards"));

        // Initialize sub-panels for People
        handPeoplePanel =new JPanel();
        seenPeoplePanel= new JPanel();
        add(createCategorySection("People", handPeoplePanel, seenPeoplePanel));

        // Rooms
        handRoomsPanel= new JPanel();
        seenRoomsPanel = new JPanel();
        add(createCategorySection("Rooms", handRoomsPanel, seenRoomsPanel));

        // Weapons
        handWeaponsPanel= new JPanel();
        seenWeaponsPanel = new JPanel();
        add(createCategorySection("Weapons", handWeaponsPanel, seenWeaponsPanel));

        // Show the default empty state ("None") when initialized
        setCardList(handPeoplePanel,new ArrayList<>());
        setCardList(seenPeoplePanel, new ArrayList<>());
        setCardList(handRoomsPanel, new ArrayList<>());
        setCardList(seenRoomsPanel,new ArrayList<>());
        setCardList(handWeaponsPanel, new ArrayList<>());
        setCardList(seenWeaponsPanel,new ArrayList<>());
    }

    
     // Build a single category box .    
  
    private JPanel createCategorySection(String title, JPanel handPanel, JPanel seenPanel) {
        JPanel section = new JPanel(); // containing box for specific category 
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS)); // BoxLayout going vertically 
        section.setBorder(new TitledBorder(title)); // Titled Border around specific seection 

        // Add "In Hand" label and panel container
        JLabel handLabel= new JLabel("In Hand:");
        handLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(handLabel);
        
        handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.Y_AXIS));
        handPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(handPanel);

        // Add vertical spacer in between of the sections
        section.add(Box.createVerticalStrut(5));

        // "Seen" label + panel container
        JLabel seenLabel = new JLabel("Seen:");
        seenLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(seenLabel);

        seenPanel.setLayout(new BoxLayout(seenPanel, BoxLayout.Y_AXIS));
        seenPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(seenPanel);

        return section;
    }

    
     // Helper method for styled, non-editable text fields for cards.
     
    private JTextField makeCardField(String name, Color color) {
        JTextField field = new JTextField(name);
        field.setEditable(false);
        field.setBackground(color);
        field.setMaximumSize(new Dimension(180, 25));
        field.setPreferredSize(new Dimension(180, 25));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    
     // Updates a sub-panel with text fields for provided cards. Has a field with "None" if list is empty/ null.
    
    public void setCardList(JPanel targetPanel, java.util.List<String> cardNames) {
        targetPanel.removeAll();

        // Check if there are cards to show
        if (cardNames== null||cardNames.isEmpty()){
            targetPanel.add(makeCardField("None",Color.WHITE)); // "NONE" with a white background 
        } else {
            for (String name:cardNames) {
                targetPanel.add(makeCardField(name,Color.WHITE));
            }
        }

        // Refresh UI areas 
        targetPanel.revalidate();
        targetPanel.repaint();
    }

    // Main method to test + preview GUI frame layout standalone
    public static void main(String[] args) {
        JFrame testFrame = new JFrame("Known Cards Test");
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setSize(220, 700);
        testFrame.add(new CardsPanel());
        testFrame.setVisible(true);
    }
}



