public class Gate {
    private final String gateId;
    private final int floorNumber;

    public Gate(String gateId, int floorNumber) {
        this.gateId = gateId;
        this.floorNumber = floorNumber;
    }

    public String getGateId() {
        return gateId;
    }

    public int getFloorNumber() {
        return floorNumber;
    }
}
