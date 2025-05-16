package at.technikumwien.tourplanner.model;

public record TourItem(String name) {
    @Override
    public String toString() {
        return name;
    }


}
