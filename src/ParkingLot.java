import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private final String name;
    private final List<ParkingFloor> floors;
    private final Map<String, Gate> gates;
    private final Map<String, ParkingTicket> activeTickets;
    private final BillingService billingService;
    private int ticketCounter;

    public ParkingLot(String name) {
        this.name = name;
        this.floors = new ArrayList<>();
        this.gates = new LinkedHashMap<>();
        this.activeTickets = new LinkedHashMap<>();
        this.billingService = new BillingService();
        this.ticketCounter = 1;
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

        if (requestedSlotType != null && !isCompatible(vehicle.getVehicleType(), requestedSlotType)) {
            throw new IllegalArgumentException(
                    "Vehicle type " + vehicle.getVehicleType() + " cannot park in " + requestedSlotType + " slot"
            );
        }

        List<SlotType> preferredOrder = getCompatibleSlotTypes(vehicle.getVehicleType());
        if (requestedSlotType != null) {
            preferredOrder.remove(requestedSlotType);
            preferredOrder.add(0, requestedSlotType);
        }

        ParkingSlot slot = findNearestAvailableSlot(gate, preferredOrder);
        if (slot == null) {
            throw new IllegalStateException("No compatible slot available for this vehicle");
        }

        slot.parkVehicle(vehicle);

        String ticketId = "T" + ticketCounter++;
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

        double amount = billingService.calculateBill(
                activeTicket.getEntryTime(),
                exitTime,
                activeTicket.getSlotType()
        );

        slot.removeVehicle();
        activeTickets.remove(activeTicket.getTicketId());
        return amount;
    }

    private ParkingSlot findNearestAvailableSlot(Gate gate, List<SlotType> slotPreferenceOrder) {
        List<ParkingFloor> orderedFloors = new ArrayList<>(floors);
        orderedFloors.sort(Comparator.comparingInt(floor ->
                Math.abs(floor.getFloorNumber() - gate.getFloorNumber())));

        for (ParkingFloor floor : orderedFloors) {
            for (SlotType slotType : slotPreferenceOrder) {
                ParkingSlot slot = floor.findFirstAvailableByType(slotType);
                if (slot != null) {
                    return slot;
                }
            }
        }
        return null;
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

    private boolean isCompatible(VehicleType vehicleType, SlotType slotType) {
        if (vehicleType == VehicleType.BIKE) {
            return true;
        }
        if (vehicleType == VehicleType.CAR) {
            return slotType == SlotType.MEDIUM || slotType == SlotType.LARGE;
        }
        return slotType == SlotType.LARGE;
    }

    private List<SlotType> getCompatibleSlotTypes(VehicleType vehicleType) {
        List<SlotType> compatibleTypes = new ArrayList<>();
        if (vehicleType == VehicleType.BIKE) {
            compatibleTypes.add(SlotType.SMALL);
            compatibleTypes.add(SlotType.MEDIUM);
            compatibleTypes.add(SlotType.LARGE);
            return compatibleTypes;
        }
        if (vehicleType == VehicleType.CAR) {
            compatibleTypes.add(SlotType.MEDIUM);
            compatibleTypes.add(SlotType.LARGE);
            return compatibleTypes;
        }
        compatibleTypes.add(SlotType.LARGE);
        return compatibleTypes;
    }
}
