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
        processList = new ArrayList<>(processList);
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();
        List<Process> completedProcesses = new ArrayList<>();

        while (completedProcesses.size() < processList.size()) {
            // Add processes that have arrived to the ready queue
            for (Process process : processList) {
                if (!completedProcesses.contains(process) && process.getArrivalTime() <= currentTime) {
                    if (!readyQueue.contains(process)) {
                        readyQueue.add(process);
                    }
                }
            }

            // Sort by burst time to simulate SJF
            readyQueue.sort(Comparator.comparingInt(Process::getBurstTime));

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.remove(0);

                // Store the start and end times for Gantt chart
                currentProcess.setStartTime(currentTime);
                currentTime = Math.max(currentTime, currentProcess.getArrivalTime()) + currentProcess.getBurstTime();
                currentProcess.setEndTime(currentTime);

                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime() - currentProcess.getBurstTime());
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                completedProcesses.add(currentProcess);
            } else {
                currentTime++;
            }
        }

        this.processList = completedProcesses;
        displayExecutionOrder();
        System.out.printf("Average Waiting Time: %.2f\n", calculateAverageWaitingTime());
        System.out.printf("Average Turnaround Time: %.2f\n", calculateAverageTurnaroundTime());
    }


    @Override
    public void displayExecutionOrder() {
        System.out.println("Execution Order:");
        for (Process process : processList) {
            System.out.println(process.getProcessName() +
                    " [WT: " + process.getWaitingTime() +
                    ", TAT: " + process.getTurnaroundTime() + "]");
        }
    }
}
