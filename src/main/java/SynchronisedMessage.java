public class SynchronisedMessage implements Comparable{
    protected final int estampille;

    public SynchronisedMessage(int estampille) {
        this.estampille = estampille;
    }

    public int getEstampille() {
        return estampille;
    }

    @Override
    public int compareTo(Object o) {
        SynchronisedMessage s = (SynchronisedMessage) o;
        return Integer.compare(this.estampille,s.getEstampille());
    }
}
