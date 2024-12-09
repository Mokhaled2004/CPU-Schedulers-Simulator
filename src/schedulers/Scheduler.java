package schedulers;

import java.util.List;
import models.Process;

public abstract class Scheduler {

    protected List<Process> processList;

    public Scheduler(List<Process> processList) {
        this.processList = processList;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    // Abstract methods for subclasses to implement
    public abstract void startScheduling();

    // Basic methods for calculating waiting and turnaround time
    protected void calculateWaitingTime() {
        for (Process process : processList) {
            int waitingTime = process.getTurnaroundTime() - process.getBurstTime();
            process.setWaitingTime(waitingTime);
        }
    }

    protected void calculateTurnaroundTime() {
        for (Process process : processList) {
            int turnaroundTime = process.getEndTime() - process.getArrivalTime();
            process.setTurnaroundTime(turnaroundTime);
        }
    }

    // Calculate average waiting time across all processes
    public double calculateAverageWaitingTime() {
        int totalWaitingTime = 0;
        for (Process process : processList) {
            totalWaitingTime += process.getWaitingTime();
        }
        return totalWaitingTime / (double) processList.size();
    }

    // Calculate average turnaround time across all processes
    public double calculateAverageTurnaroundTime() {
        int totalTurnaroundTime = 0;
        for (Process process : processList) {
            totalTurnaroundTime += process.getTurnaroundTime();
        }
        return totalTurnaroundTime / (double) processList.size();
    }

    // Display execution order of processes
    public void displayExecutionOrder() {
        for (Process process : processList) {
            System.out.println("Process " + process.getProcessName() + " - Arrival Time: " + process.getArrivalTime() +
                    ", Burst Time: " + process.getBurstTime() + ", Waiting Time: " + process.getWaitingTime() +
                    ", Turnaround Time: " + process.getTurnaroundTime() + ", End Time: " + process.getEndTime());
        }
    }
}
