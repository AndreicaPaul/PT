package com.paul.model;

/**
 * Queue class
 */
public class Queue implements Runnable{
    private Client[] clients;
    private final int ID;
    private boolean isRunning;
    private Thread queueThread;
    private int numberOfClients;
    private int timeWaiting = 0;
    private int clientsWaiting = 0;

    /**
     * Queue constructor with initializations and thread start
     * @param ID given id for queue
     */
    public Queue(int ID) {
        this.ID = ID;
        clients = new Client[1100];
        numberOfClients = 0;
        isRunning = true;
        queueThread = new Thread(this);
        queueThread.start();
    }

    /**
     * Function used for statics
     * @return waiting time for clients in queue ever
     */
    public int getTimeWaiting() {
        return timeWaiting;
    }

    /**
     * Get number of clients that ever waited in queue before being served
     * Function is used for the statistics
     * @return clients that waited while queue was running
     */
    public int getClientsWaiting() {
        return clientsWaiting;
    }

    /**
     * get ID of the queue
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Get number of clients in queue
     */
    public int getNoOfClientsInQueue(){
        return numberOfClients;
    }

    /**
     * Add client in queue
     * @param c : client to be added
     */
    public void addClientToQueue(Client c){
        Client client = new Client(c.getArrvTime(),c.getServTime(),c.getID());
        clients[numberOfClients] = client;
        numberOfClients = numberOfClients + 1;
        if(numberOfClients == 1) clients[0].setServTime(client.getServTime()+1);
    }

    /**
     * Get queue's waiting time, use for strategy selection
     * @return int: waiting time
     */
    public int getWaitingTime(){
        int waitTime = 0;
        if(numberOfClients > 0) {
            for (int i = 0; i < numberOfClients; i++) {
                waitTime += clients[i].getServTime();
            }
        }
        return waitTime;
    }

    /**
     * Generate log of clients in queue to be sent to the Controller and then the UI
     * @return message
     */
    public String clientsLog(){
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < numberOfClients; i++) {
            message.append(" (").append(clients[i].getID()).append(",").append(clients[i].getArrvTime()).append(",").append(clients[i].getServTime()).append(")").append(";");
        }
        return message.toString();
    }

    /**
     * Remove client from queue after being served
     */
    private void removeServedClient(){
        for(int i = 0; i < numberOfClients-1; i++)
            clients[i] = clients[i+1];
        numberOfClients = numberOfClients-1;
    }

    /**
     * Main function
     */
    @Override
    public void run() {
        while (isRunning || numberOfClients != 0){
            if(numberOfClients != 0){
                clients[0].setServTime(clients[0].getServTime() - 1);
                if(clients[0].getServTime() <= 0)
                    removeServedClient();
                for (int i = 1; i < numberOfClients; i++) {
                    if(!clients[i].isWaiting()){
                        clients[i].setWaiting(true);
                        clientsWaiting++;
                    }
                }
                if(numberOfClients > 1)
                    timeWaiting++;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method to stop the thread
     */
    public void stop(){
        isRunning = false;
    }
}
