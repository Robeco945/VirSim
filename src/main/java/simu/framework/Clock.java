package simu.framework;

public class Clock {
	private double time;
	private static Clock instance;
	
	private Clock(){
		time = 0;
	}
	
	public static Clock getInstance(){
		if (instance == null){
			instance = new Clock();
		}
		return instance;
	}
	
	public void setClock(double time){
		this.time = time;
	}

	public double getClock(){
		return time;
	}
}
