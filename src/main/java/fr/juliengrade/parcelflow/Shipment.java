package fr.juliengrade.parcelflow;

import java.util.Objects;
import java.util.UUID;

public final class Shipment {
    private final UUID id;
    private final String trackingNumber;
    private final String recipientEmail;
    private final ShipmentType type;
    private final double price;
    private final String description;
    private ShipmentStatus status;

    public Shipment(UUID id,
                    String trackingNumber,
                    String recipientEmail,
                    ShipmentType type,
                    double price,
                    String description) {
        this.id = Objects.requireNonNull(id, "id");
        this.trackingNumber = Objects.requireNonNull(trackingNumber, "trackingNumber");
        this.recipientEmail = Objects.requireNonNull(recipientEmail, "recipientEmail");
        this.type = Objects.requireNonNull(type, "type");
        this.price = price;
        this.description = Objects.requireNonNull(description, "description");
        this.status = ShipmentStatus.CREATED;
    }

    public void changeStatus(ShipmentStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus, "newStatus");
    }

    public UUID id() {
        return id;
    }

    public String trackingNumber() {
        return trackingNumber;
    }

    public String recipientEmail() {
        return recipientEmail;
    }

    public ShipmentType type() {
        return type;
    }

    public double price() {
        return price;
    }

    public String description() {
        return description;
    }

    public ShipmentStatus status() {
        return status;
    }
}

