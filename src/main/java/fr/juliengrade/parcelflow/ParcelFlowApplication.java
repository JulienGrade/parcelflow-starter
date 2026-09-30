package fr.juliengrade.parcelflow;

import java.util.UUID;

public final class ParcelFlowApplication {
    private static final System.Logger LOGGER =
            System.getLogger(ParcelFlowApplication.class.getName());

    private ParcelFlowApplication() {
    }

    public static void main(String[] args) {
        LegacyShippingService agencyService = new LegacyShippingService();
        LegacyShippingService websiteService = new LegacyShippingService();

        Shipment expressShipment = agencyService.createShipment(new ShipmentRequest(
                UUID.randomUUID(),
                "client.agence@example.com",
                3.5,
                DestinationZone.NATIONAL,
                ShipmentType.EXPRESS,
                true,
                true,
                false
        ));

        Shipment refrigeratedShipment = websiteService.createShipment(new ShipmentRequest(
                UUID.randomUUID(),
                "client.web@example.com",
                6.0,
                DestinationZone.EUROPEAN,
                ShipmentType.REFRIGERATED,
                true,
                false,
                true
        ));

        display(expressShipment);
        display(refrigeratedShipment);

        agencyService.updateStatus(expressShipment, ShipmentStatus.PREPARED);
        agencyService.updateStatus(expressShipment, ShipmentStatus.IN_TRANSIT);
        websiteService.updateStatus(refrigeratedShipment, ShipmentStatus.PREPARED);
    }

    private static void display(Shipment shipment) {
        LOGGER.log(
                System.Logger.Level.INFO,
                "{0} | {1} | {2,number,#0.00} EUR",
                shipment.trackingNumber(),
                shipment.description(),
                shipment.price()
        );
    }
}

