package pcd.pinballs.worker;

public class Worker extends Thread {

    public Worker(int index) {
        super();
        this.setName("Worker " + index);
    }

    protected void log(long millis, String message) {
        this.log(message);
        if (millis > 0) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void log(String message) {
        /* Accesso sincronizzato alla risorsa */
        synchronized (System.out) {
            System.out.println("["+this.getName()+"]: "+message);
        }
    }
}