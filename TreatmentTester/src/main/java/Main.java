public class Main {
    public static void main(String[] args) {
        Treatment treatment = new Treatment();

        System.out.println("Station: " + treatment.getName());
        System.out.println("Capacity: " + treatment.getCapacity());
        treatment.servePatient();
    }
}
