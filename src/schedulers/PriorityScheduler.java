package schedulers;

import java.util.List;
import models.Process;

public class PriorityScheduler extends Scheduler {

    public PriorityScheduler(List<Process> processList) {
        super(processList);
    }

    @Override
    public void startScheduling() {
        sortProcessesByPriority();

        for (Process process : processList) {
            executeProcess(process);
        }

        calculateWaitingTime();
        calculateTurnaroundTime();

        displayExecutionOrder();
    }

    private void sortProcessesByPriority() {
        processList.sort((p1, p2) -> Integer.compare(p1.getPriorityNumber(), p2.getPriorityNumber()));
    }

    private void executeProcess(Process process) {
        saveState();
        
        System.out.println("Executing Process: " + process.getProcessName() + " with Priority: " + process.getPriorityNumber());


        restoreState();
    }

    private void saveState() {
        System.out.println("Saving current process state...");
    }

    private void restoreState() {
        System.out.println("Restoring process state...");
    }

    @Override
    protected void calculateWaitingTime() {
        int waitingTime = 0;
        for (int i = 0; i < processList.size(); i++) {
            if (i == 0) {
                processList.get(i).setWaitingTime(0); // First process has zero waiting time
            } else {
                Process previousProcess = processList.get(i - 1);
                int currentWaitingTime = previousProcess.getWaitingTime() + previousProcess.getBurstTime();
                processList.get(i).setWaitingTime(currentWaitingTime);
            }
        }
    }

    @Override
    protected void calculateTurnaroundTime() {
        for (Process process : processList) {
            int turnaroundTime = process.getWaitingTime() + process.getBurstTime();
            process.setTurnaroundTime(turnaroundTime);
        }
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
        System.out.println("Execution Order (Non-Preemptive Priority): ");
        for (Process process : processList) {
            System.out.println(process.getProcessName() + " (Priority: " + process.getPriorityNumber() + ")");
        }
    }
}
