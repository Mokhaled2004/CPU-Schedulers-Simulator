package models;

public class Process {
    private String processName;
    private String color;  // Color of the process (e.g., "Red")
    private int arrivalTime;
    private int burstTime;
    private int waitingTime;
    private int turnaroundTime;
    private int priority;  // Priority field
    private int remainingTime;  // Remaining time field

    // Constructor that includes priority
    public Process(String processName, String color, int arrivalTime, int burstTime, int priority,int waitingTime, int turnaroundTime) {
        this.processName = processName;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.waitingTime = waitingTime;
        this.turnaroundTime = turnaroundTime;
        this.priority = priority;  // Initialize priority
    }

    // Getters
    public String getProcessName() {
        return processName;
    }

    public String getColor() {
        return color;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    // Setter for remaining time
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    // Setters for waiting time and turnaround time
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;  // Add setter for priority
    }
}
