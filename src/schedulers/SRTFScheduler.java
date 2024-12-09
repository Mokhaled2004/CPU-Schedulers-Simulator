package schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import models.Process;

public class SRTFScheduler extends Scheduler {
    protected int contextSwitchTime; // Context switch time between processes

    public SRTFScheduler(List<Process> processList, int contextSwitchTime) {
        super(processList);
        this.contextSwitchTime = contextSwitchTime;
    }

    @Override
    public void startScheduling() {
        List<Process> executedProcesses = new ArrayList<>();
        int currentTime = 0;

        // Initialize the remaining times and pause times for all processes
        for (Process process : processList) {
            process.setRemainingTime(process.getBurstTime());
            process.setStartTime(-1); // Initialize startTime to -1
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

            // Check for preemption: If a new process arrives with a shorter remaining time than the current process
            if (currentProcess != null && !readyQueue.isEmpty() && readyQueue.peek().getRemainingTime() < currentProcess.getRemainingTime()) {
                currentTime += contextSwitchTime;  // Account for context switch time
                readyQueue.add(currentProcess);  // Re-add the current process to the queue
                currentProcess = readyQueue.poll();  // Switch to the new process
            }

            // If no process is running, pick the next one from the queue
            if (currentProcess == null && !readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll();
                if (currentTime > currentProcess.getRemainingTime()) {
                    currentProcess.setTotalWaitingTime(currentProcess.getTotalWaitingTime() + (currentTime - currentProcess.getRemainingTime()));
                }
            }

            // Execute the current process for 1 unit of time
            if (currentProcess != null) {
                // Set the start time only the first time the process runs
                if (currentProcess.getStartTime() == -1) {
                    currentProcess.setStartTime(currentTime);
                }

                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                currentTime++;

                // If the current process finishes
                if (currentProcess.getRemainingTime() == 0) {
                    
                    currentTime += contextSwitchTime;  // Handle context switch time after completion
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getStartTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    executedProcesses.add(currentProcess);
                    currentProcess = null;  // Reset current process
                    
                }
            } else {
                // If no process is running, increment the time
                currentTime++;
            }
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