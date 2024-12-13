package schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import models.Process;

public class SrtfStarvation extends Scheduler {
    protected int contextSwitchTime; 
    protected int agingInterval;    
    protected int maxWaitTime;      

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

        for (Process process : processList) {
            process.setRemainingTime(process.getBurstTime());
            process.setWaitingTime(0);
        }

        Process currentProcess = null; 

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
            (p1, p2) -> Integer.compare(p1.getRemainingTime(), p2.getRemainingTime()) 
        );

        while (!processList.isEmpty() || !readyQueue.isEmpty() || currentProcess != null) {
            for (int i = 0; i < processList.size(); i++) {
                Process process = processList.get(i);
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    processList.remove(i);
                    i--; 
                }
            }

            for (Process process : readyQueue) {
                int waitTime = currentTime - process.getArrivalTime() - (process.getBurstTime() - process.getRemainingTime());
                if (waitTime > maxWaitTime) {
                    process.setRemainingTime(Math.max(1, process.getPriority() - agingInterval));
                }
            }

            if (currentProcess != null && !readyQueue.isEmpty() && readyQueue.peek().getRemainingTime() < currentProcess.getRemainingTime()) {
                readyQueue.add(currentProcess); 
                currentProcess = readyQueue.poll(); 
                currentTime += contextSwitchTime; 
            }

            if (currentProcess == null && !readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll(); 
                currentTime += contextSwitchTime; 
            }

            if (currentProcess != null) {
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);

                if (currentProcess.getRemainingTime() == 0) {
                    currentProcess.setTurnaroundTime(currentTime + 1 - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    executedProcesses.add(currentProcess);
                    currentProcess = null; 
                }
            }

            currentTime++;
        }

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