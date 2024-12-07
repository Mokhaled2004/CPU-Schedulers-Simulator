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
        // Step 1: Create process objects
        Process p1 = new Process("P1", "Red", 0, 8, 0, 0);
        Process p2 = new Process("P2", "Blue", 1, 4, 0, 0);
        Process p3 = new Process("P3", "Green", 2, 9, 0, 0);
        Process p4 = new Process("P4", "Yellow", 3, 5, 0, 0);

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
        Canvas canvas = createCanvas();

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

    private Canvas createCanvas() {
        // Create Canvas for the scheduling graph
        Canvas canvas = new Canvas(800, 200);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Define burst times and process names
        String[] processes = {"P1", "P2", "P3", "P4"};
        int[] burstTimes = {8, 4, 9, 5};
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};

        // Draw the process scheduling graph (based on burst times)
        int startX = 50;
        for (int i = 0; i < processes.length; i++) {
            gc.setFill(colors[i]);
            gc.fillRect(startX, 100, burstTimes[i] * 50, 30);  // Bar width based on burst time
            startX += burstTimes[i] * 50;
        }

        return canvas;
    }

    public static void main(String[] args) {
        launch(args); // Launch JavaFX application
    }
}
