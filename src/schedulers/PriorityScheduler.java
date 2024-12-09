package schedulers;

import java.util.ArrayList;
import java.util.List;
import models.Process;

public class PriorityScheduler extends Scheduler {

    private final int contextSwitchTime;  // Variable to hold the context switch time

    public PriorityScheduler(List<Process> processList, int contextSwitchTime) {
        super(processList);
        this.contextSwitchTime = contextSwitchTime;  // Initialize context switch time
    }

    @Override
    public void startScheduling() {
        List<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;

        processList = new ArrayList<>(processList);

        // Keep track of the order of execution for display
        List<Process> executedProcesses = new ArrayList<>();
        List<Process> remainingProcesses = new ArrayList<>(processList);  // Create a copy of the process list

        while (!remainingProcesses.isEmpty() || !readyQueue.isEmpty()) {
            // Add processes to the ready queue if they've arrived
            for (int i = 0; i < remainingProcesses.size(); i++) {
                Process process = remainingProcesses.get(i);
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    remainingProcesses.remove(i);
                    i--; // Adjust index after removing a process
                }
            }

            // If the ready queue is empty, move time forward
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Sort the ready queue by priority (ascending order), breaking ties by arrival time
            readyQueue.sort((p1, p2) -> {
                int priorityCompare = Integer.compare(p1.getPriority(), p2.getPriority());
                return (priorityCompare != 0) ? priorityCompare : Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
            });

            // Pick the process with the highest priority
            Process currentProcess = readyQueue.remove(0);  // Use remove(0) instead of removeFirst() (no LinkedList)

            // Calculate start time (either current time or the arrival time, whichever is greater)
            int startTime = Math.max(currentTime, currentProcess.getArrivalTime());

            // Calculate completion time
            int completionTime = startTime + currentProcess.getBurstTime();

            // Set turnaround time: Turnaround Time = Completion Time - Arrival Time
            currentProcess.setTurnaroundTime(completionTime - currentProcess.getArrivalTime());

            // Set waiting time: Waiting Time = Turnaround Time - Burst Time
            currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

            // Set end time for the process
            currentProcess.setEndTime(completionTime);

            // Update current time after the process finishes
            currentTime = completionTime;

            // Execute the process and save it for later display
            executeProcess(currentProcess, startTime);
            executedProcesses.add(currentProcess);

            // Apply context switch time before the next process starts
            currentTime += contextSwitchTime; // Add context switch time after each process execution
        }

        // Keep the original process list intact for UI updates
        processList.clear();
        processList.addAll(executedProcesses);  // Re-add the processes in the order they were executed

        displayExecutionOrder();
        System.out.printf("Average Waiting Time: %.2f\n", calculateAverageWaitingTime());
        System.out.printf("Average Turnaround Time: %.2f\n", calculateAverageTurnaroundTime());
    }

    private void executeProcess(Process process, int startTime) {
        System.out.println("Time " + startTime + ": Executing Process " + process.getProcessName() +
                " with Priority " + process.getPriority());
    }

    @Override
    public double calculateAverageWaitingTime() {
        return super.calculateAverageWaitingTime();
    }

    @Override
    public double calculateAverageTurnaroundTime() {
        return super.calculateAverageTurnaroundTime();
    }

    @Override
    public void displayExecutionOrder() {
        System.out.println("Execution Order (Priority Scheduling with Arrival Time): ");
        for (Process process : processList) {
            System.out.println(process.getProcessName() +
                    ", Waiting Time: " + process.getWaitingTime() +
                    ", Turnaround Time: " + process.getTurnaroundTime() +
                    ", End Time: " + process.getEndTime());  // Display end time
        }
    }
}
