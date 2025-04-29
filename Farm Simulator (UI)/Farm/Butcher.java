package Farm;

public class Butcher {
    private static int increment = 100000;
    private final long butcherID;
    private final boolean allowedButcher;

    public Butcher() {
        butcherID = increment++;
        allowedButcher = (butcherID == 100000);
    }

    public boolean isAllowedButcher() { return allowedButcher; }

    public void processMeat(Animal animal) {
        if (!allowedButcher) {
            System.out.println("Fake butcher can't process meat!");
            return;
        }
        if (animal.isAlive()) {
            System.out.println("Can't process alive animal!");
        } else if (animal.getMeatType() == MeatType.MINCED) {
            System.out.println("Minced meat can't be processed further!");
        } else {
            animal.processMeat(this);
        }
    }
}