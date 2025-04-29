package Farm;

import java.util.ArrayList;
import java.util.List;

// The Farm class manages a list of animals and the butcher.
public class Farm {

    
    private final Butcher butcher;      // The butcher responsible for processing animals
    private final List<Animal> animals; // The list of animals on the farm

    // Constructor that initializes the butcher and animal list
    public Farm() {
        this.butcher = new Butcher();
        this.animals = new ArrayList<>();
    }

    // Adds an animal to the farm
    public void addAnimal(int age, int weight, String owner, Specie specie) {
        Animal newAnimal = new Animal(age, weight, owner, specie);
        animals.add(newAnimal);
    }

    // Returns the list of animals on the farm
    public List<Animal> getAnimals() {
        return animals;
    }

    // Returns the butcher instance of the farm
    public Butcher getButcher() {
        return butcher;
    }

    // Displays information of all animals currently on the farm
    public void showFarm() {
        StringBuilder sb = new StringBuilder();
        sb.append("Farm Overview:\n");

        // Displaying the animal's details in a readable format
        for (Animal animal : animals) {
            sb.append(animal.toString());
            sb.append("\n----------------------\n");
        }

        System.out.println(sb.toString());
    }

    // Kills an animal and processes its meat if conditions are met
    public void processAnimal(Animal animal) {
        if (animal.isAlive()) {
            System.out.println("Cannot process a living animal.");
        } else {
            butcher.processMeat(animal);
        }
    }

    // Feed an animal with specified kilograms of food
    public void feedAnimal(Animal animal, int kg) {
        if (animal.isAlive()) {
            animal.eatFood(kg);
            System.out.println("Animal fed " + kg + "kg of food.");
        } else {
            System.out.println("Dead animals can't be fed.");
        }
    }
    
}
