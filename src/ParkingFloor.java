import java.util.ArrayList;
import java.util.List;

public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSlot> slots;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.slots = new ArrayList<>();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSlot> getSlots() {
        return slots;
    }

    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }

    public ParkingSlot findFirstAvailableByType(SlotType slotType) {
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied() && slot.getSlotType() == slotType) {
                return slot;
            }
        }
        return null;
    }
}
