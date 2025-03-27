package Farm;
public class ButcherShop {
    public static void main(String[] args) {
        
        System.out.println("========= OPENING FARM =========");
        Farm myFarm = Farm.openFarm(new Animal(2, 150, "Mehmet", Specie.SHEEP));
        myFarm.showFarm();

       
        System.out.println("\n========= ADDING ANIMALS =========");
        myFarm.addAnimal(3, 200, "Ahmet", Specie.COW);
        myFarm.addAnimal(4, 250, "Ayşe", Specie.CALF);
        myFarm.showFarm();

        
        System.out.println("\n========= REAL BUTCHER TEST =========");
        butcherTest(myFarm.butcher, myFarm.getAnimals()); 
        myFarm.showFarm();

        
        System.out.println("\n========= FAKE BUTCHER TEST =========");
        fakeButcherTest(myFarm.getAnimals()[1]); 
        
        
        
        
        
        myFarm.showFarm();
        
        System.out.println("\n========= MEAT PROCESSING TEST =========");
        myFarm.butcher.processMeat(myFarm.getAnimals()[0]); 
        myFarm.showFarm();
        
        System.out.println("\n========= MEAT PROCESSING TEST (Fake Buthcer) =========");
        Butcher fakeButcher1 = new Butcher();
        fakeButcher1.processMeat(myFarm.getAnimals()[0]);
        myFarm.showFarm();
        
    }

    private static void butcherTest(Butcher butcher, Animal[] animals) {
        System.out.println("Before Butcher Tries to Kill:");
        System.out.println(animals[0]);

        animals[0].kill(butcher);

        System.out.println("\nAfter Butcher Tries to Kill:");
        System.out.println(animals[0]);
        System.out.println("================================\n");
    }

    private static void fakeButcherTest(Animal animal) {
        Butcher fakeButcher = new Butcher();

        System.out.println("Before Fake Butcher Tries to Kill:");
        System.out.println(animal);

        animal.kill(fakeButcher);
        

        System.out.println("\nAfter Fake Butcher Tries to Kill:");
        System.out.println(animal);
        
        System.out.println("================================\n");
    }
}
