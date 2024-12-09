package models;

public class Process {
    private String processName;
    private String color;  // Color of the process (e.g., "Red")
    private int arrivalTime;
    private int burstTime;
    private int waitingTime;
    private int turnaroundTime;
    private int priority;  // Priority field

    // New fields to store the start and end times for scheduling
    private int startTime;  // Start time of the process
    private int endTime;    // End time of the process

    // Constructor that includes priority, start time, and end time
    public Process(String processName, String color, int arrivalTime, int burstTime, int waitingTime, int turnaroundTime, int priority) {
        this.processName = processName;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.waitingTime = waitingTime;
        this.turnaroundTime = turnaroundTime;
        this.priority = priority;  // Initialize priority
        this.startTime = -1;  // Initially set start time to -1 (not yet started)
        this.endTime = -1;    // Initially set end time to -1 (not yet finished)
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

    // New getters for start and end times
    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    // Setters for waiting time and turnaround time
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    // Setters for start and end times
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    // For displaying process id and number (if needed)
    public int getPid() {
        return 1; // Placeholder implementation (change as necessary)
    }

    public int getProcessNumber() {
        return 1; // Placeholder implementation (change as necessary)
    }
}
