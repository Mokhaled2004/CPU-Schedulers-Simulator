package schedulers;

import java.util.*;
import models.Process;

public class FCAIScheduler {

    public void FCAIScheduling(List<Process> processes) {
        double v1 = calculateV1(processes);
        double v2 = calculateV2(processes);


    
        Queue<Process> readyQueue = new LinkedList<>();
        int currentTime = 0;
    
        while (!allProcessesCompleted(processes)) {
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && !p.isCompleted() && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }
    
            readyQueue.removeIf(Process::isCompleted);
    
            for (Process p : readyQueue) {
                p.calculateFCAIFactor(v1, v2);
            }
    
            Process currentProcess = selectProcessWithLowestFCAI(readyQueue);
    
            if (currentProcess != null) {
                int quantum = currentProcess.getQuantum();
                int remainingTime = currentProcess.getRemainingTime();
                int executionTime = Math.min(quantum, remainingTime);
                int fortyPercentTime = (int) Math.ceil(quantum * 0.4);
    
                if (executionTime > fortyPercentTime) {
                    for (int i = 0; i < fortyPercentTime; i++) {
                        incrementWaitingTimeForOthers(readyQueue, currentProcess);
                        currentTime++;
                    }
                    currentProcess.setRemainingTime(remainingTime - fortyPercentTime);
                } else {
                    for (int i = 0; i < executionTime; i++) {
                        incrementWaitingTimeForOthers(readyQueue, currentProcess);
                        currentTime++;
                    }
                    currentProcess.setRemainingTime(0);
                }
    
                if (currentProcess.getRemainingTime() > 0) {
                    if (executionTime < quantum) {

                        int unusedQuantum = quantum - executionTime; 
                        currentProcess.setQuantum(quantum + unusedQuantum); 
                    } else {

                        currentProcess.setQuantum(quantum + 2); 
                    }
                    System.out.println("Updated quantum for process " + currentProcess.getProcessName() + ": " + currentProcess.getQuantum());
                } else {
                    currentProcess.setCompleted(true);
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    currentProcess.setendTime(currentTime);
                }
            } else {
                
                currentTime++;
            }
        }
    
        printStatistics(processes);
    }
    

    private void incrementWaitingTimeForOthers(Queue<Process> readyQueue, Process currentProcess) {
        for (Process p : readyQueue) {
            if (!p.equals(currentProcess) && !p.isCompleted()) {
                p.incrementWaitingTime(); 
            }
        }
    }

    static double calculateV1(List<Process> processes) {
        int lastArrivalTime = processes.stream().mapToInt(Process::getArrivalTime).max().orElse(0);
        return lastArrivalTime / 10.0;
    }

    static double calculateV2(List<Process> processes) {
        int maxBurstTime = processes.stream().mapToInt(Process::getBurstTime).max().orElse(0);
        return maxBurstTime / 10.0;
    }

    static boolean allProcessesCompleted(List<Process> processes) {
        return processes.stream().allMatch(Process::isCompleted);
    }

    static Process selectProcessWithLowestFCAI(Queue<Process> readyQueue) {
        return readyQueue.stream().min(Comparator.comparingDouble(Process::getFcaifactor)).orElse(null);
    }

    static void printStatistics(List<Process> processes) {
        System.out.println("\nProcess Execution Statistics:");
        double totalWaitingTime = 0, totalTurnaroundTime = 0;

        for (Process p : processes) {
            System.out.println( p.getProcessName() + ":");
            System.out.println("WT: " + p.getWaitingTime());
            System.out.println("TT: " + p.getTurnaroundTime());
            totalWaitingTime += p.getWaitingTime();
            totalTurnaroundTime += p.getTurnaroundTime();
        }

        System.out.println("Avg Waiting Time: " + Math.ceil(totalWaitingTime / processes.size()));
        System.out.println("Avg Turnaround Time: " + Math.ceil(totalTurnaroundTime / processes.size()));
    }
}
