package remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import storage.DatabaseMediator;
import storage.SerializationMediator;

import model.GameManagement;
import model.UserManagement;

public class RmiServer{
	
	private static File mediatorChoice = new File("mediatorChoice");
	
	public static void main(String[] args){
        try{
        	InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Server: Started and open on IP address: " + localhost.getHostAddress());
            
            new RmiServer();
            
            // Start a thread to change mediators
            new Thread(new Runnable(){
            	public void run(){
            		Scanner scanner = new Scanner(System.in);
            		
            		// Get the first input from the mediator choice input, which knows what was used last time
            		String input = "";
            		boolean init = true;
            		try(FileInputStream stream = new FileInputStream(RmiServer.mediatorChoice)){
            			input = String.valueOf(stream.read());
            		}catch(IOException exception){
            			System.err.println("Could not read the mediator choice of the last session");
            		}
            		
            		while(true){
            			// Switch to the new choice, and save
            			try(FileOutputStream stream = new FileOutputStream(RmiServer.mediatorChoice)){
        					switch(input){
	            				case "1":
	            					System.out.println("You choose for the serialization");
	            					stream.write(1);
	            					UserManagement.setStorageType(SerializationMediator.class);
	            					if(init){
	            						UserManagement.load();
	            						init = false;
	            					}else{
	            						UserManagement.save();
	            					}
	            					break;
	            				case "2":
	            					System.out.println("You choose for the database");
	            					stream.write(2);
	            					UserManagement.setStorageType(DatabaseMediator.class);
	            					if(init){
	            						UserManagement.load();
	            						init = false;
	            					}else{
	            						UserManagement.save();
	            					}
	            					break;
	            				default:
	            					System.err.println("You did not provide a legal input, try again");
	            					break;
	            			}
    					}catch(IOException exception){
    						System.err.println("Could not save the new choice");
    					}
    					System.out.println();
    					System.out.println("You can switch between the mediators");
            			System.out.println("Choose '1' for the serialization mediatior");
            			System.out.println("Choose '2' for the database mediatior");
            			System.out.println();
            			input = scanner.next();
            		}
            		
            	}
            }).start();
        }catch(UnknownHostException ex){}
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
