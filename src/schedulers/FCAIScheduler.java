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

    // Calculate V1 and V2
    private void calculateV1AndV2() {
        int lastArrivalTime = processes.stream().mapToInt(Process::getArrivalTime).max().orElse(0);
        int maxBurstTime = processes.stream().mapToInt(Process::getBurstTime).max().orElse(0);

        V1 = lastArrivalTime / 10.0;
        V2 = maxBurstTime / 10.0;
    }

    // FCAI Factor Calculation
    private void calculateFCAIFactors() {
        for (Process p : readyQueue) {
            // Calculate each component of the FCAI factor
            int priorityFactor = (10 - p.getPriority());
            int arrivalFactor = (int) (p.getArrivalTime() / V1);
            int remainingFactor = (int) (p.getRemainingTime() / V2);
        
            // Set the FCAI factor using the calculated components
            p.setFcaifactor(priorityFactor, arrivalFactor, remainingFactor);
        }
        
    }

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
                currentTime = processes.get(0).getArrivalTime();
                continue;
            }

            for (Process p : readyQueue) {
                // Calculate each component of the FCAI factor
                int priorityFactor = (10 - p.getPriority());
                int arrivalFactor = (int) (p.getArrivalTime() / V1);
                int remainingFactor = (int) (p.getRemainingTime() / V2);
            
                // Set the FCAI factor using the calculated components
                p.setFcaifactor(priorityFactor, arrivalFactor, remainingFactor);
            }
            

            // Sort the ready queue by FCAI Factor
            List<Process> sortedQueue = new ArrayList<>(readyQueue);
            sortedQueue.sort(Comparator.comparingDouble(Process::getFcaifactor).reversed());
            readyQueue = new LinkedList<>(sortedQueue);

            // Fetch the next process to execute
            Process currentProcess = readyQueue.poll();

            // Calculate quantum execution time (40% of quantum, rounded up)
            int quantumTime = (int) Math.ceil(0.4 * currentProcess.getQuantum());

            if (currentProcess.getRemainingTime() <= quantumTime) {
                // Process can complete within this quantum
                currentTime += currentProcess.getRemainingTime();
                currentProcess.setRemainingTime(0);
                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime() - currentProcess.getBurstTime());
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());

                System.out.println("Process " + currentProcess.getProcessName() + " completed at time " + currentTime);
            } else {
                // Process is preempted
                currentTime += quantumTime;
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - quantumTime);

                // Update quantum as per the rules
                currentProcess.setQuantum(currentProcess.getQuantum() + 2);

                    // Calculate each component of the FCAI factor
                    int priorityFactor = (10 - currentProcess.getPriority());
                    int arrivalFactor = (int) (currentProcess.getArrivalTime() / V1);
                    int remainingFactor = (int) (currentProcess.getRemainingTime() / V2);
                
                    // Set the FCAI factor using the calculated components
                    currentProcess.setFcaifactor(priorityFactor, arrivalFactor, remainingFactor);
                
                // Recalculate FCAI Factor


                // Re-add the process to the ready queue
                readyQueue.add(currentProcess);
            }
        }
    }
}
