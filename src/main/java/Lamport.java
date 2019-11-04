public interface Lamport {
    public int getClock();

    public void setClock(int newValue);

    public void lockClock();

    public void unlockClock();
}
