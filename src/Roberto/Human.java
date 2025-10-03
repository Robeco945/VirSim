package Roberto;

import simu.model.Customer;

import java.util.Random;

public class Human extends Customer{
    private boolean illness;
    private int severity; //scale 1 to 10
    private boolean vaccine;
    private boolean forCheckup;
    private boolean dead;

    public Human(){
        super();
        Random rand = new Random();
        this.illness = rand.nextDouble() < 0.7;
        if (illness) this.severity = rand.nextInt(10) + 1;
        if (severity>=7){this.forCheckup = false;}
        else this.forCheckup = true;
        this.vaccine = false;
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

    public void setReason(boolean forCheckup){this.forCheckup = forCheckup;
    }
    public boolean getReason() {return forCheckup;
    }
    public void setDead(boolean dead){
        this.dead = dead;
    }

    public boolean isDead(){
        return dead;
    }

    @Override
    public String toString() {
        return "Human information: id=" + getId() +
                ", illness=" + illness +
                ", severity=" + severity +
                ", vaccine=" + vaccine +
                ", forCheckup=" + forCheckup +
                ", dead=" + dead
                ;
    }

}