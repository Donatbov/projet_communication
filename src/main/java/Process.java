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
            int sender = 1;
            try {
                if (this.getId() == sender) {
                    String message = "Message du processus n°" + sender;
                    communicateur.sendToSync(2, message);
                }
                Thread.sleep(10);
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
