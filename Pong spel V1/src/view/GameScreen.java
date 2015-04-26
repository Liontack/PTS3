package view;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameScreen extends JPanel{
	
	JLabel label_scorePlayer1 = new JLabel();
	JLabel label_scorePlayer2 = new JLabel();
	JLabel label_scorePlayer3 = new JLabel();
	
	// Draw the rectangles with power up information
	
	// Draw the game field
	
	public GameScreen(){
		this.setLayout(null);
		
		// Init gui
		label_scorePlayer1.setSize(300, 50);
		label_scorePlayer1.setLocation(8 , 8);
		label_scorePlayer1.setForeground(Color.red);
		this.add(label_scorePlayer1);

		label_scorePlayer2.setSize(300, 50);
		label_scorePlayer2.setLocation(8 , 28);
		label_scorePlayer1.setForeground(Color.blue);
		this.add(label_scorePlayer2);

		label_scorePlayer3.setSize(300, 50);
		label_scorePlayer3.setLocation(8 , 48);
		label_scorePlayer1.setForeground(Color.green);
		this.add(label_scorePlayer3);
		
	}
	
}
