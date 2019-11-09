public class Process implements Runnable {
    private Thread thread;
    private boolean alive;
    private Communicateur communicateur;


    public Process(String name) {
        this.thread = new Thread(this);
        this.thread.setName(name);
        this.communicateur = new Communicateur();
        this.alive = true;
        this.thread.start();
    }

    public void run() {
        while (this.alive) {
            String message = "Message du processus 1 pour le processus 2";

            try {
                this.communicateur.broadcastSync(message,1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getId () {
        return this.communicateur.getId();
    }

    public void stop() {
        this.getAndDisplayAllMessages();
        this.alive = false;
    }

    private void getAndDisplayAllMessages(){
        this.communicateur.getBoiteAuxLettres().getAllMessages().forEach(System.out::println);
    }
}
