package remote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import storage.DatabaseMediator;
import storage.SerializationMediator;
import storage.WatchMediatorFileRunnable;

import model.GameManagement;
import model.UserManagement;

public class RmiServer{
	
	public static File mediatorChoice = new File("mediatorChoice.txt");
	private static File outputFile = new File("serverOutput.txt");
	
	private static int lastMediatorChoice;
	private static boolean init = true;
	
	public static void main(String[] args){
		// Set the System.out and System.err streams to the outputFile
		try{
			if(outputFile.exists()){
				outputFile.delete();
			}
			System.setOut(new PrintStream(outputFile));
			System.setErr(new PrintStream(outputFile));
		}catch(FileNotFoundException exception){
			System.err.println("Could not find the outputfile!");
		}
		
		// Start up the server
		try{
        	InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Server: Started and open on IP address: " + localhost.getHostAddress());
            
            new RmiServer();
            
            // Start a thread to change mediators
            new Thread(new Runnable(){
            	public void run(){
            		// Get the first input from the mediator choice input, which knows what was used last time
            		RmiServer.changeMediatorOnInput();
            		
            		try{
	    				// Watch for a update in the mediatorfile, this is the new input
	            		// this runnable calls changeMediatorOnInput when needed.
	            		new Thread(new WatchMediatorFileRunnable()).start();
            		}catch(IOException exception){
            			System.err.println("Could not watch for changes in the mediator!");
            		}
            	}
            }).start();
        }catch(UnknownHostException ex){}
    }
    
	public static void changeMediatorOnInput(){
		// Get the input from the mediator choice file
		String input = "";
		try(Scanner scanner = new Scanner(RmiServer.mediatorChoice)){
			input = String.valueOf(scanner.nextInt());
			scanner.close();
		}catch(Exception exception){
			System.out.println();
			System.err.println("Could not read the mediator choice of the last session");
		}
		
		// If the request is an actual change
		if(lastMediatorChoice != Integer.valueOf(input)){
			System.out.println();
			
			// Switch to the new choice, and save
			switch(input){
				case "1":
					System.out.println("You choose for the serialization");
					lastMediatorChoice = 1;
					UserManagement.setStorageType(SerializationMediator.class);
					if(RmiServer.init){
						UserManagement.load();
						RmiServer.init = false;
					}else{
						UserManagement.save();
					}
					break;
				case "2":
					System.out.println("You choose for the database");
					lastMediatorChoice = 2;
					UserManagement.setStorageType(DatabaseMediator.class);
					if(RmiServer.init){
						UserManagement.load();
						RmiServer.init = false;
					}else{
						UserManagement.save();
					}
					break;
				default:
					System.err.println("'" + input + "' is not a legal input, try again");
					break;
			}
		}
	}
	
	
	
    private Registry registry = null;
    public static int registryPort = 1099;
    private GameManagement gameManagement = null;
    private UserManagement userManagement = null;
    
    private RmiServer(){
        createRegistry();
        gameManagement = GameManagement.getInstance();
		userManagement = UserManagement.getInstance();
        
        if(registry != null && gameManagement != null && userManagement != null){
            bindManagersUsingRegistry();
        }
    }
    
    private void createRegistry(){
        try{
            registry = LocateRegistry.createRegistry(registryPort);
            System.out.println("SERVER: Registry created on port number " + registryPort);
        }catch(RemoteException ex){
            System.err.println("SERVER: Cannot create registry");
            System.err.println("SERVER: RemoteException: " + ex.getMessage());
            registry = null;
        }
    }
    
    private void bindManagersUsingRegistry(){
        try{
            registry.rebind("secured", gameManagement);
            registry.rebind("unsecured", userManagement);
            System.out.println("SERVER: managers are bound to registry");
        }catch(RemoteException ex){
            System.err.println("Server: Cannot bind managers");
            ex.printStackTrace();
        }
    }
    
}
