/*
 * CardsPanel Class
 * 
 * Shows the player's known cards arranged by categories: People, Rooms, Weapons
 * Shows both cards in hand and seen cards
 * 
 * @author Jaylene Khourn, Trisha Varadaraj
 * 
 */

package clueGame;
import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class CardsPanel extends JPanel {

    // Sub-panels used to hold and dynamically update card lists
    private JPanel handPeoplePanel, seenPeoplePanel; // Instance Variables . 
    private JPanel handRoomsPanel, seenRoomsPanel;
    private JPanel handWeaponsPanel, seenWeaponsPanel;

    // constructor creates the Known Cards panel and initializes all 6 card display sections
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

    // creates one category section containing separate In Hand and Seen card panels 
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

    // creates a non-editable text field for a card
    private JTextField makeCardField(String name, Color color) {
        JTextField field = new JTextField(name);
        field.setEditable(false);
        field.setBackground(color);
        field.setForeground(Color.BLACK);
        field.setMaximumSize(new Dimension(180, 25));
        field.setPreferredSize(new Dimension(180, 25));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    
    // Updates a sub-panel with text fields for provided cards. Has a field with "None" if list is empty/ null
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
    
    // updates a seen card panel with player specific colors
    public void setSeenCardList(JPanel targetPanel, List<String> cardNames, List<Color> colors) {
        targetPanel.removeAll();

        // display None if no cards have been seen.
        if (cardNames == null || cardNames.isEmpty()) {
            targetPanel.add(makeCardField("None", Color.WHITE));
        } else {
            if (colors == null || colors.size() != cardNames.size()) {
                throw new IllegalArgumentException(
                        "Each seen card must have a player color.");
            }
            for (int i = 0; i < cardNames.size(); i++) {
                targetPanel.add(makeCardField(cardNames.get(i), colors.get(i)));
            }
        }

        // refresh
        targetPanel.revalidate();
        targetPanel.repaint();
    }
    
    // updates all 3 categories of cards in the human players hand
    public void setHandCards(List<String> people, List<String> rooms, List<String> weapons) {
        setCardList(handPeoplePanel, people);
        setCardList(handRoomsPanel, rooms);
        setCardList(handWeaponsPanel, weapons);
    }
    
    // updates seen people cards and their player colors
    public void setSeenPeople(List<String> cards, List<Color> colors) {
        setSeenCardList(seenPeoplePanel, cards, colors);
    }
    
    // updates seen room cards and their player colors
    public void setSeenRooms(List<String> cards, List<Color> colors) {
        setSeenCardList(seenRoomsPanel, cards, colors);
    }
    
    // updates seen weapon cards and their player colors
    public void setSeenWeapons(List<String> cards, List<Color> colors) {
        setSeenCardList(seenWeaponsPanel, cards, colors);
    }

    // main method to test + preview GUI frame layout standalone
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // create the Known Cards panel.
            CardsPanel panel = new CardsPanel();

            // test displaying cards in the human player's hand
            panel.setHandCards(Arrays.asList("Steve"), Arrays.asList("Enchantment Room"), Arrays.asList("Diamond Sword"));

            // test seen people cards using player colors
            panel.setSeenPeople(Arrays.asList("Alex", "Jordan"), Arrays.asList(Color.RED, Color.BLUE));

            // test seen room cards
            panel.setSeenRooms(Arrays.asList("Ancient City"), Arrays.asList(Color.GREEN));

            // test seen weapon cards
            panel.setSeenWeapons(Arrays.asList("Bow", "Trident"), Arrays.asList(Color.RED, Color.BLUE));

            JFrame testFrame = new JFrame("Known Cards Test");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setSize(300, 700);
            testFrame.setContentPane(panel);
            testFrame.setLocationRelativeTo(null);
            testFrame.setVisible(true);
        });
    }
}



