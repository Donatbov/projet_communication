import com.google.common.eventbus.Subscribe;

public class Communicateur implements Lamport {
    private int clock;
    private static int nombreCommunicateurs = 0;
    private int id;
    private BoiteAuxLettres boiteAuxLettres;
    private EventBusService bus;

    public Communicateur() {
        nombreCommunicateurs++;
        this.id = nombreCommunicateurs - 1;
        this.setClock(0);
        this.boiteAuxLettres = new BoiteAuxLettres();
        this.bus = EventBusService.getInstance();
        this.bus.registerSubscriber(this);
    }

    public int getId () {
        return this.id;
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

    public BoiteAuxLettres getBoiteAuxLettres() {
        return this.boiteAuxLettres;
    }

    @Subscribe
    public void onBroadCastMessageOnBus (BroadcastMessage b) {
        if (!(b.getSender() == this.id)) {
            System.out.println("P" + this.id + " receive message with estampille: " + b.getEstampille());
            System.out.println("P" + this.id + " actual clock: " + getClock());
            if (b.getEstampille() > getClock()) {
                setClock(b.getEstampille());
            }
            incrementClock();
            System.out.println("P" + this.id + " new clock : " + getClock());

            this.boiteAuxLettres.push(b);
            System.out.println("P" + this.id + " le facteur est passé :p, il a déposé le message du processus n°" + b.getSender() +
                    " avec le message suivant: " + b.getPayload());
        }
    }

    @Subscribe
    public void onDedicatedMessageOnBus (DedicatedMessage b) {
        if (b.getRecipient() == this.id) {
            System.out.println("P" + this.id + " receive message with estampille: " + b.getEstampille());
            System.out.println("P" + this.id + " actual clock: " + getClock());
            if (b.getEstampille() > getClock()) {
                setClock(b.getEstampille());
            }
            incrementClock();
            System.out.println("P" + this.id + " new clock : " + getClock());

            this.boiteAuxLettres.push(b);
            System.out.println("P" + this.id + " le facteur est passé :p, il a déposé un message pour le processus n°" + b.getRecipient() +
                    " avec le message suivant: " + b.getPayload());
        }
    }

    public void broadcast (String payload) {
        this.incrementClock();
        BroadcastMessage b = new BroadcastMessage(payload, this.getClock(), this.id);
        this.bus.postEvent(b);
    }

    public void sendTo (int recipientId, String payload) {
        this.incrementClock();
        DedicatedMessage d = new DedicatedMessage(payload, this.getClock(), recipientId);
        this.bus.postEvent(d);
    }
}
