import java.util.ArrayList;
import java.util.List;
import models.Process;
import schedulers.PriorityScheduler;

public class Main {
    public static void main(String[] args) {
        Process p1 = new Process("P1", "Red", 0, 10, 5, 0);  // Priority 5, Quantum 0
        Process p2 = new Process("P2", "Blue", 1, 5, 3, 0);  // Priority 3, Quantum 0
        Process p3 = new Process("P3", "Green", 2, 8, 4, 0); // Priority 4, Quantum 0
        Process p4 = new Process("P4", "Yellow", 3, 6, 2, 0); // Priority 2, Quantum 0

        List<Process> processList = new ArrayList<>();
        processList.add(p1);
        processList.add(p2);
        processList.add(p3);
        processList.add(p4);

        PriorityScheduler scheduler = new PriorityScheduler(processList);

        scheduler.startScheduling();

        System.out.println("Average Waiting Time: " + scheduler.calculateAverageWaitingTime());
        System.out.println("Average Turnaround Time: " + scheduler.calculateAverageTurnaroundTime());
    }
}
