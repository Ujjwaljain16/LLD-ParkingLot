import java.util.List;

public interface CompatibilityStrategy {
    boolean canParkInSlotType(VehicleType vehicleType, SlotType slotType);

    List<SlotType> getCompatibleSlotOrder(VehicleType vehicleType, SlotType requestedSlotType);
}
