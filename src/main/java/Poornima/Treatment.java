package Poornima;// Poornima.Treatment.java

import Melkamu.Hospital;

public class Treatment extends Hospital {
    public Treatment() {
        // Call parent constructor with name, random service time, and capacity
        super("Poornima.Treatment", getRandomServiceTime(), 2);
    }

    @Override
    public void appointment(String patientName) {
        System.out.println("Appointment booked for " + patientName + " at " + getName());
    }

    private static int getRandomServiceTime() {
        // Simulate ~20 ± 5 minutes
        return (int)(20 + (Math.random() * 10 - 5));
    }
}
