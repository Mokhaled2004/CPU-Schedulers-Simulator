package schedulers;

import java.util.ArrayList;
import java.util.List;
import models.Process;

public class SRTFScheduler extends Scheduler {

    private int contextSwitchTime;  // Variable to hold the context switch time
    private int agingInterval;      // Time interval for aging to fix starvation
    private int maxWaitTime;        // Maximum wait time before aging is applied

    public SRTFScheduler(List<Process> processList, int contextSwitchTime, int agingInterval, int maxWaitTime) {
        super(processList);
        this.contextSwitchTime = contextSwitchTime;
        this.agingInterval = agingInterval;
        this.maxWaitTime = maxWaitTime;
    }

    @Override
    public void startScheduling() {
        List<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;

        // Keep track of the order of execution for display
        List<Process> executedProcesses = new ArrayList<>();

        // Track the time each process has been in the system
        int[] waitTimes = new int[processList.size()];

        while (!processList.isEmpty() || !readyQueue.isEmpty()) {
            // Add processes to the ready queue if they've arrived
            for (int i = 0; i < processList.size(); i++) {
                Process process = processList.get(i);
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    processList.remove(i);
                    i--; // Adjust index after removing a process
                }
            }

            // If the ready queue is empty, move time forward
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Sort the ready queue by remaining burst time (ascending order)
            readyQueue.sort((p1, p2) -> Integer.compare(p1.getRemainingTime(), p2.getRemainingTime()));

            // Pick the process with the shortest remaining time
            Process currentProcess = readyQueue.get(0);

            // Handle starvation by aging processes (reduce remaining time)
            for (Process p : readyQueue) {
                if (currentTime - p.getArrivalTime() > maxWaitTime) {
                    p.setRemainingTime(Math.max(p.getRemainingTime() - agingInterval, p.getBurstTime() / 2));
                }
            }

            // Calculate the time the process will complete
            int remainingTime = currentProcess.getRemainingTime();
            currentProcess.setRemainingTime(remainingTime - 1);  // Decrease the remaining burst time

            // If the process finishes, calculate turnaround and waiting time
            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setTurnaroundTime(currentTime + 1 - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                executedProcesses.add(currentProcess);
                readyQueue.remove(currentProcess);
            }

            // Apply context switch time after each process execution
            currentTime++;
            currentTime += contextSwitchTime;
        }

        // Replace the original list with executed processes (ordered execution)
        processList.addAll(executedProcesses);

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
        return Math.ceil(totalWaitingTime / (double) processList.size());
    }

    @Override
    public double calculateAverageTurnaroundTime() {
        int totalTurnaroundTime = 0;
        for (Process process : processList) {
            totalTurnaroundTime += process.getTurnaroundTime();
        }
        return Math.ceil(totalTurnaroundTime / (double) processList.size());
    }

    @Override
    public void displayExecutionOrder() {
        System.out.println("Execution Order (Shortest Remaining Time First with Context Switching): ");
        for (Process process : processList) {
            System.out.println(process.getProcessName() +
                    ", Waiting Time: " + process.getWaitingTime() +
                    ", Turnaround Time: " + process.getTurnaroundTime() + ")");
        }
    }
}
