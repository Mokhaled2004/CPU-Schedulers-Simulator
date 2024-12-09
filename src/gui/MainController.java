package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import models.Process;
import schedulers.*;

import java.util.List;

public class MainController {

    public VBox graphContainer;

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

    @FXML
    private ComboBox<String> schedulerTypeComboBox;

    private Scheduler scheduler;

    public void initialize() {
        // Initialize ComboBox with scheduler options
        schedulerTypeComboBox.setItems(FXCollections.observableArrayList("FCFS", "SJF", "Priority", "SRTF"));

        // Set listener for ComboBox selection
        schedulerTypeComboBox.setOnAction(event -> {
            if (schedulerTypeComboBox.getValue() != null) {
                updateScheduler();
            } else {
                System.out.println("Please select a scheduler!");
            }
        });
    }

    private void updateScheduler() {
        String selectedScheduler = schedulerTypeComboBox.getValue();
        if (selectedScheduler == null || selectedScheduler.isEmpty()) {
            System.out.println("No scheduler selected.");
            return;
        }

        System.out.println("Selected Scheduler: " + selectedScheduler);

        switch (selectedScheduler) {
            case "FCFS":
                // Uncomment and use the correct FCFS implementation
                // scheduler = new FCFScheduler(createProcessList());
                System.out.println("FCFS Scheduler selected");
                break;
            case "SJF":
                scheduler = new SJFNonPreemptiveScheduler(createProcessList());
                System.out.println("SJF Scheduler selected");
                break;
            case "Priority":
                int contextSwitchTime = getContextSwitchTime();
                scheduler = new PriorityScheduler(createProcessList(), 1);
                System.out.println("Priority Scheduler selected");
                break;
            case "SRTF":
                // Uncomment and use the correct SRTF implementation
                scheduler = new SRTFScheduler(createProcessList(),1);
                System.out.println("SRTF Scheduler selected");
                break;
            default:
                System.out.println("Invalid scheduler type selected.");
                return;
        }


        // Start scheduling and update UI
        populateProcessTable();
        scheduler.startScheduling();
        updateStatistics();
        drawSchedulingGraph();
    }

    private int getContextSwitchTime() {
        // Placeholder for getting user input for context switch time, can be replaced with a proper dialog
        return 1;
    }

    private List<Process> createProcessList() {
        Process p1 = new Process("P1", "Red", 0, 17, 4, 0, 0, 4, 0);  // Arrival time 0, Burst time 17
        Process p2 = new Process("P2", "Blue", 3, 6, 9, 0, 0, 3, 0);  // Arrival time 3, Burst time 6
        Process p3 = new Process("P3", "Green", 4, 10, 3, 0, 0, 5, 0); // Arrival time 4, Burst time 10
        Process p4 = new Process("P4", "Yellow", 29, 4, 8, 0, 0, 2, 0); // Arrival time 29, Burst time 4
        return List.of(p1, p2, p3, p4);
    }

    private void updateStatistics() {
        System.out.println("Updating statistics...");
        if (scheduler != null) {
            averageWaitingTimeLabel.setText("Average Waiting Time: " + scheduler.calculateAverageWaitingTime());
            averageTurnaroundTimeLabel.setText("Average Turnaround Time: " + scheduler.calculateAverageTurnaroundTime());
        }
    }
    private void populateProcessTable() {
        System.out.println("Populating process table...");

        // Remove all children except the header row (rowIndex = 0)
        processTable.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

        if (scheduler != null) {
            int rowIndex = 1;
            for (Process process : scheduler.getProcessList()) {
                Label pidLabel = new Label(String.valueOf(process.getPid()));
                Label priorityLabel = new Label(String.valueOf(process.getPriority()));
                Label colorLabel = new Label(process.getColor());
                Label processNumberLabel = new Label(String.valueOf(process.getProcessNumber()));
                Label nameLabel = new Label(process.getProcessName());

                // Set the text color of all labels to white
                String whiteTextStyle = "-fx-text-fill: white;";
                pidLabel.setStyle(whiteTextStyle);
                priorityLabel.setStyle(whiteTextStyle);
                colorLabel.setStyle(whiteTextStyle);
                processNumberLabel.setStyle(whiteTextStyle);
                nameLabel.setStyle(whiteTextStyle);

                processTable.add(pidLabel, 0, rowIndex);
                processTable.add(priorityLabel, 1, rowIndex);
                processTable.add(colorLabel, 2, rowIndex);
                processTable.add(processNumberLabel, 3, rowIndex);
                processTable.add(nameLabel, 4, rowIndex);

                rowIndex++;
            }
        }
    }

    private void drawSchedulingGraph() {
        System.out.println("Drawing scheduling graph...");
        if (scheduler != null) {
            GraphicsContext gc = schedulingCanvas.getGraphicsContext2D();
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(0, 0, schedulingCanvas.getWidth(), schedulingCanvas.getHeight());

            int startY = 50;
            int verticalSpacing = 50;
            int rectHeight = 30;
            int marginLeft = 10;

            for (Process process : scheduler.getProcessList()) {
                Color color = switch (process.getColor()) {
                    case "Red" -> Color.RED;
                    case "Blue" -> Color.BLUE;
                    case "Green" -> Color.GREEN;
                    case "Yellow" -> Color.YELLOW;
                    default -> Color.GRAY;
                };

                gc.setFill(color);

                double rectWidth = (process.getEndTime() - process.getStartTime()) * 20;
                gc.fillRect(process.getStartTime() * 20 + marginLeft, startY, rectWidth, rectHeight);

                gc.setFill(Color.WHITE);
                double textX = marginLeft - 5;
                double textY = startY + (double) rectHeight / 2 + 5;
                gc.fillText(process.getProcessName(), textX, textY);

                startY += verticalSpacing;
            }
        }
    }
}
