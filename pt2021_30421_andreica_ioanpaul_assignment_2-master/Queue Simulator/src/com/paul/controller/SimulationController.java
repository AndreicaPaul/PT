package com.paul.controller;
import com.paul.model.Client;
import com.paul.model.Queue;
import com.paul.ui.ApplicationGUI;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simulation manager and controller class
 */
public class SimulationController implements Runnable{

    private ApplicationGUI applicationGUI;
    private int maxArrvTime;
    private int minArrvTime;
    private int numberOfExpectedClients;
    private int numberOfQueues;
    private int totalSimulationTime;
    private int minServTime;
    private int maxServTime;
    private Thread localThread;
    private int simulationTime = 0;
    private int totalClients;
    private int peakHour = 0;
    private int peakClients = 0;
    private int totalServiceTime = 0;

    /**
     * Start function called from application start function
     */
    public void start(){
        this.applicationGUI = new ApplicationGUI();
        this.applicationGUI.setLocation(300,200);
        this.applicationGUI.pack();
        this.applicationGUI.setVisible(true);
        this.applicationGUI.setSize(600,800);
        this.localThread = new Thread(this);
        addListeners();
    }

    /**
     * Function used to rerun a simulation with changed data
     * without restarting the entire application
     */
    private void startReviveThread(){
        if(!this.localThread.isAlive()) {
            this.localThread = new Thread(this);
        }
        simulationTime = 0;
        localThread.start();
    }

    /**
     * Input validation function
     * @return true if input is valid and false otherwise
     */
    public boolean validateInput(){
        int tempMaxArrvTime, tempMinArrvTime, tempNumberOfExpectedClients, tempNumbOfQueues, tempMinServTime , tempMaxServTime , tempSimulationTime;
        try {
            tempMaxArrvTime = Integer.parseInt(this.applicationGUI.getMaxArrvTimeInput());
            tempMinArrvTime = Integer.parseInt(this.applicationGUI.getMinArrvTimeInput());
            tempNumberOfExpectedClients = Integer.parseInt(this.applicationGUI.getNoClientsInput());
            tempNumbOfQueues = Integer.parseInt(this.applicationGUI.getNoOfQueuesInput());
            tempMinServTime = Integer.parseInt(this.applicationGUI.getMinServTimeInput());
            tempMaxServTime = Integer.parseInt(this.applicationGUI.getMaxServTimeInput());
            tempSimulationTime = Integer.parseInt(this.applicationGUI.getSimulationTimeInput());
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Invalid input data!" + e.getMessage());
            return false;
        }
        if(tempMaxServTime > tempMinServTime && tempMaxArrvTime > tempMinArrvTime
                && tempNumbOfQueues > 0 && tempSimulationTime > 0 && tempNumberOfExpectedClients > 0 && tempMinArrvTime > 0 && tempMinServTime > 0) {
            this.maxArrvTime = tempMaxArrvTime;
            this.minArrvTime = tempMinArrvTime;
            this.numberOfExpectedClients = tempNumberOfExpectedClients;
            this.totalClients = numberOfExpectedClients;
            this.numberOfQueues = tempNumbOfQueues;
            this.totalSimulationTime = tempSimulationTime;
            this.minServTime = tempMinServTime;
            this.maxServTime = tempMaxServTime;
            return true;
        }
        JOptionPane.showMessageDialog(null, "Invalid input data!");
        return false;
    }

    /**
     * Find the best queue based on waiting time.
     * @param queues
     * @return queue : best queue
     */
    private Queue selectBestQueue(Queue[] queues){
        int bestQueueID = 0;
        int smallestWaitingTime = Integer.MAX_VALUE;
        for (Queue q : queues) {
            if (q.getWaitingTime() < smallestWaitingTime) {
                bestQueueID = q.getID();
                smallestWaitingTime = q.getWaitingTime();
            }
        }
        return queues[bestQueueID];
    }

    /**
     * Send client to chosen queue.
     * @param queue
     * @param client
     */
    private void sendTaskToQueue(Queue queue, Client client){
        queue.addClientToQueue(client);
    }

    /**
     * Check if current time is peak hour
     * @param queues
     */
    private void computePeakHour(Queue[] queues){
        int clientCount = 0;
        for (Queue q : queues) {
            clientCount += q.getNoOfClientsInQueue();
        }
        if (clientCount > peakClients) {
            peakClients = clientCount;
            peakHour = simulationTime;
        }
    }

    /**
     * Main thread control function, contains all the logic behind initializing and sending tasks to queues.
     */
    @Override
    public void run() {
        Client[] waitingClients = new Client[numberOfExpectedClients];
        Queue[] queues = new Queue[numberOfQueues];
        for (int i = 0; i < this.numberOfExpectedClients; i++) {
            waitingClients[i] = new Client((int) (minArrvTime + Math.random() * (maxArrvTime - minArrvTime)), (int) (minServTime + Math.random() * (maxServTime - minServTime)), i);
            totalServiceTime += waitingClients[i].getServTime();
        }
        for(int i = 0; i < this.numberOfQueues; i++) queues[i] = new Queue(i);
        while(this.simulationTime <= this.totalSimulationTime && clientsRemaining(queues)>0) {
            int i = 0;
            while (i < numberOfExpectedClients) {
                if (waitingClients[i].getArrvTime() == simulationTime) {
                    sendTaskToQueue(selectBestQueue(queues),waitingClients[i]);
                    for (int j = i; j < numberOfExpectedClients - 1; j++)
                        waitingClients[j] = waitingClients[j + 1];
                    numberOfExpectedClients--;
                } else i++;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            computePeakHour(queues);
            applicationGUI.updateLog(buildMessage(waitingClients, queues));
            simulationTime++;
        }
        applicationGUI.updateLog(buildSimulationResultsLogs(queues));
        applicationGUI.writeToFile();
    }

    /**
     * Build and send simulation logs to UI after each unit of time passed
     * @param waitingClients
     * @param queues
     * @return log message
     */
    private String buildMessage(Client[] waitingClients, Queue[] queues){
        StringBuilder message = new StringBuilder();
        message.append("Time: ").append(simulationTime).append("\n");
        message.append("Waiting  clients: ");
        for (int i = 0; i < numberOfExpectedClients; i++) {
            message.append(" (").append(waitingClients[i].getID()).append(",").append(waitingClients[i].getArrvTime()).append(",").append(waitingClients[i].getServTime()).append(")").append(";");
        }
        message.append("\n");
        for (Queue q: queues) {
            message.append("Queue ").append(q.getID()+1).append(": ");
            if(q.getNoOfClientsInQueue() < 1)
                message.append("closed");
            else
                message.append(q.clientsLog());
            message.append("\n");
        }
        return message.toString();
    }

    /**
     * Compute and send simulation results to GUI
     * @param queues
     * @return message with simulation results
     */
    private String buildSimulationResultsLogs(Queue[] queues) {
        StringBuilder message = new StringBuilder();
        int totalClientsThatWaited = 0, totalWaitTime = 0;
        for (int i = 0; i < numberOfQueues; i++) {
            totalClientsThatWaited += queues[i].getClientsWaiting();
            totalWaitTime += queues[i].getTimeWaiting();
            queues[i].stop();
        }
        double avgWaitTime = 0.0;
        if(totalWaitTime > 0)
            avgWaitTime = (double)totalWaitTime/(double)totalClientsThatWaited;
        message.append("Average Waiting Time: ").append(avgWaitTime).append("\n");
        message.append("Average Service Time: ").append( ((double)totalServiceTime / (double)totalClients)).append("\n");
        message.append("Peak Time: ").append(peakHour).append(" with ").append(peakClients).append(" clients\n");
        message.append("------------------SIMULATION ENDED.------------------");
        return  message.toString();
    }

    /**
     * Calculate remaining clients
     * @param queues
     * @return numberOfRemainingClients
     */
    private int clientsRemaining(Queue[] queues){
        int clients = 0;
        clients+=numberOfExpectedClients;
        for (Queue q: queues) {
            clients += q.getNoOfClientsInQueue();
        }
        return clients;
    }

    /**
     * Button Listeners Initialization
     */
    private void addListeners(){
        applicationGUI.addRunBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!localThread.isAlive() && validateInput()){
                    System.out.println("Input is valid.");
                    startReviveThread();
                }
                else if(localThread.isAlive())
                    JOptionPane.showMessageDialog(null, "Please wait for the simulation to end or close the program if you want to simulate again.");
            }
        });
        applicationGUI.addAutofillBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationGUI.setNoClientsInput("4");
                applicationGUI.setNoOfQueuesInput("2");
                applicationGUI.setSimulationTimeInput("60");
                applicationGUI.setMinArrvTimeInput("2");
                applicationGUI.setMaxArrvTimeInput("30");
                applicationGUI.setMinServTimeInput("2");
                applicationGUI.setMaxServTimeInput("4");
            }
        });
    }
}
