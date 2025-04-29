package Farm;

public enum MeatType {
    STEAK, CUBED, MINCED;

    public static MeatType downgrade(MeatType current) {
        return switch (current) {
            case STEAK -> CUBED;
            case CUBED -> MINCED;
            default -> throw new IllegalStateException("Minced meat can't be processed further!");
        };
    }
}
