public class Communicateur implements Lamport {
    private int clock;
    private EventBusService bus;

    public Communicateur() {
        this.setClock(0);
        this.bus = EventBusService.getInstance();
        this.bus.registerSubscriber(this);
    }

    public int getClock() {
        return 0;
    }

    public void setClock(int newValue) {

    }

    public void lockClock() {

    }

    public void unlockClock() {

    }



}
