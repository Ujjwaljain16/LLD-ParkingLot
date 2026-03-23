public class SimpleTicketIdGenerator implements TicketIdGenerator {
    private int counter = 1;

    @Override
    public String nextId() {
        return "T" + counter++;
    }
}
