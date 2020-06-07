package com.forkexec.pts.domain;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Points
 * <p>
 * A points server.
 */
public class Points {
    private int initPoints;
                        
    public Map<String, List<Integer>> emails = new ConcurrentHashMap<>(); //ArrayList<Integer> -->  BALANCE TAG

    /**
     * Constant representing the default initial balance for every new client
     */
    private static final int DEFAULT_INITIAL_BALANCE = 100;
    /**
     * Global with the current value for the initial balance of every new client
     */
    private final AtomicInteger initialBalance = new AtomicInteger(DEFAULT_INITIAL_BALANCE);

    // Singleton -------------------------------------------------------------

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private Points() {}

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        private static final Points INSTANCE = new Points();
    }

    public static synchronized Points getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int writeEmail(final String email, final int value, final int tag) {
        int valor = readEmail(email).get(0) + value;
        if (tag > emails.get(email).get(1)){
            emails.get(email).set(1, tag);
            emails.get(email).set(0, valor);
        }  
        return emails.get(email).get(1);
    }

    public List<Integer> readEmail(final String email) {
        return emails.get(email);
    }

    public boolean emailExists(final String email) {
        return emails.containsKey(email);
    }

    public void registerEmail(final String email) {
        List<Integer> array = new ArrayList<Integer>();
        array.add(initPoints);
        array.add(0);
        emails.put(email, array);
    }

    public boolean existsEmail(final String email) {
        return emails.containsKey(email);
    }

    public void reset(){
        emails.clear();
    }

    public void init(int startPoints){
        this.initPoints = startPoints;
    }

    
}
