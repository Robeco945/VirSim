package controller;

/* interface for the engine */
public interface IControllerMtoV {
		public void showEndTime(double time);
    void visualiseHumanArrival(int humanId, String servicePointName, boolean isIll, int severity);
    void visualiseHumanMove(int humanId, String servicePointName, boolean isIll, int severity);
    void visualiseHumanDeparture(int humanId);
}
