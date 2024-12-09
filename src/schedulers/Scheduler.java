package schedulers;

import java.util.List;
import models.Process;

public abstract class Scheduler {

    protected List<Process> processList;

    public Scheduler(List<Process> processList) {
        this.processList = processList;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public abstract void startScheduling();

    protected void calculateWaitingTime() {
    }

    protected void calculateTurnaroundTime() {
    }

    public double calculateAverageWaitingTime() {
        return 0;
    }

    public double calculateAverageTurnaroundTime() {
        return 0;
    }

    public void displayExecutionOrder() {
    }
}