class Human{
    private int id;
    private boolean illness;
    private int severity; //scale 1 to 10
    private boolean vaccine;

    public Human(int id, boolean illness, int severity, boolean vaccine){
        this.id = id;
        this. illness = illness;
        this.severity = severity;
        this.vaccine = vaccine;
    }
    public void setID(int id){
        this.id = id;
    }
    public int getID(){
        return id;
    }
    public void setIllness(boolean illness){
        this.illness = illness;
    }
    public boolean getIllness(){
        return illness;
    }
    public void setSeverity(int severity){
        this.severity = severity;
    }
    public int getSeverity(){
        return severity;
    }
    public void setVaccine(boolean vaccine){
        this.vaccine = vaccine;
    }
    public boolean getVaccine(){
        return vaccine;
    }
}