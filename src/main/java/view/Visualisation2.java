package view;

import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.HashSet;
import java.util.Set;

public class Visualisation2 extends Canvas implements IVisualisation {
    private final GraphicsContext gc;

    private final Set<Integer> activeHumanIDs = new HashSet<>();
    private int totalArrivals = 0;

    public Visualisation2(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        clearDisplay();
    }

    private void draw() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());

        gc.setFill(Color.LIGHTGREEN);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        gc.setFont(new Font("Arial", 24));

        String activeText = "Patients currently in system: " + activeHumanIDs.size();
        String totalText = "Total patients arrived: " + totalArrivals;

        gc.fillText(activeText, 20, 20);
        gc.fillText(totalText, 20, 60);
    }

    @Override
    public void clearDisplay() {
        Platform.runLater(() -> {
            activeHumanIDs.clear();
            totalArrivals = 0;
            draw();
        });
    }

    @Override
    public void humanArrived(int humanId, String atServicePoint, boolean isIll, int severity) {
        Platform.runLater(() -> {
            totalArrivals++;
            activeHumanIDs.add(humanId);
            draw();
        });
    }

    @Override
    public void updateHumanPosition(int humanId, String toServicePoint, boolean isIll, int severity) {

    }

    @Override
    public void removeHuman(int humanId) {
        Platform.runLater(() -> {
            activeHumanIDs.remove(humanId);
            draw(); // Redraw with updated numbers
        });
    }
}