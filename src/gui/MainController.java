package gui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import models.Process;
import schedulers.SJFNonPreemptiveScheduler;

import java.util.List;

public class MainController {

    @FXML
    private Label averageWaitingTimeLabel;

    @FXML
    private Label averageTurnaroundTimeLabel;

    @FXML
    private VBox processesContainer;

    @FXML
    private Canvas schedulingCanvas;

    private SJFNonPreemptiveScheduler scheduler;

    public void initialize() {
        // Create processes
        Process p1 = new Process("P1", "Red", 0, 8, 0, 0, 1);
        Process p2 = new Process("P2", "Blue", 1, 4, 0, 0, 2);
        Process p3 = new Process("P3", "Green", 2, 9, 0, 0, 3);
        Process p4 = new Process("P4", "Yellow", 3, 5, 0, 0, 4);

        List<Process> processList = List.of(p1, p2, p3, p4);

        // Initialize scheduler
        scheduler = new SJFNonPreemptiveScheduler(processList);
        scheduler.startScheduling();

        // Update the UI
        updateStatistics();
        populateProcessList();
        drawSchedulingGraph();
    }

    private void updateStatistics() {
        averageWaitingTimeLabel.setText("Average Waiting Time: " + scheduler.calculateAverageWaitingTime());
        averageTurnaroundTimeLabel.setText("Average Turnaround Time: " + scheduler.calculateAverageTurnaroundTime());
    }

    private void populateProcessList() {
        for (Process process : scheduler.getProcessList()) {
            processesContainer.getChildren().add(
                    new Label(process.getProcessName() + " [WT: " + process.getWaitingTime() +
                            ", TAT: " + process.getTurnaroundTime() + "]")
            );
        }
    }

    private void drawSchedulingGraph() {
        GraphicsContext gc = schedulingCanvas.getGraphicsContext2D();
        int startX = 50;

        for (Process process : scheduler.getProcessList()) {
            Color color = switch (process.getColor()) {
                case "Red" -> Color.RED;
                case "Blue" -> Color.BLUE;
                case "Green" -> Color.GREEN;
                case "Yellow" -> Color.YELLOW;
                default -> Color.GRAY;
            };

            gc.setFill(color);
            gc.fillRect(startX, 100, process.getBurstTime() * 50, 30);
            startX += process.getBurstTime() * 50;
        }
    }
}
