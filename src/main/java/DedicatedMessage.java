public class DedicatedMessage extends Message {
    private final String recipient;

    public DedicatedMessage(String payload, int estampille, String recipient) {
        super(payload, estampille);
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }
}

