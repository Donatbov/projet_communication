import java.util.ArrayDeque;
import java.util.Deque;

public class BoiteAuxLettres {
    private Deque<Message> boite;

    public BoiteAuxLettres() {
        this.boite =  new ArrayDeque<>();
    }

    public void push(Message m){
        this.boite.push(m);
    }

    public Message pop(){
        return this.boite.pop();
    }

    public Deque<String> getAllMessages() {
        Deque<String> res = new ArrayDeque<>();
        this.boite.forEach(message -> {
            res.add(message.getPayload());
        });
        return res;
    }

    public String getMessage() {
        return this.boite.pop().getPayload();
    }

}
