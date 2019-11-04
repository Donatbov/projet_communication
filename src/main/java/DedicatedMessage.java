public class DedicatedMessage extends Message {
    private final int recipient;

    public DedicatedMessage(String payload, int estampille, int recipient) {
        super(payload, estampille);
        this.recipient = recipient;
    }

    public int getRecipient() {
        return recipient;
    }
}

