package fr.juliengrade.parcelflow;

import java.time.Year;
import java.util.UUID;

/**
 * Version initiale fonctionnelle, volontairement difficile à faire évoluer.
 * Cette classe constitue le point de départ du refactoring demandé.
 */
public final class LegacyShippingService {
    private static final System.Logger LOGGER =
            System.getLogger(LegacyShippingService.class.getName());

    private long sequence;

    public Shipment createShipment(ShipmentRequest request) {
        String trackingNumber = "PF-%d-%06d".formatted(
                Year.now().getValue(),
                ++sequence
        );

        String transportDescription;
        switch (request.shipmentType()) {
            case STANDARD -> transportDescription = "Expédition standard — livraison sous 3 à 5 jours";
            case EXPRESS -> transportDescription = "Expédition express — livraison sous 24 heures";
            case REFRIGERATED -> transportDescription = "Expédition réfrigérée — chaîne du froid contrôlée";
            default -> throw new IllegalArgumentException("Type d'expédition inconnu");
        }

        double price = calculateBasePrice(request);
        StringBuilder fullDescription = new StringBuilder(transportDescription);

        if (request.insured()) {
            price += 5.00;
            fullDescription.append(" + assurance");
        }
        if (request.signatureRequired()) {
            price += 2.50;
            fullDescription.append(" + signature à la réception");
        }
        if (request.fragile()) {
            price += 4.00;
            fullDescription.append(" + traitement fragile");
        }

        Shipment shipment = new Shipment(
                UUID.randomUUID(),
                trackingNumber,
                request.recipientEmail(),
                request.shipmentType(),
                price,
                fullDescription.toString()
        );

        notifyCustomer(shipment, "Expédition créée");
        audit(shipment, "CREATION");
        return shipment;
    }

    public void updateStatus(Shipment shipment, ShipmentStatus newStatus) {
        ShipmentStatus previousStatus = shipment.status();
        shipment.changeStatus(newStatus);
        notifyCustomer(shipment, "Nouveau statut : " + newStatus);
        audit(shipment, previousStatus + " -> " + newStatus);
    }

    private double calculateBasePrice(ShipmentRequest request) {
        double fixedPrice = switch (request.shipmentType()) {
            case STANDARD -> 8.00;
            case EXPRESS -> 18.00;
            case REFRIGERATED -> 28.00;
        };
        double weightPrice = request.weightKg() * 1.20;
        return (fixedPrice + weightPrice) * request.destinationZone().multiplier();
    }

    private void notifyCustomer(Shipment shipment, String message) {
        LOGGER.log(
                System.Logger.Level.INFO,
                "[NOTIFICATION] {0} | {1} | {2}",
                shipment.recipientEmail(),
                shipment.trackingNumber(),
                message
        );
    }

    private void audit(Shipment shipment, String action) {
        LOGGER.log(
                System.Logger.Level.INFO,
                "[AUDIT] {0} | {1}",
                shipment.trackingNumber(),
                action
        );
    }
}

