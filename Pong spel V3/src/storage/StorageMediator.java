package storage;

import java.util.Set;

import model.User;

public interface StorageMediator{
    
	/**
	 * Fills the Usermanagement singleton with users
	 */
	Set<User> load();
    
    /**
     * Saves all users in the Usermanagement singleton 
     */
    void save();
    
}
