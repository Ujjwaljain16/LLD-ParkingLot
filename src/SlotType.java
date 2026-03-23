public enum SlotType {
    SMALL(20),
    MEDIUM(40),
    LARGE(80);

    private final double hourlyRate;

    SlotType(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }
}
