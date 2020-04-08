package pcd.pinballs.mvc;

import pcd.pinballs.worker.SimulatorWorker;

/**
 * Monitor che consente di mettere in pausa un thread chiamante
 * in attesa dell'arrivo di un altro che li svegli tutti.
 */
public class Pauser {
    private boolean paused;

    public Pauser() {
        this.paused = true;
    }

    /**
     * Metodo da chiamare per capire se doversi fermare oppure no.
     * Finché lo stato del monitor sarà "in pausa", devo aspettare
     * che qualcuno mi venga a svegliare.
     * @param worker Worker che richiama questo metodo per log.
     * @throws InterruptedException Lanciata quando si interrompe il thread
     * mentre si trova nello stato di attesa.
     */
    public synchronized void checkIfIsPaused(SimulatorWorker worker) throws InterruptedException {
        while(this.paused) {
            // worker.log("Mi sono fermato all'iter " + worker.getCurrentIter());
            this.wait();
        }
    }

    /**
     * Mette lo stato a true, così che i thread che chiamino isPaused
     * si mettano in attesa.
     */
    public synchronized void goSleep() {
        this.paused = true;
    }

    /**
     * Mette lo stato a false, così che i thread che chiamino isPaused
     * non si mettano in attesa.
     */
    public synchronized void goWake() {
        this.paused = false;
        this.notifyAll();
    }
}
