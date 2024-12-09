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
    private int quantum;  // Quantum field
    private int fcaifactor;  // FCAI factor field
    private int startTime; // Default to -1 (not started yet)
    private int endTime; // New field for completion time
    private int TotalWaitingTime; // To track the finish time of the process

    // Constructor that includes priority and other fields
    public Process(String processName, String color, int arrivalTime, int burstTime, int priority, int waitingTime, int turnaroundTime, int quantum, int fcaifactor) {
        this.processName = processName;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.waitingTime = waitingTime;
        this.turnaroundTime = turnaroundTime;
        this.priority = priority;
        this.quantum = quantum;
        this.remainingTime = burstTime;
        this.fcaifactor = fcaifactor;
    }

    // Constructor without quantum and fcaifactor (defaults to 0)
    public Process(String processName, String color, int arrivalTime, int burstTime, int priority, int waitingTime, int turnaroundTime) {
        this(processName, color, arrivalTime, burstTime, priority, waitingTime, turnaroundTime, 0, 0);  // Call the full constructor with defaults
    }

    // Getters and Setters
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

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getQuantum() {
        return quantum;
    }

    public int getFcaifactor() {
        return fcaifactor;
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

    // Calculate the FCAI factor based on provided parameters
    public void calculateFCAIFactor(double lastArrivalTime, double maxBurstTime) {
        double V1 = lastArrivalTime / 10.0;
        double V2 = maxBurstTime / 10.0;

        this.fcaifactor = (int) ((10 - priority)
                + (arrivalTime / V1)
                + (remainingTime / V2));
    }

    public void setPriority(int max) {
        this.priority = max;
    }

    public int getPid() {
        return 1;
    }

    public int getProcessNumber() {
        return 1;
    }
}
