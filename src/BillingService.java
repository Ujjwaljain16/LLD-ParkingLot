import java.time.Duration;
import java.time.LocalDateTime;

public class BillingService implements PricingService {

    @Override
    public double calculateBill(LocalDateTime entryTime, LocalDateTime exitTime, SlotType slotType) {
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }

        long totalMinutes = Duration.between(entryTime, exitTime).toMinutes();
        long billableHours = (long) Math.ceil(totalMinutes / 60.0);
        if (billableHours == 0) {
            billableHours = 1;
        }

        return billableHours * slotType.getHourlyRate();
    }
}
