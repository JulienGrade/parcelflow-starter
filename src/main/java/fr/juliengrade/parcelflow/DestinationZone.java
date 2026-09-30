package fr.juliengrade.parcelflow;

public enum DestinationZone {
    LOCAL(1.0),
    NATIONAL(1.25),
    EUROPEAN(1.65);

    private final double multiplier;

    DestinationZone(double multiplier) {
        this.multiplier = multiplier;
    }

    public double multiplier() {
        return multiplier;
    }
}

