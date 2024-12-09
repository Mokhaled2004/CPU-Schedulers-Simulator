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
    private boolean completed; // Flag to track if the process is completed

    // Constructor that includes priority
    public Process(String processName, String color, int arrivalTime, int burstTime, int priority, int waitingTime, int turnaroundTime, int quantum, int fcaifactor) {
        this.processName = processName;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.waitingTime = waitingTime;
        this.turnaroundTime = turnaroundTime;
        this.priority = priority;  // Initialize priority
        this.quantum = quantum;  // Initialize quantum
        this.remainingTime = burstTime;  // Remaining time initially equals burst time
        this.fcaifactor = fcaifactor;  // Initialize FCAI factor
        this.completed = false;  // Initially not completed
    }

    // New method to increment the waiting time
    public void incrementWaitingTime() {
        this.waitingTime++;
    }

    public int getTotalWaitingTime() {
        return TotalWaitingTime;
    }

    public void setTotalWaitingTime(int TotalWaitingTime) {
        this.TotalWaitingTime = TotalWaitingTime;
    }

    // Getters
    public String getProcessName() {
        return processName;
    }

    public int getEndTime() { // Getter for completion time
        return endTime;
    }

    public void setendTime(int eTime) { // Setter for completion time
        this.endTime = eTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
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

    public int getQuantum() {
        return quantum;
    }

    public double getFcaifactor() {
        return fcaifactor;
    }

    // Setters
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    // Updated setFcaifactor to accept components
    public void setFcaifactor(int priorityFactor, int arrivalFactor, int remainingFactor) {
        this.fcaifactor = (int) (priorityFactor + arrivalFactor + remainingFactor);
    }

    // Method to calculate FCAI factor
    public void calculateFCAIFactor(double lastArrivalTime, double maxBurstTime) {
        double V1 = lastArrivalTime / 10.0;
        double V2 = maxBurstTime / 10.0;

        this.fcaifactor = (int) ((10 - priority) 
                        + (arrivalTime / V1) 
                        + (remainingTime / V2));
    }

    // Method to check if the process is completed
    public boolean isCompleted() {
        return completed;
    }

    // Method to mark the process as completed
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
