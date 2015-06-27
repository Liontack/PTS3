package util.storage;

import java.util.HashSet;
import java.util.Set;

import model.User;
import storage.StorageMediator;

public class DummyMediator implements StorageMediator{

	public Set<User> load(){
		return new HashSet<User>();
	}

	public void save(){
		
	}
	
}
