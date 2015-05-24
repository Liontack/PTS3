package view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameScreen extends JPanel{
	
	JLabel label_scorePlayer1 = new JLabel("0");
	JLabel label_scorePlayer2 = new JLabel("0");
	JLabel label_scorePlayer3 = new JLabel("0");
	
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
		label_scorePlayer2.setForeground(Color.blue);
		this.add(label_scorePlayer2);
		
		label_scorePlayer3.setSize(300, 50);
		label_scorePlayer3.setLocation(8 , 48);
		label_scorePlayer3.setForeground(Color.green);
		this.add(label_scorePlayer3);
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(Program.activeGame != null){
			Program.activeGame.draw(g);
		}
		
	}
	
}
