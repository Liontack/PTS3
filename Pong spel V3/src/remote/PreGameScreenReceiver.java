package remote;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
			preGameScreen.ratings = players.ratings;
			preGameScreen.setPlayButtonEnabled();
			preGameScreen.repaint();
		}
		
		// Go on to the game screen if an BarricadePositions is retrieved,
		// this is send on game start
		else if(event.getNewValue() instanceof GameStartState){
			preGameScreen.gameIsStarted((GameStartState) event.getNewValue());
			
			// Announce the first round
			Program.setFeedback("Eerste ronde begint zo", Color.cyan);
		}
	}
	
}
