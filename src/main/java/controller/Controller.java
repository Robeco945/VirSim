package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import simu.framework.Clock;
import simu.framework.IEngine;
import simu.model.MyEngine;
import view.ISimulatorUI;

public class Controller implements IControllerVtoM, IControllerMtoV {
    @FXML
    public TextField time;
    @FXML
    public TextField delay;
    @FXML
    public Button startButton;

    private IEngine engine;
	private ISimulatorUI ui;
	
	public Controller(ISimulatorUI ui) {
		this.ui = ui;
	}

	/* Engine control: */
	@Override
	public void startSimulation() {
        Clock.getInstance().setClock(0); // reset clock
		engine = new MyEngine(this); // new Engine thread is created for every simulation
		engine.setSimulationTime(ui.getTime());
		engine.setDelay(ui.getDelay());
		ui.getVisualisation().clearDisplay();
		((Thread) engine).start();
		//((Thread)engine).run(); // Never like this, why?
	}

	@Override
	public void decreaseSpeed() { // hidastetaan moottorisäiettä
		engine.setDelay((long)(engine.getDelay()*1.10));
	}

	@Override
	public void increaseSpeed() { // nopeutetaan moottorisäiettä
		engine.setDelay((long)(engine.getDelay()*0.9));
	}


	@Override
	public void showEndTime(double time) {
		Platform.runLater(()->ui.setEndingTime(time));
	}

    @Override
    public void visualiseHumanArrival(int humanId, String servicePointName, boolean isIll, int severity) {
        Platform.runLater(() -> ui.getVisualisation().humanArrived(humanId, servicePointName, isIll, severity));
    }

    @Override
    public void visualiseHumanMove(int humanId, String servicePointName, boolean isIll, int severity) {
        Platform.runLater(() -> ui.getVisualisation().updateHumanPosition(humanId, servicePointName, isIll, severity));
    }

    @Override
    public void visualiseHumanDeparture(int humanId) {
        Platform.runLater(() -> ui.getVisualisation().removeHuman(humanId));
    }
}
