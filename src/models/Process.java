package models;

public class Process {
    private String processName;
    private String color;  
    private int arrivalTime;
    private int burstTime;
    private int waitingTime;
    private int turnaroundTime;
    private int priority;  
    private int remainingTime;  
    private int quantum;  
    private int fcaifactor;  
    private int startTime; 
    private int endTime; 
    private int TotalWaitingTime; 
    private boolean completed; 

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
        this.completed = false;  
        this.quantum  = quantum; 
    }

    public void incrementWaitingTime() {
        this.waitingTime++;
    }

    public int getTotalWaitingTime() {
        return TotalWaitingTime;
    }

    public void setTotalWaitingTime(int TotalWaitingTime) {
        this.TotalWaitingTime = TotalWaitingTime;
    }

    public String getProcessName() {
        return processName;
    }

    public int getEndTime() { 
        return endTime;
    }

    public void setendTime(int eTime) { 
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

    public void setFcaifactor(int priorityFactor, int arrivalFactor, int remainingFactor) {
        this.fcaifactor = (int) (priorityFactor + arrivalFactor + remainingFactor);
    }

    public void calculateFCAIFactor(double lastArrivalTime, double maxBurstTime) {
        double V1 = lastArrivalTime ;
        double V2 = maxBurstTime ;

        this.fcaifactor = (int) ((10 - priority) 
                        + (arrivalTime / V1) 
                        + (remainingTime / V2));
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
