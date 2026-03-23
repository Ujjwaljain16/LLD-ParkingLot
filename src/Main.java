import java.time.LocalDateTime;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ParkingLot parkingLot = buildParkingLot();

        Vehicle bike = new Vehicle("KA-01-BK-1001", VehicleType.BIKE);
        Vehicle car = new Vehicle("KA-01-CR-2002", VehicleType.CAR);
        Vehicle bus = new Vehicle("KA-01-BS-3003", VehicleType.BUS);

        LocalDateTime now = LocalDateTime.now();

        ParkingTicket bikeTicket = parkingLot.park(bike, now.minusMinutes(95), SlotType.SMALL, "G1");
        ParkingTicket carTicket = parkingLot.park(car, now.minusMinutes(140), SlotType.MEDIUM, "G2");
        ParkingTicket busTicket = parkingLot.park(bus, now.minusMinutes(190), SlotType.LARGE, "G1");

        System.out.println("--- Tickets Generated ---");
        System.out.println(bikeTicket);
        System.out.println(carTicket);
        System.out.println(busTicket);

        System.out.println("\n--- Availability After Parking ---");
        printStatus(parkingLot.status());

        double bikeBill = parkingLot.exit(bikeTicket, now.plusMinutes(10));
        double busBill = parkingLot.exit(busTicket, now.plusMinutes(20));

        System.out.println("\n--- Exit Bills ---");
        System.out.println("Bike bill: " + bikeBill);
        System.out.println("Bus bill: " + busBill);

        System.out.println("\n--- Availability After Exit ---");
        printStatus(parkingLot.status());
    }

    private static ParkingLot buildParkingLot() {
        ParkingLot parkingLot = new ParkingLot("City Mall Parking");

        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSlot(new ParkingSlot("F1-S1", SlotType.SMALL, 1));
        floor1.addSlot(new ParkingSlot("F1-S2", SlotType.SMALL, 1));
        floor1.addSlot(new ParkingSlot("F1-M1", SlotType.MEDIUM, 1));
        floor1.addSlot(new ParkingSlot("F1-L1", SlotType.LARGE, 1));

        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addSlot(new ParkingSlot("F2-S1", SlotType.SMALL, 2));
        floor2.addSlot(new ParkingSlot("F2-M1", SlotType.MEDIUM, 2));
        floor2.addSlot(new ParkingSlot("F2-M2", SlotType.MEDIUM, 2));
        floor2.addSlot(new ParkingSlot("F2-L1", SlotType.LARGE, 2));

        parkingLot.addFloor(floor1);
        parkingLot.addFloor(floor2);

        parkingLot.addGate(new Gate("G1", 1));
        parkingLot.addGate(new Gate("G2", 2));

        return parkingLot;
    }

    private static void printStatus(Map<SlotType, Integer> status) {
        for (SlotType slotType : SlotType.values()) {
            System.out.println(slotType + " -> " + status.get(slotType) + " slots free");
        }
    }
}
