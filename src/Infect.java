class Infect{
    public static void infect(Human human){
        human.setIllness(true);
    }

    public static void cure(Human human){
        human.setIllness(false);
    }
    public static void giveVaccine(Human human){
        human.setVaccine(true);
    }

}