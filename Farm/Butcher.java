package Farm;

public class Butcher {

    private static int increment = 100000;
    final private long butcherID;
     private boolean allowedButcher = false;

    public Butcher() {
        butcherID = increment++;
        if (butcherID == 100000) {
            allowedButcher = true;
        }
    }

    public boolean isAllowedButcher() {
        return allowedButcher;
    }
    

    public void processMeat(Animal animal) {
        if (this.isAllowedButcher()) {
            if (animal.isAlive()) {
                System.out.println("Can't process alive animal");
            } else if (animal.getMeatType() == MeatType.MINCED) {
                System.out.println("Minced meat can't get processed any further!");
            } else {
                animal.setMeatType(MeatType.downgrade(animal.getMeatType()), this);
            }
        } else {
            System.out.println("\nFake butcher can't process meat.\n");
        }

    }

}
