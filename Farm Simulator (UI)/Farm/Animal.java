package Farm;

public class Animal {
    private final int age;
    private int weight;
    private final String owner;
    private final Specie specie;
    private boolean isAlive;
    private MeatType meatType;

    public Animal(int age, int weight, String owner, Specie specie) {
        this.age = age;
        this.weight = weight;
        this.owner = owner;
        this.specie = specie;
        this.isAlive = true;
        this.meatType = null;
    }

    public int getAge() { return age; }
    public int getWeight() { return weight; }
    public String getOwner() { return owner; }
    public Specie getSpecie() { return specie; }
    public boolean isAlive() { return isAlive; }
    public MeatType getMeatType() { return meatType; }

    public void eatFood(int kg) {
        if (isAlive) weight += kg;
        else System.out.println("Dead animals can't eat!");
    }

    public void kill(Butcher butcher) {
        if (butcher.isAllowedButcher() && isAlive) {
            isAlive = false;
            meatType = MeatType.STEAK;
        }
    }

    public void processMeat(Butcher butcher) {
        if (butcher.isAllowedButcher() && !isAlive && meatType != MeatType.MINCED) {
            meatType = MeatType.downgrade(meatType);
        }
    }
}
