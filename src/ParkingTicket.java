import java.time.LocalDateTime;

public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final String slotNumber;
    private final SlotType slotType;
    private final LocalDateTime entryTime;
    private final String entryGateId;

    public ParkingTicket(
            String ticketId,
            Vehicle vehicle,
            String slotNumber,
            SlotType slotType,
            LocalDateTime entryTime,
            String entryGateId
    ) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.entryTime = entryTime;
        this.entryGateId = entryGateId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public String getEntryGateId() {
        return entryGateId;
    }

    @Override
    public String toString() {
        return "Ticket=" + ticketId
                + ", Vehicle=" + vehicle
                + ", Slot=" + slotNumber
                + ", SlotType=" + slotType
                + ", EntryTime=" + entryTime
                + ", Gate=" + entryGateId;
    }
}
