package models;

public class Process {
    // Attributes
    private String processName;
    private String processColor;
    private int arrivalTime;
    private int burstTime;
    private int priorityNumber;
    private int quantum;
    private int waitingTime;    // New attribute for waiting time
    private int turnaroundTime; // New attribute for turnaround time

    // Constructor
    public Process(String processName, String processColor, int arrivalTime, int burstTime, int priorityNumber, int quantum) {
        this.processName = processName;
        this.processColor = processColor;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.quantum = quantum;
        this.waitingTime = 0;      // Default value for waiting time
        this.turnaroundTime = 0;   // Default value for turnaround time
    }


    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessColor() {
        return processColor;
    }

    public void setProcessColor(String processColor) {
        this.processColor = processColor;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriorityNumber() {
        return priorityNumber;
    }

    public void setPriorityNumber(int priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
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

    @Override
    public String toString() {
        return "Process{" +
                "processName='" + processName + '\'' +
                ", processColor='" + processColor + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priorityNumber=" + priorityNumber +
                ", quantum=" + quantum +
                ", waitingTime=" + waitingTime +      
                ", turnaroundTime=" + turnaroundTime + 
                '}';
    }
}
