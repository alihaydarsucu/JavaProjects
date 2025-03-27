package Farm;

public class Farm {

    public final Butcher butcher;
    public Animal animal;
    private Animal[] animals = new Animal[1000];
    private int animalCount = 0;

    Farm(Butcher butcher, Animal animal) {
        this.butcher = butcher;
        this.animal = animal;
        animals[0] = animal;
        animalCount++;
    }

    static public Farm openFarm() {
        Butcher butcher = new Butcher();
        Animal sheep = new Animal(5, 300, "Ali & Salih", Specie.SHEEP);

        return new Farm(butcher, sheep);
    }

    static public Farm openFarm(Animal animal) {
        Butcher butcher = new Butcher();

        return new Farm(butcher, animal);
    }

    public Animal[] getAnimals() {
        return animals;
    }
    
    

    public void addAnimal(int age, int weight, String owner, Specie specie) {
        animal = new Animal(age, weight, owner, specie);
        animals[animalCount++] = animal;
    }

    public void showFarm() {
        String farmName = "The Farm";
        StringBuilder sb = new StringBuilder();
        sb.append("┌───────────────────────────────────────────────────────────────┐\n");
        sb.append(String.format("│ %-61s │\n", farmName));
        sb.append("├─────┬────────┬───────────────┬──────────┬──────────┬──────────┤\n");
        sb.append(String.format("│ %-3s │ %-5s │ %-13s │ %-8s │ %-8s │ %-8s │\n", "Age", "Weight", "Owner", "Specie", "State", "Meat"));
        sb.append("├─────┼────────┼───────────────┼──────────┼──────────┼──────────┤\n");
        for (int i = 0; i < animalCount; i++) {
            String meat = animals[i].getMeatType() == null ? "----" : animals[i].getMeatType().toString();
            String state = animals[i].isAlive() ? "Alive" : "Dead";
            String specieName = animals[i].getSpecie().getSpecie();
            sb.append(String.format("│ %-3d │ %-5d  │ %-13s │ %-8s │ %-8s │ %-8s │\n",
                    animals[i].getAge(),
                    animals[i].getWeight(),
                    animals[i].getOwner(),
                    specieName,
                    state,
                    meat));
        }
        sb.append("└─────┴────────┴───────────────┴──────────┴──────────┴──────────┘\n");
        System.out.print(sb.toString());
    }

    

}
