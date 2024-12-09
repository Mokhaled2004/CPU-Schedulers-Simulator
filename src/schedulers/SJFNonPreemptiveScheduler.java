package schedulers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import models.Process;

public class SJFNonPreemptiveScheduler extends Scheduler {

    public SJFNonPreemptiveScheduler(List<Process> processList) {
        super(processList);
    }

    @Override
    public void startScheduling() {
        // Ensure processList is mutable
        processList = new ArrayList<>(processList);

        // Step 1: Sort the processes by arrival time
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();
        List<Process> completedProcesses = new ArrayList<>();

        while (completedProcesses.size() < processList.size()) {
            // Step 2: Add processes that have arrived to the ready queue
            for (Process process : processList) {
                if (!completedProcesses.contains(process) && process.getArrivalTime() <= currentTime) {
                    if (!readyQueue.contains(process)) {
                        readyQueue.add(process);
                    }
                }
            }

            // Step 3: Decrease priority for processes in the ready queue to prevent starvation
            Process longestBurstProcess = null;
            int maxBurstTime = Integer.MIN_VALUE;

            // Find the process with the longest burst time in the ready queue
            for (Process process : readyQueue) {
                if (process.getBurstTime() > maxBurstTime) {
                    maxBurstTime = process.getBurstTime();
                    longestBurstProcess = process;
                }
            }

            // If there's a process with the longest burst time, decrease its priority
            if (longestBurstProcess != null) {
                longestBurstProcess.setPriority(Math.max(1, longestBurstProcess.getPriority() - 1)); // Decrease priority but ensure it doesn't go below 1
            }



            // Step 4: Select the process with the shortest burst time and highest priority
            readyQueue.sort(Comparator.comparingInt((Process p) -> p.getBurstTime())
                    .thenComparingInt(Process::getPriority));

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.remove(0);

                // Step 5: Execute the selected process
                currentTime = Math.max(currentTime, currentProcess.getArrivalTime()) + currentProcess.getBurstTime();
                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime() - currentProcess.getBurstTime());
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                completedProcesses.add(currentProcess);
            } else {
                // If no process is ready, increment the current time
                currentTime++;
            }
        }

        // Step 6: Update the process list with the scheduled data
        this.processList = completedProcesses;

        // Display results
        displayExecutionOrder();
        System.out.printf("Average Waiting Time: %.2f\n", calculateAverageWaitingTime());
        System.out.printf("Average Turnaround Time: %.2f\n", calculateAverageTurnaroundTime());
    }

    @Override
    protected void calculateWaitingTime() {
        for (Process process : processList) {
            process.setWaitingTime(
                    process.getTurnaroundTime() - process.getBurstTime()
            );
        }
    }

    @Override
    protected void calculateTurnaroundTime() {
        for (Process process : processList) {
            process.setTurnaroundTime(
                    process.getWaitingTime() + process.getBurstTime()
            );
        }
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
        System.out.println("Execution Order : ");
        for (Process process : processList) {
            System.out.println(process.getProcessName() +
                    ", Waiting Time: " + process.getWaitingTime() +
                    ", Turnaround Time: " + process.getTurnaroundTime() + ")");
        }
    }
}