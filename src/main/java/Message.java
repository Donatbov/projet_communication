public abstract class Message {
    protected final String payload;
    protected final int estampille;

    public Message(String payload, int estampille) {
        this.payload = payload;
        this.estampille = estampille;
    }

    public String getPayload() {
        return this.payload;
    }

    public int getEstampille() {
        return this.estampille;
    }

    public String toString() {
        return "Message: " + this.payload + " Estampille : " + this.estampille;
    }
}
