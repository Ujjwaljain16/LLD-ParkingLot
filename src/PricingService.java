import java.time.LocalDateTime;

public interface PricingService {
    double calculateBill(LocalDateTime entryTime, LocalDateTime exitTime, SlotType slotType);
}
