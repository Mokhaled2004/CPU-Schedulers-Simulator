package schedulers;

import java.util.ArrayList;
import java.util.List;
import models.Process;

public class PriorityScheduler extends Scheduler {

    public PriorityScheduler(List<Process> processList) {
        super(processList);
    }

    @Override
    public void startScheduling() {
        List<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;

        // Keep track of the order of execution for display
        List<Process> executedProcesses = new ArrayList<>();

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

            // Sort the ready queue by priority (ascending order)
            readyQueue.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));

            // Pick the process with the highest priority
            Process currentProcess = readyQueue.remove(0);

            // Calculate waiting time and turnaround time
            int startTime = Math.max(currentTime, currentProcess.getArrivalTime());
            currentProcess.setWaitingTime(startTime - currentProcess.getArrivalTime());
            currentTime = startTime + currentProcess.getBurstTime();
            currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());

            // Execute the process and save it for later display
            executeProcess(currentProcess, startTime);
            executedProcesses.add(currentProcess);
        }

        // Replace the original list with executed processes (ordered execution)
        processList.addAll(executedProcesses);

        displayExecutionOrder();
    }

    private void executeProcess(Process process, int startTime) {
        System.out.println("Time " + startTime + ": Executing Process " + process.getProcessName() +
                " with Priority " + process.getPriority());
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
        System.out.println("Execution Order (Priority Scheduling with Arrival Time): ");
        for (Process process : processList) {
            System.out.println(process.getProcessName() +
                    " (Priority: " + process.getPriority() +
                    ", Arrival Time: " + process.getArrivalTime() +
                    ", Waiting Time: " + process.getWaitingTime() +
                    ", Turnaround Time: " + process.getTurnaroundTime() + ")");
        }
    }
}
