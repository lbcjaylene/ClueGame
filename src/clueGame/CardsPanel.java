package clueGame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class CardsPanel extends JPanel {

    private JPanel peopleInHandPanel;
    private JPanel peopleSeenPanel;
    private JPanel roomsInHandPanel;
    private JPanel roomsSeenPanel;
    private JPanel weaponsInHandPanel;
    private JPanel weaponsSeenPanel;

    public CardsPanel() {
        setLayout(new GridLayout(3, 1, 5, 5));
        setBorder(new TitledBorder("Known Cards"));

        peopleInHandPanel = new JPanel();
        peopleSeenPanel = new JPanel();
        add(createCategoryPanel("People", peopleInHandPanel, peopleSeenPanel));

        roomsInHandPanel = new JPanel();
        roomsSeenPanel = new JPanel();
        add(createCategoryPanel("Rooms", roomsInHandPanel, roomsSeenPanel));

        weaponsInHandPanel = new JPanel();
        weaponsSeenPanel = new JPanel();
        add(createCategoryPanel("Weapons", weaponsInHandPanel, weaponsSeenPanel));

        updatePanel(peopleInHandPanel, null);
        updatePanel(peopleSeenPanel, null);
        updatePanel(roomsInHandPanel, null);
        updatePanel(roomsSeenPanel, null);
        updatePanel(weaponsInHandPanel, null);
        updatePanel(weaponsSeenPanel, null);
    }

    private JPanel createCategoryPanel(String title, JPanel inHandContainer, JPanel seenContainer) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder(title));

        JLabel inHandLabel = new JLabel("In Hand:");
        inHandLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(inHandLabel);

        inHandContainer.setLayout(new BoxLayout(inHandContainer, BoxLayout.Y_AXIS));
        inHandContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(inHandContainer);

        panel.add(Box.createVerticalStrut(5));

        JLabel seenLabel = new JLabel("Seen:");
        seenLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(seenLabel);

        seenContainer.setLayout(new BoxLayout(seenContainer, BoxLayout.Y_AXIS));
        seenContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(seenContainer);

        return panel;
    }

    private JTextField createCardTextField(String text, Color bgColor) {
        JTextField textField = new JTextField(text);
        textField.setEditable(false);
        textField.setBackground(bgColor);
        textField.setMaximumSize(new Dimension(180, 25));
        textField.setPreferredSize(new Dimension(180, 25));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        return textField;
    }

    public void updatePanel(JPanel container, List<String> cards) {
        container.removeAll();
        if (cards == null || cards.isEmpty()) {
            container.add(createCardTextField("None", Color.WHITE));
        } else {
            for (String cardName : cards) {
                container.add(createCardTextField(cardName, Color.WHITE));
            }
        }
        container.revalidate();
        container.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Known Cards Display Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(220, 700);
        frame.add(new CardsPanel());
        frame.setVisible(true);
    }
}