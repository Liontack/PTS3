package remote;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import view.GameScreen;
import view.PreGameScreen;
import view.Program;

import fontys.observer.RemotePropertyListener;

public class PreGameScreenReceiver extends UnicastRemoteObject implements RemotePropertyListener, Serializable{
	private static final long serialVersionUID = 1L;
	
	private PreGameScreen preGameScreen;
	
	public PreGameScreenReceiver(PreGameScreen screen) throws RemoteException{
		this.preGameScreen = screen;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) throws RemoteException{
		// Update usernames if there is a change in players in the game
		if(event.getNewValue() instanceof PlayersInGameUpdate){
			// Update the names in the gui
			PlayersInGameUpdate players = (PlayersInGameUpdate) event.getNewValue();
			preGameScreen.usernames = players.usernames;
			preGameScreen.repaint();
		}
		
		// Go on to the game screen if an BarricadePositions is retrieved,
		// this is send on game start
		else if(event.getNewValue() instanceof BarricadePositions){

			// Stop listening for updates
			try{
				Program.secured.removeListener(this, "game" + Program.gameID);
			}catch(RemoteException exception){
				System.err.println("PreGameScreen: Could not stop listening to this game's updates");
			}
			
			// Go to GameScreen
			if(Program.getActivePanel() == preGameScreen){
				Program.switchToPanel(GameScreen.class);
			}
		}
	}
	
}
