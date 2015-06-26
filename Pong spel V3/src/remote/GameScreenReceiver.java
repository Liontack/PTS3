package remote;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import view.GameScreen;
import fontys.observer.RemotePropertyListener;

public class GameScreenReceiver extends UnicastRemoteObject implements RemotePropertyListener, Serializable{
	private static final long serialVersionUID = 1L;
	
	private GameScreen gameScreen;
	
	public GameScreenReceiver(GameScreen screen) throws RemoteException{
		this.gameScreen = screen;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) throws RemoteException{
		// If it is a BarricadePositions; does never occur, because it starts listening when this was sent
		if(event.getNewValue() instanceof BarricadesState){
			// Set the barricades state
			gameScreen.drawOnlyGame.setBarricadesState((BarricadesState) event.getNewValue());
		}
		
		// If it is a GameUpdate
		else if(event.getNewValue() instanceof GameUpdate){
			// Update game values for drawing purposes only
			if(gameScreen.drawOnlyGame != null){
				gameScreen.drawOnlyGame.setGameUpdate((GameUpdate) event.getNewValue());
			}
			gameScreen.playerPoints = ((GameUpdate) event.getNewValue()).playerPoints;
		}
		
		// If it is a GameFinished
		else if(event.getNewValue() instanceof GameFinished){
			// Call the game finished method on the game screen.
			gameScreen.gameIsFinished();
		}
		
		gameScreen.repaint();
	}
	
}
