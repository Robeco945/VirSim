public class Main{
    public static void main(String[] arg){
        Human Adam = new Human(1, true, 7, false);
        System.out.println("Adam's vaccination status: "+Adam.getVaccine());
        Infect.giveVaccine(Adam);
        System.out.println("Adam's vaccination status now: "+Adam.getVaccine());
        System.out.println("Adam's illness status : "+Adam.getIllness());
        }
}