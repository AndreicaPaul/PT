package com.paul.ui;
import com.paul.controller.SimulationController;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

/**
 * GUI Class
 */
public class ApplicationGUI extends JFrame{
    /**
     * Application start point;
     * Instantiate and initialize controller.
     * @param args
     */
    public static void main(String[] args) {
        SimulationController controller = new SimulationController();
        controller.start();
    }

    private JTextField noClientsInput;
    private JTextField noOfQueuesInput;
    private JTextField minArrvTimeInput;
    private JTextField maxArrvTimeInput;
    private JTextField minServTimeInput;
    private JTextField maxServTimeInput;
    private JButton runBtn;
    private JPanel mainPanel;
    private JTextArea textArea1;
    private JScrollPane scrollPane;
    private JTextField simulationTimeInput;
    private JButton autofillBtn;
    private JFormattedTextField minimumTimeOfArrivalFormattedTextField;
    private JFormattedTextField numberOfQueuesFormattedTextField;
    private JFormattedTextField numberOfClientsFormattedTextField;
    private JFormattedTextField maximumTimeOfArrivalFormattedTextField;
    private JFormattedTextField minimumTimeOfServiceFormattedTextField;
    private JFormattedTextField maximumTimeOfServiceFormattedTextField;
    private JFormattedTextField simulationTimeFormattedTextField;

    /**
     * Modified constructor set-up base view properties
     */
    public ApplicationGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
    }

    /**
     * Set noClientsInput text (used for default values and autocomplete functionality).
     * @param noClientsInput
     */
    public void setNoClientsInput(String noClientsInput) {
        this.noClientsInput.setText(noClientsInput);
    }

    /**
     * Set noOfQueuesInput text (used for default values and autocomplete functionality).
     * @param noOfQueuesInput
     */
    public void setNoOfQueuesInput(String noOfQueuesInput) {
        this.noOfQueuesInput.setText(noOfQueuesInput);
    }

    /**
     * Set minArrvTimeInput text (used for default values and autocomplete functionality).
     * @param minArrvTimeInput
     */
    public void setMinArrvTimeInput(String minArrvTimeInput) {
        this.minArrvTimeInput.setText(minArrvTimeInput);
    }

    /**
     * Set maxArrvTimeInput text (used for default values and autocomplete functionality).
     * @param maxArrvTimeInput
     */
    public void setMaxArrvTimeInput(String maxArrvTimeInput) {
        this.maxArrvTimeInput.setText(maxArrvTimeInput);
    }

    /**
     * Set minServTimeInput text (used for default values and autocomplete functionality).
     * @param minServTimeInput
     */
    public void setMinServTimeInput(String minServTimeInput) {
        this.minServTimeInput.setText(minServTimeInput);
    }

    /**
     * Set maxServTimeInput text (used for default values and autocomplete functionality).
     * @param maxServTimeInput
     */
    public void setMaxServTimeInput(String maxServTimeInput) {
        this.maxServTimeInput.setText(maxServTimeInput);
    }

    /**
     * Set simulationTimeInput text (used for default values and autocomplete functionality).
     * @param simulationTimeInput
     */
    public void setSimulationTimeInput(String simulationTimeInput) {
        this.simulationTimeInput.setText(simulationTimeInput);
    }

    /**
     * @return simulationTimeInput
     */
    public String getSimulationTimeInput() {
        return simulationTimeInput.getText();
    }

    /**
     * @return noClientsInput
     */
    public String getNoClientsInput() {
        return noClientsInput.getText();
    }

    /**
     * @return noOfQueuesInput
     */
    public String getNoOfQueuesInput() {
        return noOfQueuesInput.getText();
    }

    /**
     * @return minArrvTimeInput
     */
    public String getMinArrvTimeInput() {
        return minArrvTimeInput.getText();
    }

    /**
     * @return maxArrvTimeInput
     */
    public String getMaxArrvTimeInput() {
        return maxArrvTimeInput.getText();
    }

    /**
     * @return minServTimeInput
     */
    public String getMinServTimeInput() {
        return minServTimeInput.getText();
    }

    /**
     * @return maxServTimeInput
     */
    public String getMaxServTimeInput() {
        return maxServTimeInput.getText();
    }

    /**
     * Setup button listener
     * @param actionListener
     */
    public void addRunBtnActionListener(final ActionListener actionListener)
    {
        runBtn.addActionListener(actionListener);
    }

    /**
     * Setup button listener
     * @param actionListener
     */
    public void addAutofillBtnActionListener(final ActionListener actionListener)
    {
        autofillBtn.addActionListener(actionListener);
    }

    /**
     * Append message to UI log
     * @param message
     */
    public void updateLog(String message){
            this.textArea1.append(message + "\n");
            this.scrollPane.getVerticalScrollBar().setValue(this.scrollPane.getVerticalScrollBar().getMaximum());
    }

    /**
     * Copy data from app log to a .txt file
     */
    public void writeToFile(){
        try {
            String logName = "F:\\GitHub\\pt2021_30421_andreica_ioanpaul_assignment_2\\Queue Simulator\\log" + Date.from(Instant.now()).getTime() + ".txt";
            File myObj = new File(logName);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(logName);
            myWriter.write(this.textArea1.getText());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
