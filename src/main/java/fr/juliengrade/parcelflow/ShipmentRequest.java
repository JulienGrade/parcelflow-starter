package fr.juliengrade.parcelflow;

import java.util.Objects;
import java.util.UUID;

public record ShipmentRequest(
        UUID customerId,
        String recipientEmail,
        double weightKg,
        DestinationZone destinationZone,
        ShipmentType shipmentType,
        boolean insured,
        boolean signatureRequired,
        boolean fragile
) {
    public ShipmentRequest {
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(recipientEmail, "recipientEmail");
        Objects.requireNonNull(destinationZone, "destinationZone");
        Objects.requireNonNull(shipmentType, "shipmentType");
        if (recipientEmail.isBlank()) {
            throw new IllegalArgumentException("L'adresse électronique est obligatoire");
        }
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Le poids doit être strictement positif");
        }
    }
}

