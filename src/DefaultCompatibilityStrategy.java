import java.util.ArrayList;
import java.util.List;

public class DefaultCompatibilityStrategy implements CompatibilityStrategy {

    @Override
    public boolean canParkInSlotType(VehicleType vehicleType, SlotType slotType) {
        if (vehicleType == VehicleType.BIKE) {
            return true;
        }
        if (vehicleType == VehicleType.CAR) {
            return slotType == SlotType.MEDIUM || slotType == SlotType.LARGE;
        }
        return slotType == SlotType.LARGE;
    }

    @Override
    public List<SlotType> getCompatibleSlotOrder(VehicleType vehicleType, SlotType requestedSlotType) {
        List<SlotType> order = new ArrayList<>();

        if (vehicleType == VehicleType.BIKE) {
            order.add(SlotType.SMALL);
            order.add(SlotType.MEDIUM);
            order.add(SlotType.LARGE);
        } else if (vehicleType == VehicleType.CAR) {
            order.add(SlotType.MEDIUM);
            order.add(SlotType.LARGE);
        } else {
            order.add(SlotType.LARGE);
        }

        if (requestedSlotType != null) {
            order.remove(requestedSlotType);
            order.add(0, requestedSlotType);
        }

        return order;
    }
}
