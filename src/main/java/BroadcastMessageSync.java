public class BroadcastMessageSync extends Message {
    private final int sender;

    public BroadcastMessageSync(String payload, int estampille, int sender) {
        super(payload, estampille);
        this.sender = sender;
    }

    public int getSender() {
        return sender;
    }
}
