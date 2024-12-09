package schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import models.Process;

public class SrtfStarvation extends Scheduler {
    protected int contextSwitchTime; // Context switch time between processes
    protected int agingFactor; // Factor to age processes in the ready queue

    public SrtfStarvation(List<Process> processList, int contextSwitchTime, int agingFactor) {
        super(processList);
        this.contextSwitchTime = contextSwitchTime;
        this.agingFactor = agingFactor;
    }

    @Override
    public void startScheduling() {
        List<Process> executedProcesses = new ArrayList<>();
        int currentTime = 0;

        // Initialize the remaining times and start times for all processes
        for (Process process : processList) {
            process.setRemainingTime(process.getBurstTime());
            process.setStartTime(-1); // Initialize start time to -1
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
                }
            }

            // Apply aging mechanism: Decrease the remaining time for processes that have been waiting
            // and give them a higher priority (i.e., reduce their remaining time)
            if (!readyQueue.isEmpty()) {
                for (Process process : readyQueue) {
                    // Aging process: only if remainingTime is greater than 0 (and has been waiting for a while)
                    if (process.getRemainingTime() > 0) {
                        process.setRemainingTime(process.getRemainingTime() - agingFactor);
                    }
                }
            }

            // If a process is running, check for preemption
            if (currentProcess != null && !readyQueue.isEmpty() &&
                readyQueue.peek().getRemainingTime() < currentProcess.getRemainingTime()) {
                currentTime += contextSwitchTime;  // Add context switch time
                currentProcess.setWaitingTime(currentProcess.getWaitingTime() + contextSwitchTime); // Add to waiting time
                readyQueue.add(currentProcess);  // Re-add the current process to the queue
                currentProcess = readyQueue.poll();  // Switch to the next process
            }

            // If no process is running, pick the next one from the ready queue
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
                    currentTime += contextSwitchTime;  // Add context switch time after completion
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getStartTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    executedProcesses.add(currentProcess);
                    currentProcess = null;  // Reset current process after completion
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
