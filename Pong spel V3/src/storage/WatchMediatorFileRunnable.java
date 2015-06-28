package storage;

import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;

import static java.nio.file.StandardWatchEventKinds.*;
import java.io.*;
import java.util.*;

import remote.RmiServer;

/**
 * @author JSF
 */
public class WatchMediatorFileRunnable implements Runnable {

    private final WatchService watcher;  // the service object who processes events for us
    private final Map<WatchKey, Path> keys; // the map of WatchKey's and belonging Path

    /**
     * Creates a WatchService and registers the given directory
     */
    public WatchMediatorFileRunnable() throws IOException {
    	Path dir = Paths.get("");
    	
        // create a default WatchService
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();  // map of watchkeys and path belonging to it
        
        // register Path to watch
        register(dir);
    }

    @SuppressWarnings("unchecked")
    /**
     * utility method to get the context out of the WatchEvent
     */
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Register the given directory with the WatchService First, create a
     * WatchKey object which connects all event types to a certain path, and add
     * it to the WatchService. keep an own Map for tracing
     *
     */
    private void register(Path dir) throws IOException {

        WatchKey key = dir.register(this.watcher, ENTRY_MODIFY);
        
        keys.put(key, dir);
    }

    @Override
    /**
     * Process all events for keys queued to the watcher.
     */
    public void run() {
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                // get event kind
                Kind<?> kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;  // something unexpected happened, let's ignore this
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path filename = ev.context();
                Path child = dir.resolve(filename);
                
                String editedFilename = child.getFileName().toString();
                String lookingForFilename = RmiServer.mediatorChoice.getName(); 
                if(editedFilename.equals(lookingForFilename)){
                	// Change the storage mediator on the RmiServer
                    RmiServer.changeMediatorOnInput();
                }
                
            }

            // reset key (because you just handled it, and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
}
