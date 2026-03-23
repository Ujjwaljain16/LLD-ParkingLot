import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private final String name;
    private final List<ParkingFloor> floors;
    private final Map<String, Gate> gates;
    private final Map<String, ParkingTicket> activeTickets;
    private final PricingService pricingService;
    private final CompatibilityStrategy compatibilityStrategy;
    private final SlotAssignmentStrategy slotAssignmentStrategy;
    private final TicketIdGenerator ticketIdGenerator;

    public ParkingLot(String name) {
        this(
                name,
                new BillingService(),
                new DefaultCompatibilityStrategy(),
                new NearestSlotAssignmentStrategy(),
                new SimpleTicketIdGenerator()
        );
    }

    public ParkingLot(
            String name,
            PricingService pricingService,
            CompatibilityStrategy compatibilityStrategy,
            SlotAssignmentStrategy slotAssignmentStrategy,
            TicketIdGenerator ticketIdGenerator
    ) {
        this.name = name;
        this.floors = new ArrayList<>();
        this.gates = new LinkedHashMap<>();
        this.activeTickets = new LinkedHashMap<>();
        this.pricingService = pricingService;
        this.compatibilityStrategy = compatibilityStrategy;
        this.slotAssignmentStrategy = slotAssignmentStrategy;
        this.ticketIdGenerator = ticketIdGenerator;
    }

    public String getName() {
        return name;
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public void addGate(Gate gate) {
        gates.put(gate.getGateId(), gate);
    }

    public ParkingTicket park(Vehicle vehicle, LocalDateTime entryTime, SlotType requestedSlotType, String entryGateId) {
        Gate gate = gates.get(entryGateId);
        if (gate == null) {
            throw new IllegalArgumentException("Invalid gate id: " + entryGateId);
        }

        if (requestedSlotType != null
                && !compatibilityStrategy.canParkInSlotType(vehicle.getVehicleType(), requestedSlotType)) {
            throw new IllegalArgumentException(
                    "Vehicle type " + vehicle.getVehicleType() + " cannot park in " + requestedSlotType + " slot"
            );
        }

        ParkingSlot slot = slotAssignmentStrategy.findSlot(
                vehicle,
                requestedSlotType,
                gate,
                floors,
                compatibilityStrategy
        );
        if (slot == null) {
            throw new IllegalStateException("No compatible slot available for this vehicle");
        }

        slot.parkVehicle(vehicle);

        String ticketId = ticketIdGenerator.nextId();
        ParkingTicket ticket = new ParkingTicket(
                ticketId,
                vehicle,
                slot.getSlotNumber(),
                slot.getSlotType(),
                entryTime,
                entryGateId
        );
        activeTickets.put(ticketId, ticket);
        return ticket;
    }

    public Map<SlotType, Integer> status() {
        Map<SlotType, Integer> freeCounts = new EnumMap<>(SlotType.class);
        for (SlotType slotType : SlotType.values()) {
            freeCounts.put(slotType, 0);
        }

        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.getSlots()) {
                if (!slot.isOccupied()) {
                    freeCounts.put(slot.getSlotType(), freeCounts.get(slot.getSlotType()) + 1);
                }
            }
        }
        return freeCounts;
    }

    public double exit(ParkingTicket parkingTicket, LocalDateTime exitTime) {
        ParkingTicket activeTicket = activeTickets.get(parkingTicket.getTicketId());
        if (activeTicket == null) {
            throw new IllegalArgumentException("Ticket is invalid or already used: " + parkingTicket.getTicketId());
        }

        ParkingSlot slot = findSlotByNumber(activeTicket.getSlotNumber());
        if (slot == null) {
            throw new IllegalStateException("Slot not found for ticket: " + activeTicket.getSlotNumber());
        }

        double amount = pricingService.calculateBill(
                activeTicket.getEntryTime(),
                exitTime,
                activeTicket.getSlotType()
        );

        slot.removeVehicle();
        activeTickets.remove(activeTicket.getTicketId());
        return amount;
    }

    private ParkingSlot findSlotByNumber(String slotNumber) {
        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.getSlots()) {
                if (slot.getSlotNumber().equals(slotNumber)) {
                    return slot;
                }
            }
        }
        return null;
    }
}
