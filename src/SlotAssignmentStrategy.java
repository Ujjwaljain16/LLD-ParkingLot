import java.util.List;

public interface SlotAssignmentStrategy {
    ParkingSlot findSlot(
            Vehicle vehicle,
            SlotType requestedSlotType,
            Gate entryGate,
            List<ParkingFloor> floors,
            CompatibilityStrategy compatibilityStrategy
    );
}
