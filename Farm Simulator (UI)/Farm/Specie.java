package Farm;

public enum Specie {
    SHEEP("Sheep"), COW("Cow"), CALF("Calf");

    private final String specieName;

    Specie(String specie) {
        this.specieName = specie;
    }

    public String getSpecie() { return specieName; }
}
