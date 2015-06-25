package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import model.User;
import model.UserManagement;

public class SerializationMediator implements StorageMediator{

	private static File usersFile = new File("users.data");
	
    public Set<User> load() {
    	System.out.println("SerializationMediator loading");
    	
    	Set<User> users = new HashSet<>();
		if(usersFile.exists()){
			try{
				ObjectInputStream reader = new ObjectInputStream(new FileInputStream(usersFile));
				while(true){
					try{
						User user = (User) reader.readObject();
						users.add(user);
					}catch(Exception exception){
						break;
					}
				}
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}
		
		return users;
    }
    
    public void save() {
    	System.out.println("SerializationMediator saving");
    	
    	try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFile));
			for(User user : UserManagement.getUsers()){
				out.writeObject(user);
			}
			out.close();
		}catch(IOException exception){}
    }
    
}
