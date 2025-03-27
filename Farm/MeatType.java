package Farm;
public enum MeatType {
    STEAK, CUBED, MINCED ;
    
    public static MeatType downgrade( MeatType current) {
        return switch (current) {
            case STEAK -> CUBED;
            case CUBED -> MINCED ;
            default -> throw new IllegalStateException("Minced meat can not be further processed!");
        };
    }

    @Override
    public String toString() {
        return name();
    }
    
    
}
