package Farm;

public class Specie {

    public static final Specie SHEEP = new Specie("Sheep");
    public static final Specie CALF = new Specie("Calf");
    public static final Specie COW = new Specie("Cow");

    private final String specie;

    private Specie(String specie) {
        this.specie = specie;
    }

    public String getSpecie() {
        return specie;
    }
    
}
