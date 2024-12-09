package models;

public class Process {
    private String processName;
    private String color;
    private int arrivalTime;
    private int burstTime;
    private int waitingTime;
    private int turnaroundTime;
    private int priority;
    private int pid;
    private int processNumber;
    private int startTime;
    private int endTime;

    public Process(String processName, String color, int arrivalTime, int burstTime, int waitingTime, int turnaroundTime, int priority) {
        this.processName = processName;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.waitingTime = waitingTime;
        this.turnaroundTime = turnaroundTime;
        this.priority = priority;
        this.pid = 0; // Should be set later based on the system
        this.processNumber = 0; // Should be set based on the list
        this.startTime = -1;
        this.endTime = -1;
    }

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

    public void setPriority(int priority) {  // Add this setter method
        this.priority = priority;
    }

    public int getPid() {
        return pid;
    }

    public int getProcessNumber() {
        return processNumber;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setProcessNumber(int processNumber) {
        this.processNumber = processNumber;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    // Add the missing setter for waitingTime
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

}
