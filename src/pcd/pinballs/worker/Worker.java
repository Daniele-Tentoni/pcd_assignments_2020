package pcd.pinballs.worker;

import pcd.pinballs.components.Boundary;

public class Worker extends Thread {
    protected long maxIteration;
    protected Boundary bounds;

    public Worker(int index, long maxIteration, Boundary bounds) {
        this.setName("Worker " + index);
        this.maxIteration = maxIteration;
        this.bounds = bounds;
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