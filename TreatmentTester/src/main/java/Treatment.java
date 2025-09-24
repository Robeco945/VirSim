public class Treatment {
    private static final String NAME = "Treatment";
    private static final int CAPACITY = 2;

    private double serviceTime;

    public Treatment() {
        this.serviceTime = getRandomServiceTime();
    }

    public String getName() {
        return NAME;
    }

    public int getCapacity() {
        return CAPACITY;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    private static double getRandomServiceTime() {
        // Simulate ~20 ± 5 minutes
        return 20 + (Math.random() * 10 - 5);
    }

    public void servePatient() {
        System.out.println("Serving patient at " + NAME +
                " for " + serviceTime + " minutes.");
    }
}
