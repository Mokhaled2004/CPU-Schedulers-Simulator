import java.util.ArrayList;
import java.util.List;
import models.Process;
import schedulers.SRTFScheduler;

public class Main {
    public static void main(String[] args) {
        // Step 1: Create process objects
        Process p1 = new Process("P1", "Red", 0, 17, 4, 0,0);  // Arrival time 0, Burst time 8
        Process p2 = new Process("P2", "Blue", 3, 6, 9, 0,0); // Arrival time 1, Burst time 4
        Process p3 = new Process("P3", "Green", 4, 10, 3, 0,0); // Arrival time 2, Burst time 9
        Process p4 = new Process("P4", "Yellow", 29, 4, 8, 0,0); // Arrival time 3, Burst time 5

        // Step 2: Add processes to the list
        List<Process> processList = new ArrayList<>();
        processList.add(p1);
        processList.add(p2);
        processList.add(p3);
        processList.add(p4);

        // Step 3: Initialize the SJF Non-Preemptive Scheduler
        SRTFScheduler scheduler = new SRTFScheduler(processList,1,2,5);

        // Step 4: Start scheduling
        scheduler.startScheduling();

        
    }
}
