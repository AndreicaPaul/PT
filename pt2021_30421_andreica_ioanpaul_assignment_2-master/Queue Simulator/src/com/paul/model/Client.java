package com.paul.model;

public class Client {
    private final int arrvTime;
    private int servTime;
    private final int ID;
    private boolean waiting = false;

    /**
     * Client constructor with parameters from controller
     * @param arrvTime time of arrival
     * @param servTime time of service
     * @param ID unique id
     */
    public Client(int arrvTime, int servTime, int ID) {
        this.arrvTime = arrvTime;
        this.servTime = servTime;
        this.ID = ID;
    }

    /**
     * @return if the client is waiting in queue
     */
    public boolean isWaiting() {
        return waiting;
    }

    /**
     * set waiting in queue status
     */
    public void setWaiting(boolean waited) {
        this.waiting = waited;
    }

    /**
     * set service time
     * @param t : service time
     */
    public void setServTime(int t){
        this.servTime = t;
    }

    /**
     * @return time of arrival
     */
    public int getArrvTime() {
        return arrvTime;
    }

    /**
     * Get time of service
     */
    public int getServTime() {
        return servTime;
    }

    /**
     * @return the unieue id
     */
    public int getID() {
        return ID;
    }
}
