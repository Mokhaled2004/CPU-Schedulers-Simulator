package schedulers;

import java.util.*;
import models.Process;

public class FCAIScheduler {
    private List<Process> processes; // List of processes
    private Queue<Process> readyQueue; // Ready queue to manage processes
    private double V1; // Last arrival time / 10
    private double V2; // Max burst time / 10

    public FCAIScheduler(List<Process> processes) {
        this.processes = processes;
        this.readyQueue = new LinkedList<>();
        calculateV1AndV2();
    }

    // Calculate V1 and V2 based on the last arrival time and max burst time
    private void calculateV1AndV2() {
        int lastArrivalTime = processes.stream().mapToInt(Process::getArrivalTime).max().orElse(0);
        int maxBurstTime = processes.stream().mapToInt(Process::getBurstTime).max().orElse(0);

        V1 = lastArrivalTime / 10.0;
        V2 = maxBurstTime / 10.0;
    }

    // Calculate FCAI factor for each process in the ready queue
    private void calculateFCAIFactors() {
        for (Process p : readyQueue) {
            int priorityFactor = (10 - p.getPriority());
            int arrivalFactor = (int) (p.getArrivalTime() / V1);
            int remainingFactor = (int) (p.getRemainingTime() / V2);

            p.setFcaifactor(priorityFactor + arrivalFactor + remainingFactor, remainingFactor, remainingFactor);
        }
    }

    // Execute processes using FCAI scheduling algorithm
    public void execute() {
        // Sort processes by arrival time initially
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add processes to the ready queue based on arrival time
            for (Iterator<Process> iterator = processes.iterator(); iterator.hasNext(); ) {
                Process process = iterator.next();
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    iterator.remove();
                }
            }

            if (readyQueue.isEmpty()) {
                // If no processes are ready, advance time to the next arrival time
                if (!processes.isEmpty()) {
                    currentTime = processes.get(0).getArrivalTime();
                }
                continue;
            }

            // Calculate FCAI factors for all processes in the ready queue
            calculateFCAIFactors();

            // Sort the ready queue by FCAI Factor
            List<Process> sortedQueue = new ArrayList<>(readyQueue);
            sortedQueue.sort(Comparator.comparingDouble(Process::getFcaifactor).reversed());
            readyQueue = new LinkedList<>(sortedQueue);

            // Fetch the next process to execute
            Process currentProcess = readyQueue.poll();

            // Calculate quantum execution time (40% of quantum, rounded up)
            int quantumTime = (int) Math.ceil(0.4 * currentProcess.getQuantum());

            // Execute the process for the first 40% of its quantum (non-preemptively)
            if (currentProcess.getRemainingTime() <= quantumTime) {
                // Process can complete within this quantum
                currentTime += currentProcess.getRemainingTime();
                currentProcess.setRemainingTime(0);
                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime() - currentProcess.getBurstTime());
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                System.out.println("Process " + currentProcess.getProcessName() + " completed at time " + currentTime);
            } else {
                // Process is preempted after 40% execution
                currentTime += quantumTime;
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - quantumTime);

                // Update quantum as per the rules
                if (currentProcess.getRemainingTime() > 0) {
                    currentProcess.setQuantum(currentProcess.getQuantum() + 2); // Increase by 2 if it still has remaining time
                } else {
                    currentProcess.setQuantum(currentProcess.getQuantum() + quantumTime); // Increase by unused quantum if preempted
                }

                // Recalculate FCAI factor for preempted process
                calculateFCAIFactors();

                // Re-add the process to the ready queue for further execution
                readyQueue.add(currentProcess);
            }
        }
    }
}
