package view;

import controller.Controller;
import controller.IControllerVtoM;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import simu.framework.Trace;
import simu.framework.Trace.Level;

import java.text.DecimalFormat;

public class SimulatorGUI extends Application implements ISimulatorUI {

    private IControllerVtoM controller;

    @FXML private TextField time;
    @FXML private TextField delay;
    @FXML private Button startButton;
    @FXML private Pane displayContainer;
    private IVisualisation display;

    @Override
    public void init() {
        Trace.setTraceLevel(Level.INFO);
        controller = new Controller(this);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/virsim_view.fxml"));
            loader.setController(this);
            Parent root = loader.load();

            primaryStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });
            primaryStage.setTitle("Simulator");
            display = new Visualisation2(400,200);
            displayContainer.getChildren().add((Canvas) display);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStart() {
        try (java.io.FileWriter fw = new java.io.FileWriter("service_log.csv", false)) {
            fw.write(String.format(""
            ));
        } catch (java.io.IOException e) {
            System.err.println("CSV logging failed: " + e.getMessage());
        }
        startButton.setDisable(true);
        controller.startSimulation();
    }

    @Override
    public double getTime() {
        return Double.parseDouble(time.getText());
    }

    @Override
    public long getDelay() {
        return Long.parseLong(delay.getText());
    }

    @Override
    public void setEndingTime(double time) {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        // this.results.setText(formatter.format(time));
        startButton.setDisable(false); // Re-enable start button when simulation ends
    }

    @Override
    public IVisualisation getVisualisation() {
        return (IVisualisation) display;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
