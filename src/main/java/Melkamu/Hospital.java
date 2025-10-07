package Melkamu;

public abstract class Hospital {
    private String name;
    private int serviceTime;
    private int capacity;

    public Hospital(String name, int serviceTime, int capacity) {
        this.name = name;
        this.serviceTime = serviceTime;
        this.capacity = capacity;
    }
    public abstract void appointment(String patientName);

    public String getName() {
        return name;
    }

    public double getServiceTime(){
        return serviceTime;
    }

    public int getCapacity(){
        return capacity;
    }

    public void setServiceTime(int serviceTime){
        this.serviceTime = serviceTime;
    }

    public void servePatient(String patientName){
        System.out.println("Serving patient " + patientName + " at " + name + " for " + serviceTime + " minutes.");

    }

}
