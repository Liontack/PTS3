package remote;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.GameManagement;
import model.UserManagement;

public class RmiServer{
	
	public static void main(String[] args){
        try{
        	InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Server: Started and open on IP address: " + localhost.getHostAddress());
            
            new RmiServer();
        }catch(UnknownHostException ex){}
    }
    
    private Registry registry = null;
    private int registryPort = 1099;
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
            System.out.println("Server: Registry created on port number " + registryPort);
        }catch(RemoteException ex){
            System.err.println("Server: Cannot create registry");
            System.err.println("Server: RemoteException: " + ex.getMessage());
            registry = null;
        }
    }
    
    private void bindManagersUsingRegistry(){
        /*try{
            registry.rebind("gameManagement", gameManagement);
            registry.rebind("userManagement", userManagement);
            System.out.println("SERVER: managers are bound to effectenBeurs");
        }catch(RemoteException ex){
            System.err.println("Server: Cannot bind managers");
            ex.printStackTrace();
        }*/
    }
    
}
