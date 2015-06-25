package remote;

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
	
	public static void main(String[] args){
        try{
        	InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Server: Started and open on IP address: " + localhost.getHostAddress());
            
            new RmiServer();
            
            // Start a thread to change mediators
            new Thread(new Runnable(){
            	public void run(){
            		Scanner scanner = new Scanner(System.in);
            		while(true){
            			System.err.println();
    					System.out.println("You can switch between the mediators");
            			System.out.println("Choose '1' for the serialization mediatior");
            			System.out.println("Choose '2' for the database mediatior");
            			System.err.println();
    					switch(scanner.next()){
            				case "1":
            					System.out.println("You choose for the serialization");
            					UserManagement.setStorageType(SerializationMediator.class);
            					UserManagement.save();
            					break;
            				case "2":
            					System.out.println("You choose for the database");
            					UserManagement.setStorageType(DatabaseMediator.class);
            					UserManagement.save();
            					break;
            				default:
            					System.err.println("You did not provide a legal input, try again");
            					break;
            			}
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
