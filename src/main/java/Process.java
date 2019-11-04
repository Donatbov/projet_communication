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

    }

    public void stop() {
        this.alive = false;
    }

}
