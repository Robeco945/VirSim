package view;

public interface IVisualisation {

    void clearDisplay();


    void humanArrived(int humanId, String atServicePoint, boolean isIll, int severity);
    void updateHumanPosition(int humanId, String toServicePoint, boolean isIll, int severity);


    void removeHuman(int humanId);
}