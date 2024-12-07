import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import models.Process;
import schedulers.SJFNonPreemptiveScheduler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Step 1: Create process objects with name, color, arrival time, burst time, waiting time, turnaround time, and priority
        Process p1 = new Process("P1", "Red", 0, 8, 0, 0, 1); // Added priority value
        Process p2 = new Process("P2", "Blue", 1, 4, 0, 0, 2);
        Process p3 = new Process("P3", "Green", 2, 9, 0, 0, 3);
        Process p4 = new Process("P4", "Yellow", 3, 5, 0, 0, 4);

        // Step 2: Add processes to the list
        List<Process> processList = new ArrayList<>();
        processList.add(p1);
        processList.add(p2);
        processList.add(p3);
        processList.add(p4);

        // Step 3: Initialize the SJF Non-Preemptive Scheduler
        SJFNonPreemptiveScheduler scheduler = new SJFNonPreemptiveScheduler(processList);

        // Step 4: Start scheduling
        scheduler.startScheduling();

        // Create the main container (VBox) with some padding and spacing
        VBox vbox = new VBox(15);
        vbox.setStyle("-fx-padding: 20; -fx-background-color: #2f2f2f;");

        // Step 5: Add labels for statistics and execution order
        addLabels(vbox, scheduler);

        // Step 6: Set up Canvas to visualize CPU scheduling graph
        Canvas canvas = createCanvas(scheduler);

        // Add Canvas and labels to VBox
        vbox.getChildren().add(canvas);

        // Step 7: Set up the scene
        Scene scene = new Scene(vbox, 800, 500);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());  // Apply CSS
        primaryStage.setTitle("CPU Scheduler Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addLabels(VBox vbox, SJFNonPreemptiveScheduler scheduler) {
        // Add the statistical labels
        vbox.getChildren().add(new Label("Average Waiting Time: " + scheduler.calculateAverageWaitingTime()));
        vbox.getChildren().add(new Label("Average Turnaround Time: " + scheduler.calculateAverageTurnaroundTime()));

        // Add execution order label
        vbox.getChildren().add(new Label("Execution Order:"));
        for (Process process : scheduler.getProcessList()) {
            vbox.getChildren().add(new Label(process.getProcessName() + " [WT: " +
                    process.getWaitingTime() + ", TAT: " + process.getTurnaroundTime() + "]"));
        }
    }

    private Canvas createCanvas(SJFNonPreemptiveScheduler scheduler) {
        // Create Canvas for the scheduling graph
        Canvas canvas = new Canvas(800, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Define burst times and process names
        List<Process> processes = scheduler.getProcessList();  // Get the process list from the scheduler
        int startX = 50; // Starting X position

        // Draw the process scheduling graph (based on burst times)
        for (Process process : processes) {
            Color processColor;
            switch (process.getColor()) {
                case "Red":
                    processColor = Color.RED;
                    break;
                case "Blue":
                    processColor = Color.BLUE;
                    break;
                case "Green":
                    processColor = Color.GREEN;
                    break;
                case "Yellow":
                    processColor = Color.YELLOW;
                    break;
                default:
                    processColor = Color.GRAY;
            }

            // Draw the process on the canvas (based on burst time)
            gc.setFill(processColor);
            gc.fillRect(startX, 100, process.getBurstTime() * 50, 30);  // Bar width based on burst time
            startX += process.getBurstTime() * 50; // Update the starting X position for the next process
        }

        return canvas;
    }

    public static void main(String[] args) {
        launch(args); // Launch JavaFX application
    }
}
