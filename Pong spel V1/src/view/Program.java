package view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.server.User;

public class Program{
	
	// Logged in user
	public static User loggedInUser = null;
	
	// Window specifications
	public static String title = "Pong spel";
	public static Dimension windowSize = new Dimension(800, 600);
	
	// MainFrame with all panels
	private static JFrame mainFrame;
	public static JPanel cardStack,
						startScreen;
	
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
		defineCard(RegistrationScreen.class);
		defineCard(PreGameScreen.class);
		defineCard(GameScreen.class);
	}
	public static void showFirstCard(){
		// Show one of the cards first
		mainFrame.add(cardStack);
		switchToPanel(RegistrationScreen.class);//XXX Change to StartScreen
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
