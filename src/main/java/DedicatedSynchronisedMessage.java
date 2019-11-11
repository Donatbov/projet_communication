public class DedicatedSynchronisedMessage extends Message {
    private final int recipient;
    private final int sender;
    private boolean received;

    public DedicatedSynchronisedMessage(String payload, int estampille, int sender, int recipient) {
        super(payload, estampille);
        this.sender = sender;
        this.recipient = recipient;
        this.received = false;
    }

    public int getRecipient() {
        return this.recipient;
    }

    public int getSender() {
        return this.sender;
    }

    public boolean isReceived() {
        return this.received;
    }

    public void setReceivedTrue() {
        this.received = true;
    }
}
