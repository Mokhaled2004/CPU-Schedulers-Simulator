package schedulers;

import java.util.ArrayList;
import java.util.List;
import models.Process;

public class PriorityScheduler extends Scheduler {

    private int contextSwitchTime;  

    public PriorityScheduler(List<Process> processList, int contextSwitchTime) {
        super(processList);
        this.contextSwitchTime = contextSwitchTime;  
    }

    @Override
    public void startScheduling() {
        List<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;
    

        List<Process> executedProcesses = new ArrayList<>();
    
        while (!processList.isEmpty() || !readyQueue.isEmpty()) {
            for (int i = 0; i < processList.size(); i++) {
                Process process = processList.get(i);
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    processList.remove(i);
                    i--; 
                }
            }
    
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }
    
            readyQueue.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));
    
            Process currentProcess = readyQueue.remove(0);
    
            int startTime = Math.max(currentTime, currentProcess.getArrivalTime());
    
            int completionTime = startTime + currentProcess.getBurstTime();
    
            currentProcess.setTurnaroundTime(completionTime - currentProcess.getArrivalTime() + contextSwitchTime);
    
            currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
    
            currentTime = completionTime;
    
            executeProcess(currentProcess, startTime);
            executedProcesses.add(currentProcess);
    
            currentTime += contextSwitchTime; 
        }
    
        processList.addAll(executedProcesses);
    
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
                    ", Waiting Time: " + process.getWaitingTime() +
                    ", Turnaround Time: " + process.getTurnaroundTime() + ")");
        }
    }
}
