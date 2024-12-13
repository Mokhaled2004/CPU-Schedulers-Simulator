import java.util.ArrayList;
import java.util.List;
import models.Process;
import schedulers.FCAIScheduler;
import schedulers.PriorityScheduler;
import schedulers.SJFNonPreemptiveScheduler;
import schedulers.SRTFScheduler;
import schedulers.SrtfStarvation;

public class Main {
    public static void main(String[] args) {

        Process p1 = new Process("P1", "Red", 0, 17, 4, 0, 0,4,0);  
        Process p2 = new Process("P2", "Blue", 3, 6, 9, 0, 0,3,0);  
        Process p3 = new Process("P3", "Green", 4, 10, 3, 0, 0,5,0); 
        Process p4 = new Process("P4", "Yellow", 29, 4, 8, 0, 0,2,0); 

        List<Process> processList = new ArrayList<>();
        processList.add(p1);
        processList.add(p2);
        processList.add(p3);
        processList.add(p4);


        PriorityScheduler priorityScheduler = new PriorityScheduler(processList,1);
        SJFNonPreemptiveScheduler  sjfScheduler = new SJFNonPreemptiveScheduler(processList);
        SRTFScheduler srtfScheduler = new SRTFScheduler(processList, 1);
        SrtfStarvation srtfStarvation = new SrtfStarvation(processList, 1, 1, 25);
        FCAIScheduler fcaiScheduler = new FCAIScheduler();




        //priorityScheduler.startScheduling();
        //sjfScheduler.startScheduling();
        //srtfScheduler.startScheduling();
        //srtfStarvation.startScheduling();
        //fcaiScheduler.FCAIScheduling(processList);
    
    }

}
