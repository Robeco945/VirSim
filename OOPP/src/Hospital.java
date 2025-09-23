public abstract class Hospital {
    private String name;
    private double time;

    public Hospital(String name, double time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public double getTime(){
        return time;
    }

}
