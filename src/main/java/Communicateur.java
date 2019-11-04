public class Communicateur implements Lamport {
    private int clock;

    private EventBusService bus;

    public Communicateur() {
        this.setClock(0);
        this.bus = EventBusService.getInstance();
        this.bus.registerSubscriber(this);
    }

    public synchronized int getClock() {
        return this.clock;
    }

    public synchronized void setClock(int newValue) {
        this.clock = newValue;
    }

    public synchronized void incrementClock() {
        this.setClock(this.getClock() + 1);
    }



}
