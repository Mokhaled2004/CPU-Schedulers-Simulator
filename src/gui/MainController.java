package gui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Process;
import schedulers.SJFNonPreemptiveScheduler;

import java.util.List;

public class MainController {

    @FXML
    private Canvas schedulingCanvas;

    @FXML
    private VBox processPropertiesContainer;

    @FXML
    private GridPane processTable;

    @FXML
    private Label averageWaitingTimeLabel;

    @FXML
    private Label averageTurnaroundTimeLabel;

    @FXML
    private Label executionOrderLabel;

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
        populateProcessTable();
        drawSchedulingGraph();
    }

    private void updateStatistics() {
        averageWaitingTimeLabel.setText("Average Waiting Time: " + scheduler.calculateAverageWaitingTime());
        averageTurnaroundTimeLabel.setText("Average Turnaround Time: " + scheduler.calculateAverageTurnaroundTime());
    }

    private void populateProcessTable() {
        // Dynamically add each process's details to the GridPane
        int rowIndex = 1; // Start adding rows from the second row
        for (Process process : scheduler.getProcessList()) {
            Label pidLabel = new Label(String.valueOf(process.getPid()));
            pidLabel.setStyle("-fx-text-fill: white;");

            Label priorityLabel = new Label(String.valueOf(process.getPriority()));
            priorityLabel.setStyle("-fx-text-fill: white;");

            Label colorLabel = new Label(process.getColor());
            colorLabel.setStyle("-fx-text-fill: white;");

            Label processNumberLabel = new Label(String.valueOf(process.getProcessNumber()));
            processNumberLabel.setStyle("-fx-text-fill: white;");

            Label nameLabel = new Label(process.getProcessName());
            nameLabel.setStyle("-fx-text-fill: white;");

            // Add the labels to the corresponding grid cells
            processTable.add(pidLabel, 0, rowIndex);
            processTable.add(priorityLabel, 1, rowIndex);
            processTable.add(colorLabel, 2, rowIndex);
            processTable.add(processNumberLabel, 3, rowIndex);
            processTable.add(nameLabel, 4, rowIndex);

            rowIndex++; // Move to the next row
        }
    }

    private void drawSchedulingGraph() {
        GraphicsContext gc = schedulingCanvas.getGraphicsContext2D();
        int startX = 50; // Starting X position for drawing

        // Draw the scheduling graph on the canvas
        for (Process process : scheduler.getProcessList()) {
            Color color = switch (process.getColor()) {
                case "Red" -> Color.RED;
                case "Blue" -> Color.BLUE;
                case "Green" -> Color.GREEN;
                case "Yellow" -> Color.YELLOW;
                default -> Color.GRAY;
            };

            gc.setFill(color);
            // Draw a rectangle for each process based on its burst time
            gc.fillRect(startX, 100, process.getBurstTime() * 50, 30);
            startX += process.getBurstTime() * 50; // Update the X position for the next process
        }
    }
}
