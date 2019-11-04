public class BroadcastMessage extends Message {
    private final String sender;

    public BroadcastMessage(String payload, int estampille, String sender) {
        super(payload, estampille);
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
}
