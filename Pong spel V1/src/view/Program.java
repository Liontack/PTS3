package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.User;

public class Program{
	
	// Logged in user
	public static User loggedInUser = null;
	
	// Window specifications
	public static String title = "Pong spel";
	public static Dimension windowSize = new Dimension(800, 600);
	
	// MainFrame with all panels
	private static JFrame mainFrame;
	public static JPanel cardStack,
						feedbackPanel;
	
	private static JLabel label_feedback = new JLabel();
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                createFrame();
            }
        });
	}
	
	private static void createFrame(){
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.setSize(windowSize.width + 6, windowSize.height + 28); // Additions for window borders
		mainFrame.setTitle(title);
		mainFrame.setResizable(false);
		mainFrame.setLocationRelativeTo(null);
		
		createCardStack();
		defineCardsInStack();
		showFirstCard();
		
		// Set up feedback panel
		feedbackPanel = new JPanel();
		feedbackPanel.setSize(50, 50);
		feedbackPanel.setBackground(Color.black);
		mainFrame.add(feedbackPanel, BorderLayout.SOUTH);
		// Init feedback label
		feedbackPanel.add(label_feedback, BorderLayout.CENTER);
		
	}
	
	public static void setFeedback(String feedback, Color color){
		if(color == Color.red){
			color = new Color(255, 100, 100);
		}
		feedbackPanel.setBackground(color);
		label_feedback.setText(feedback);
		new Thread(new Runnable(){
			public void run(){
				try{
					Thread.sleep(5000);
					removeFeedback();
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	private static void removeFeedback(){
		label_feedback.setText("");
		feedbackPanel.setBackground(Color.black);
	}
	
	public static void createCardStack(){
		// Creating an cardStack, containing all possible cards(JPanels)
		cardStack = new JPanel();
		cardStack.setLayout(new CardLayout());
	}
	public static void defineCardsInStack(){
		// Defining all cards and add them to the stack
		defineCard(StartScreen.class);
		defineCard(LoginScreen.class);
		defineCard(PreGameScreen.class);
		defineCard(GameScreen.class);
	}
	public static void showFirstCard(){
		// Show one of the cards first
		mainFrame.add(cardStack);
		switchToPanel(StartScreen.class);
	}
	
	public static void defineCard(Class<? extends JPanel> panelClass){
		// Define a new card with given class and add to the card stack
		try{
			JPanel panel = panelClass.newInstance();
			panel.setFocusable(true);
			cardStack.add(panel, panelClass.getName());
		}catch(InstantiationException | IllegalAccessException e){
			System.err.println("Failed to create screen: '" + panelClass.getName() + "'");
		}
	}
	public static void switchToPanel(Class<? extends JPanel> panelClass){
		System.out.println("Switching to panel: '" + panelClass.getName() + "'");
		CardLayout cardLayout = (CardLayout)(cardStack.getLayout());
		cardLayout.show(cardStack, panelClass.getName());
		Program.getActivePanel().requestFocusInWindow();
	}
	public static Component getActivePanel(){
		Component[] panels = cardStack.getComponents();
		for(Component panel : panels){
			if(panel.isShowing()){
				return panel;
			}
		}
		return null;
	}
	
	public static void close(){
		mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
	}
	
}
