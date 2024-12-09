package schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import models.Process;

public class SrtfStarvation extends Scheduler {
    protected int contextSwitchTime; // Context switch time between processes
    protected int agingInterval;    // Interval for applying aging
    protected int maxWaitTime;      // Maximum wait time to prevent starvation

    public SrtfStarvation(List<Process> processList, int contextSwitchTime, int agingInterval, int maxWaitTime) {
        super(processList);
        this.contextSwitchTime = contextSwitchTime;
        this.agingInterval = agingInterval;
        this.maxWaitTime = maxWaitTime;
    }

    @Override
    public void startScheduling() {
        List<Process> executedProcesses = new ArrayList<>();
        int currentTime = 0;

        // Initialize the remaining times and waiting times for all processes
        for (Process process : processList) {
            process.setRemainingTime(process.getBurstTime());
            process.setWaitingTime(0);
        }

        Process currentProcess = null; // Current running process

        // Use PriorityQueue to always select the process with the shortest remaining burst time
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
            (p1, p2) -> Integer.compare(p1.getRemainingTime(), p2.getRemainingTime()) // Sort by remaining burst time
        );

        // Start the scheduling loop
        while (!processList.isEmpty() || !readyQueue.isEmpty() || currentProcess != null) {
            // Add processes to the ready queue if they have arrived at currentTime
            for (int i = 0; i < processList.size(); i++) {
                Process process = processList.get(i);
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    processList.remove(i);
                    i--; // Adjust index after removal
                }
            }

            // Apply aging to prevent starvation
            for (Process process : readyQueue) {
                int waitTime = currentTime - process.getArrivalTime() - (process.getBurstTime() - process.getRemainingTime());
                if (waitTime > maxWaitTime) {
                    // Decrease remaining time to prioritize the process (simulates aging effect)
                    process.setRemainingTime(Math.max(1, process.getRemainingTime() - agingInterval));
                }
            }

            // If there's a process running and a new process arrives with a shorter remaining time, preempt
            if (currentProcess != null && !readyQueue.isEmpty() && readyQueue.peek().getRemainingTime() < currentProcess.getRemainingTime()) {
                readyQueue.add(currentProcess); // Re-add the current process to the queue
                currentProcess = readyQueue.poll(); // Preempt the current process
                currentTime += contextSwitchTime; // Account for context switch time
            }

            // If no process is running and the ready queue has processes, select the one with the shortest remaining time
            if (currentProcess == null && !readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll(); // Select the process with the smallest remaining time
                currentTime += contextSwitchTime; // Account for context switch time
            }

            // If there's a process running, execute it for 1 unit of time
            if (currentProcess != null) {
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);

                // If the current process finishes, calculate its waiting and turnaround times
                if (currentProcess.getRemainingTime() == 0) {
                    currentProcess.setTurnaroundTime(currentTime + 1 - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    executedProcesses.add(currentProcess);
                    currentProcess = null; // Reset current process
                }
            }

            // Increment the time
            currentTime++;
        }

        // Store executed processes back into processList for further use
        processList.addAll(executedProcesses);

        // Display execution order and average times
        displayExecutionOrder();
        System.out.printf("Average Waiting Time: %.2f\n", calculateAverageWaitingTime());
        System.out.printf("Average Turnaround Time: %.2f\n", calculateAverageTurnaroundTime());
    }

    @Override
    public double calculateAverageWaitingTime() {
        int totalWaitingTime = 0;
        for (Process process : processList) {
            totalWaitingTime += process.getWaitingTime();
        }
        return totalWaitingTime / (double) processList.size();
    }

    @Override
    public double calculateAverageTurnaroundTime() {
        int totalTurnaroundTime = 0;
        for (Process process : processList) {
            totalTurnaroundTime += process.getTurnaroundTime();
        }
        return totalTurnaroundTime / (double) processList.size();
    }

    @Override
    public void displayExecutionOrder() {
        System.out.println("Execution Order (Shortest Remaining Time First):");
        for (Process process : processList) {
            System.out.println(process.getProcessName() +
                    ", Waiting Time: " + process.getWaitingTime() +
                    ", Turnaround Time: " + process.getTurnaroundTime());
        }
    }
}