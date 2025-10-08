package view;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Visualisation extends Canvas implements IVisualisation {

    private final GraphicsContext gc;
    private static final int QUEUE_ITEMS_PER_ROW = 5;
    private static final double QUEUE_COLUMN_SPACING = 15.0;
    private static final double QUEUE_ROW_SPACING = 15.0;
    private static final double QUEUE_VERTICAL_OFFSET = 20.0;
    private int totalMorgue = 0;
    private int totalVaccine = 0;

    private static class VisualHuman {
        int id;
        double x, y, targetX, targetY;
        boolean isIll;
        int severity;
        Color color;
        static final double SPEED = 5.0;

        VisualHuman(int id, double startX, double startY, boolean isIll, int severity) {
            this.id = id;
            this.x = startX; this.y = startY;
            this.targetX = startX; this.targetY = startY;
            updateState(isIll, severity);
        }

        void update() {
            if (Math.abs(x - targetX) > SPEED) x += SPEED * Math.signum(targetX - x);
            if (Math.abs(y - targetY) > SPEED) y += SPEED * Math.signum(targetY - y);
        }

        void updateState(boolean isIll, int severity) {
            this.isIll = isIll;
            this.severity = severity;

            if (!this.isIll) {
                this.color = Color.LIGHTGREEN;
            } else {
                double hue = 60 - (this.severity * 6.0);
                if (hue < 0) hue = 0;
                this.color = Color.hsb(hue, 1.0, 1.0);
            }
        }
    }

    private static class VisualServicePoint {
        String name;
        double x, y, width, height;
        List<Integer> humanQueue = new CopyOnWriteArrayList<>();
        VisualServicePoint(String name, double x, double y) {
            this.name = name; this.x = x; this.y = y;
            this.width = 120; this.height = 60;
        }
    }

    private final Map<String, VisualServicePoint> servicePoints = new ConcurrentHashMap<>();
    private final Map<Integer, VisualHuman> humans = new ConcurrentHashMap<>();

    public Visualisation(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        setupServicePoints();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        };
        timer.start();
        clearDisplay();
    }

    private void setupServicePoints() {
        servicePoints.put("HOSPITAL", new VisualServicePoint("Hospital", 50, 200));
        servicePoints.put("TREATMENT", new VisualServicePoint("Treatment", 250, 50));
        servicePoints.put("AFTERCARE", new VisualServicePoint("Aftercare", 250, 200));
        servicePoints.put("CHECKUP", new VisualServicePoint("Checkup", 250, 350));
        servicePoints.put("VACCINE", new VisualServicePoint("Vaccine", 450, 350));
        servicePoints.put("MORGUE", new VisualServicePoint("Morgue", 450, 50));
    }

    private void draw() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());


        String morgueText = "Total Morgue: " + totalMorgue;
        String vaccineText = "Total Vaccinated: " + totalVaccine;
        gc.setFill(Color.LIGHTGREEN);
        gc.fillText(morgueText, 650, 50);
        gc.fillText(vaccineText, 650, 350);

        for (VisualServicePoint sp : servicePoints.values()) {
            gc.setFill(Color.CORNFLOWERBLUE);
            gc.fillRoundRect(sp.x, sp.y, sp.width, sp.height, 10, 10);
            gc.setStroke(Color.WHITE);
            gc.strokeRoundRect(sp.x, sp.y, sp.width, sp.height, 10, 10);
            gc.setFill(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFont(new Font("Arial", 14));
            gc.fillText(sp.name + "\nQueue: " + sp.humanQueue.size(), sp.x + sp.width / 2, sp.y + sp.height / 2);
        }

        for (VisualHuman human : humans.values()) {
            human.update();
            gc.setFill(human.color);
            gc.fillOval(human.x, human.y, 10, 10);
        }
    }

    @Override
    public void clearDisplay() {
        Platform.runLater(() -> {
            humans.clear();
            totalMorgue = 0;
            totalVaccine = 0;
            servicePoints.values().forEach(sp -> sp.humanQueue.clear());
        });
    }


    @Override
    public void humanArrived(int humanId, String atServicePointName, boolean isIll, int severity) {
        Platform.runLater(() -> {
            VisualServicePoint sp = servicePoints.get(atServicePointName);
            if (sp == null) return;
            humans.put(humanId, new VisualHuman(humanId, sp.x, sp.y, isIll, severity));
            updateHumanPosition(humanId, atServicePointName, isIll, severity);
        });
    }

    @Override
    public void updateHumanPosition(int humanId, String toServicePointName, boolean isIll, int severity) {
        Platform.runLater(() -> {
            VisualHuman vHuman = humans.get(humanId);
            VisualServicePoint targetSP = servicePoints.get(toServicePointName);
            if (toServicePointName.equals("MORGUE")) totalMorgue++;
            if (toServicePointName.equals("VACCINE")) totalVaccine++;
            if (vHuman == null || targetSP == null) return;

            vHuman.updateState(isIll, severity);

            servicePoints.values().forEach(sp -> sp.humanQueue.removeIf(id -> id == humanId));
            targetSP.humanQueue.add(humanId);

            int queuePosition = targetSP.humanQueue.indexOf(humanId);

            int column = queuePosition % QUEUE_ITEMS_PER_ROW;
            int row = queuePosition / QUEUE_ITEMS_PER_ROW;

            double gridWidth = (QUEUE_ITEMS_PER_ROW - 1) * QUEUE_COLUMN_SPACING;
            double startX = targetSP.x + (targetSP.width - gridWidth) / 2.0;

            vHuman.targetX = startX + (column * QUEUE_COLUMN_SPACING);
            vHuman.targetY = targetSP.y + targetSP.height + QUEUE_VERTICAL_OFFSET + (row * QUEUE_ROW_SPACING);
        });
    }

    @Override
    public void removeHuman(int humanId) {
        Platform.runLater(() -> {
            humans.remove(humanId);
            servicePoints.values().forEach(sp -> sp.humanQueue.removeIf(id -> id == humanId));
        });
    }
}