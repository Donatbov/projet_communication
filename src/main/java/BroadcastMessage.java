public class BroadcastMessage extends Message {
    private final int sender;

    public BroadcastMessage(String payload, int estampille, int sender) {
        super(payload, estampille);
        this.sender = sender;
    }

    public int getSender() {
        return sender;
    }
}
