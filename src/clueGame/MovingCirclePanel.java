// LO20P Java Graphics
// Jaylene Khourn, Trisha Varadaraj

package clueGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MovingCirclePanel extends JPanel {
	
	private int x = 0;
	private int y = 200;

    @Override protected void paintComponent(Graphics g) {
    	super.paintComponent(g);;
    	g.setColor(Color.BLUE);
    	g.fillOval(x,  y,  60,  60);
    }

    public void move() {
    	x += 5;
    	if (x > getWidth()) {
    		x = 0;
    	}
    	repaint();
    }

    public static void main(String[] args) { 
        // Create the frame
        JFrame frame = new JFrame("Moving Circle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create the panel
        MovingCirclePanel panel = new MovingCirclePanel();
        frame.add(panel);

        // Create timer that calls move() every 50 milliseconds
        Timer timer = new Timer(50, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                panel.move();
            }
        }); 
        timer.start();

        // Make frame visible 
        frame.setVisible(true);
    }
}

