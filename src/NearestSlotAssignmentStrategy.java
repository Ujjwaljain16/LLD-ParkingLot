import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NearestSlotAssignmentStrategy implements SlotAssignmentStrategy {

    @Override
    public ParkingSlot findSlot(
            Vehicle vehicle,
            SlotType requestedSlotType,
            Gate entryGate,
            List<ParkingFloor> floors,
            CompatibilityStrategy compatibilityStrategy
    ) {
        List<SlotType> order = compatibilityStrategy.getCompatibleSlotOrder(
                vehicle.getVehicleType(),
                requestedSlotType
        );

        List<ParkingFloor> orderedFloors = new ArrayList<>(floors);
        orderedFloors.sort(Comparator.comparingInt(floor ->
                Math.abs(floor.getFloorNumber() - entryGate.getFloorNumber())));

        for (ParkingFloor floor : orderedFloors) {
            for (SlotType slotType : order) {
                ParkingSlot slot = floor.findFirstAvailableByType(slotType);
                if (slot != null) {
                    return slot;
                }
            }
        }

        return null;
    }
}
