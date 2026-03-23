public class ParkingSlot {
    private final String slotNumber;
    private final SlotType slotType;
    private final int floorNumber;
    private boolean occupied;
    private Vehicle parkedVehicle;

    public ParkingSlot(String slotNumber, SlotType slotType, int floorNumber) {
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.floorNumber = floorNumber;
        this.occupied = false;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public void parkVehicle(Vehicle vehicle) {
        if (occupied) {
            throw new IllegalStateException("Slot is already occupied: " + slotNumber);
        }
        parkedVehicle = vehicle;
        occupied = true;
    }

    public void removeVehicle() {
        parkedVehicle = null;
        occupied = false;
    }
}
