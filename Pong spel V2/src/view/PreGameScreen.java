package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PreGameScreen extends JPanel{
	
	JButton btn_back = new JButton("Terug");
	JLabel label_playername1 = new JLabel();
	JLabel label_playername2 = new JLabel();
	JLabel label_playername3 = new JLabel();
	JButton btn_play = new JButton("Start spel");
	
	public PreGameScreen(){
		this.setLayout(null);
		
		// Init gui
		btn_back.setSize(100, 25);
		btn_back.setLocation(0, 0);
		btn_back.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				Program.switchToPanel(StartScreen.class);
			}
		});
		this.add(btn_back);
		
		label_playername1.setSize(100, 25);
		label_playername1.setLocation((Program.windowSize.width * 1 / 2) - (label_playername1.getWidth() * 1 / 2) , Program.windowSize.height * 2 / 10);
		label_playername1.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				Program.switchToPanel(StartScreen.class);
			}
		});
		this.add(label_playername1);
		
		label_playername2.setSize(100, 25);
		label_playername2.setLocation((Program.windowSize.width * 1 / 2) - (label_playername2.getWidth() * 1 / 2) , Program.windowSize.height * 4 / 10);
		label_playername2.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				Program.switchToPanel(StartScreen.class);
			}
		});
		this.add(label_playername2);
		
		label_playername3.setSize(100, 25);
		label_playername3.setLocation((Program.windowSize.width * 1 / 2) - (label_playername3.getWidth() * 1 / 2) , Program.windowSize.height * 6 / 10);
		label_playername3.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				Program.switchToPanel(StartScreen.class);
			}
		});
		this.add(label_playername3);
		
		btn_play.setSize(200, 50);
		btn_play.setLocation((Program.windowSize.width * 1 / 2) - (btn_play.getWidth() * 1 / 2) , Program.windowSize.height * 8 / 10);
		btn_play.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				Program.switchToPanel(GameScreen.class);
			}
		});
		this.add(btn_play);
	}
	
	public void initScreen(){
		btn_play.setEnabled(false);
	}
	
}
