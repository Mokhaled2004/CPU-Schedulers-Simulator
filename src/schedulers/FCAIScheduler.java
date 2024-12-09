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
            // Add processes that have arrived to the ready queue
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && !p.isCompleted() && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }
    
            // Remove completed processes from the ready queue
            readyQueue.removeIf(Process::isCompleted);
    
            // Recalculate FCAI factors
            for (Process p : readyQueue) {
                p.calculateFCAIFactor(v1, v2);
            }
    
            // Select the process with the lowest FCAI factor
            Process currentProcess = selectProcessWithLowestFCAI(readyQueue);
    
            if (currentProcess != null) {
                int executionTime = Math.min(currentProcess.getQuantum(), currentProcess.getRemainingTime());
                int fortyPercentTime = (int) Math.ceil(currentProcess.getQuantum() * 0.4);
    
                System.out.println("Executing process: " + currentProcess.getProcessName() + " (Start time: " + currentTime + ")");
    
                // First 40% non-preemptive
                if (executionTime > fortyPercentTime) {
                    currentTime += fortyPercentTime; // Execute 40% non-preemptively
                    currentProcess.setRemainingTime(currentProcess.getRemainingTime() - fortyPercentTime);
                } else {
                    currentTime += executionTime; // Execute the remaining burst time
                    currentProcess.setRemainingTime(0);
                }
    
                // If there's still time left to execute the process, update its quantum
                if (currentProcess.getRemainingTime() > 0) {
                    currentProcess.setQuantum(currentProcess.getQuantum() + 2); // Update quantum if interrupted
                } else {
                    currentProcess.setCompleted(true);
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    currentProcess.setendTime(currentTime);
                    System.out.println("Process " + currentProcess.getProcessName() + " completed. (Finish time: " + currentTime + ")");
                }
            } else {
                // If no process is ready, increase time by 1 and increase waiting time for all processes in the queue
                currentTime++; 
                for (Process p : processes) {
                    if (!p.isCompleted() && p.getArrivalTime() <= currentTime) {
                        p.incrementWaitingTime();  // Increment waiting time for processes that haven't finished
                    }
                }
            }
        }
    
        printStatistics(processes);
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
            System.out.println("Process " + p.getProcessName() + ":");
            System.out.println("Waiting Time: " + p.getWaitingTime());
            System.out.println("Turnaround Time: " + p.getTurnaroundTime());
            totalWaitingTime += p.getWaitingTime();
            totalTurnaroundTime += p.getTurnaroundTime();
        }

        System.out.println("Average Waiting Time: " + Math.ceil(totalWaitingTime / processes.size()));
        System.out.println("Average Turnaround Time: " + Math.ceil(totalTurnaroundTime / processes.size()));
    }
}
