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
        this.meatType = null; // No meat type at the beginning, it is alive.
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public String getOwner() {
        return owner;
    }

    public Specie getSpecie() {
        return specie;
    }

    public void setMeatType(MeatType meatType, Butcher butcher) {
        if (butcher.isAllowedButcher()) {
            this.meatType = meatType;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public MeatType getMeatType() {
        return meatType;
    }

    public void eatFood(int kg) {
        if (isAlive) {
            weight += kg;
        } else {
            System.out.println("Dead animals can't eat!");
        }
    }

    public void kill(Butcher butcher) {
        if (butcher.isAllowedButcher()) {
            if (!isAlive) {
                System.out.println("The animal is already dead!");
            } else {
                isAlive = false;
                meatType = MeatType.STEAK; // First, it becomes in steak format.
            }
        }

    }
    
    @Override
    public String toString() {
        String state = isAlive ? "Alive" : "Dead";
        String meat = meatType == null ? "It is alive!" : meatType.toString();
        String specieName = specie.getSpecie();
        
        StringBuilder sb = new StringBuilder();
        sb.append("┌───────────────────────────────┐\n");
        sb.append(String.format("│ %-30s│\n", specieName + " Information"));
        sb.append("├───────────────────────────────┤\n");
        sb.append(String.format("│ Age:       %-18s │\n", age));
        sb.append(String.format("│ Weight:    %-18s │\n", weight));
        sb.append(String.format("│ Owner:     %-18s │\n", owner));
        sb.append(String.format("│ Specie:    %-18s │\n", specieName));
        sb.append(String.format("│ State:     %-18s │\n", state));
        sb.append(String.format("│ Meat Type: %-18s │\n", meat));
        sb.append("└───────────────────────────────┘\n");

        return sb.toString();
    }

}
