import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Communicateur implements Lamport {
    private int clock;
    private static int nombreCommunicateurs = 0;
    private int id;
    private List<SynchronisedMessage> listSynchroniseMessages;
    private BoiteAuxLettres boiteAuxLettres;
    private EventBusService bus;
    private boolean attendJeton;
    private boolean isInSectionCritique;
    private boolean isSynchronized;
    private static int nbProcessusEnAttenteDeBroadcastMessageSync;
    private boolean attendsMessageBroadcastSync;

    public Communicateur() {
        nombreCommunicateurs++;
        this.id = nombreCommunicateurs - 1;
        this.setClock(0);
        this.attendJeton = false;
        this.isInSectionCritique = false;
        this.isSynchronized = false;
        this.attendsMessageBroadcastSync = false;
        this.listSynchroniseMessages = new ArrayList<>();
        this.boiteAuxLettres = new BoiteAuxLettres();
        this.bus = EventBusService.getInstance();
        this.bus.registerSubscriber(this);

        if (this.id == 0) {
            Token t = new Token(nextProcess());
            bus.postEvent(t);
        }
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
    public void onBroadCastMessageSyncOnBus (BroadcastMessageSync b) {
        if (!(b.getSender() == this.id)) {
            this.attendsMessageBroadcastSync = false;
            nbProcessusEnAttenteDeBroadcastMessageSync--;
            System.out.println("P" + this.id + " message broadcast sync de " + b.getSender() + " reçu : " + b.getPayload());
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

    @Subscribe
    public void onTokenOnBus (Token t) throws InterruptedException {
        if (t.getRecipient() == this.id) {
            if (this.attendJeton) {
                this.isInSectionCritique = true; // this.thread entre en section critique
                while (isInSectionCritique) {
                    Thread.sleep(50);
                    // on attend de ne plus être en section critique
                }
            }
            t.setRecipient(nextProcess());
            bus.postEvent(t);
        }
    }

    @Subscribe
    public void onSynchroniseMessageOnBus (SynchronisedMessage s) {
        this.listSynchroniseMessages.add(s);
        if (this.listSynchroniseMessages.size() == nombreCommunicateurs) {
            int max = Collections.max(this.listSynchroniseMessages).getEstampille();
            setClock(max);
            System.out.println("P" + this.id + " set clock of processus " + this.id + " to " + this.getClock());
        }
    }

    public void broadcast (String payload) {
        this.incrementClock();
        BroadcastMessage b = new BroadcastMessage(payload, this.getClock(), this.id);
        this.bus.postEvent(b);
    }

    public void broadcastSync (String payload, int from) throws InterruptedException {
        if(from == this.id) {
            System.out.println("P" + this.id + " Attente broadcastSync FROM");
            while(nbProcessusEnAttenteDeBroadcastMessageSync < nombreCommunicateurs - 1) {
                Thread.sleep(10);
            }
            System.out.println("P" + this.id + "Fin attente broadcastSync FROM.... Envoi du message");
            this.incrementClock();
            BroadcastMessageSync b = new BroadcastMessageSync(payload, this.getClock(), this.id);
            this.bus.postEvent(b);
        } else {
            System.out.println("P" + this.id + " Attente broadcastSync TO");
            this.attendsMessageBroadcastSync = true;
            nbProcessusEnAttenteDeBroadcastMessageSync++;
            while(this.attendsMessageBroadcastSync) {
                Thread.sleep(10);
            }
            System.out.println("P" + this.id + " Fin attente broadcastSync TO");
        }
    }

    public void sendTo (int recipientId, String payload) {
        this.incrementClock();
        DedicatedMessage d = new DedicatedMessage(payload, this.getClock(), recipientId);
        this.bus.postEvent(d);
    }

    public void synchronize () throws InterruptedException {
        System.out.println("Demande de synchronisation du processus " + this.id);
        // Envoie d'un message de synchronisation
        this.isSynchronized = false;
        this.incrementClock();
        SynchronisedMessage s = new SynchronisedMessage(this.getClock());
        this.bus.postEvent(s);

        // On attend que tous les processus aient envoyé un message de synchronisation
        while (!this.isSynchronized){
            Thread.sleep(50);
        }
    }

    public void requestSC() throws InterruptedException {
        this.attendJeton = true;
        while (!this.isInSectionCritique) {
            // on attend
            Thread.sleep(50);
        }
    }

    public void releaseSC() {
        this.attendJeton = false;
        // On sort de la section critique
        this.isInSectionCritique = false;
    }

    public int nextProcess() {
        return (this.id + 1) % nombreCommunicateurs;
    }
}
