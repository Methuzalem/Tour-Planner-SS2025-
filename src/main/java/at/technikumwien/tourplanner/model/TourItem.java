package at.technikumwien.tourplanner.model;

public record TourItem(String name, boolean done) {
    @Override
    public String toString() {
        return name;
    }

    // parameterized ctor is implicitly existing
    // no default ctor in records!

    // getters in records are implicitly existing
    // public String name()
    // public boolean done()

    // no setters in records!
}
