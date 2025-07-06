package at.technikumwien.tourplanner.utils;

public class RatingOption {
    private final String label;
    private final int value;

    public RatingOption(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() { return label; }
    public int getValue() { return value; }

    @Override
    public String toString() {
        return label;
    }

    public static RatingOption fromValue(int value) {
        return switch (value) {
            case 5 -> new RatingOption("Sehr Gut", 5);
            case 4 -> new RatingOption("Gut", 4);
            case 3 -> new RatingOption("Mittel", 3);
            case 2 -> new RatingOption("Schlecht", 2);
            case 1 -> new RatingOption("Sehr Schlecht", 1);
            default -> new RatingOption("Unbekannt", 0);
        };
    }
}

