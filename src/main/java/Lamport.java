public interface Lamport {
    int getClock();

    void setClock(int newValue);

    void incrementClock();
}
