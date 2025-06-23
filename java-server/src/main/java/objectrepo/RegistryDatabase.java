// File: RegistryDatabase.java
package objectrepo;

import objectrepo.*;
import objectrepo.ObjectRepositoryProto.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryDatabase {
    private final ConcurrentHashMap<String, ObjectInfo> objectMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lastSeen = new ConcurrentHashMap<>();
    private final long ttlMillis;

    public RegistryDatabase(long ttlSeconds) {
        this.ttlMillis = ttlSeconds * 1000;
        new Timer(true).schedule(new TimerTask() {
            public void run() {
                cleanup();
            }
        }, ttlMillis, ttlMillis);
    }

    public synchronized boolean register(ObjectInfo info) {
        objectMap.put(info.getObjectName(), info);
        lastSeen.put(info.getObjectName(), System.currentTimeMillis());
        System.out.println("[REGISTER] " + info.getObjectName());
        return true;
    }

    public synchronized boolean update(ObjectInfo info) {
        objectMap.put(info.getObjectName(), info);
        lastSeen.put(info.getObjectName(), System.currentTimeMillis());
        System.out.println("[UPDATE] " + info.getObjectName());
        return true;
    }

    public synchronized boolean deregister(String name) {
        objectMap.remove(name);
        lastSeen.remove(name);
        System.out.println("[DEREGISTER] " + name);
        return true;
    }

    public synchronized ObjectInfo get(String name) {
        return objectMap.get(name);
    }

    public synchronized List<ObjectInfo> listAll() {
        return new ArrayList<>(objectMap.values());
    }

    public synchronized boolean heartbeat(String name) {
        if (objectMap.containsKey(name)) {
            lastSeen.put(name, System.currentTimeMillis());
            return true;
        }
        return false;
    }

    private void cleanup() {
        long now = System.currentTimeMillis();
        for (String name : new HashSet<>(lastSeen.keySet())) {
            long last = lastSeen.get(name);
            if (now - last > ttlMillis) {
                System.out.println("[TIMEOUT] Removing " + name);
                deregister(name);
            }
        }
    }

    public synchronized void syncFrom(List<ObjectInfo> others) {
        for (ObjectInfo obj : others) {
            objectMap.put(obj.getObjectName(), obj);
            lastSeen.put(obj.getObjectName(), System.currentTimeMillis());
        }
        System.out.println("[SYNC] Received " + others.size() + " objects.");
    }
}
