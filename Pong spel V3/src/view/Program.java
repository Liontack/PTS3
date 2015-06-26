package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import remote.GameStartState;
import remote.ISecured;
import remote.IUnsecured;
import remote.RmiServer;

import model.Game;
import model.Player;

public class Program{
	
	// Logged in user
	public static String username = "";
	public static int userID = 0;
	
	// The game this loggedInUser is in
	public static int gameID;
	public static GameStartState gameStartState;
	
	// The offline game and player, if no one is logged in
	public static Game offlineGame = null;
	public static Player offlinePlayer = null;
	
	
	
	// Server interfaces
	public static Registry registry;
	public static IUnsecured unsecured;
	public static ISecured secured;
	private static String ipAddress = "";
	
	
	
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
	
	/*RMI METHODS*/
	static void connectToServer(final String ipAddress){
		// Keep searching for the server until you found him
		new Thread(new Runnable(){
			public void run(){
				while(registry == null){
					registry = locateRegistry(ipAddress, RmiServer.registryPort);
				}
				
		        if(registry != null){
		            System.out.println("Client: Registry located");
		            Program.ipAddress = ipAddress;
		            Program.unsecured = getUnsecuredInterface();
		            Program.secured = getSecuredInterface();
		        }else{
		            System.err.println("Client: Cannot locate registry");    
		        }
		        
		        if(Program.testConnection()){
		        	Program.setFeedback("Verbinding met de server gemaakt", Color.green);
		        }else{
		        	Program.setFeedback("Kon geen server vinden op " + ipAddress, Color.red);
		        	registry = null;
		        }
			}
		}).start();
	}
	
	private static Registry locateRegistry(String ipAddress, int portNumber){
        Registry reg = null;
        
        try{
            reg = LocateRegistry.getRegistry(ipAddress, portNumber);
        }catch(RemoteException ex){}
        
        return reg;
    }
    
    private static IUnsecured getUnsecuredInterface(){
        IUnsecured unsecured = null;
        
        try{
            unsecured = (IUnsecured) registry.lookup("unsecured");
        }catch(RemoteException ex){
            System.out.println("Client: Cannot get unsecured");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            unsecured = null;
        }catch(NotBoundException ex){
            System.out.println("Client: Cannot get unsecured");
            System.out.println("Client: NotBoundException: " + ex.getMessage());
            unsecured = null;
        }
        
        return unsecured;
    }
    
    private static ISecured getSecuredInterface(){
        ISecured secured = null;
        
        try{
            secured = (ISecured) registry.lookup("secured");
        }catch(RemoteException ex){
            System.out.println("Client: Cannot get secured");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            secured = null;
        }catch(NotBoundException ex){
            System.out.println("Client: Cannot get secured");
            System.out.println("Client: NotBoundException: " + ex.getMessage());
            secured = null;
        }
        
        return secured;
    }
	
    public static boolean testConnection(){// TODO test testConnection on two different devices and offline
    	if(ipAddress.isEmpty() || unsecured == null){
    		return false;
    	}
    	
    	boolean connected = false;
    	try{
    		connected = (unsecured.getUserRatings() != null);
    	}catch(RemoteException exception){}
    	
    	if(!connected){
			Program.setFeedback("De server is onbereikbaar", Color.red);
		}
    	return connected;
    }
    /*END RMI METHODS*/
	
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
		defineCard(ConnectServerScreen.class);
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
