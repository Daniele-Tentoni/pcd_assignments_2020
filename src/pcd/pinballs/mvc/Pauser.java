package pcd.pinballs.mvc;

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
     * @throws InterruptedException Lanciata quando si interrompe il thread
     * mentre si trova nello stato di attesa.
     */
    public synchronized void checkIfIsPaused() throws InterruptedException {
        while(this.paused) {
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
     * non si mettano in attesa. Inoltre sveglia anche tutti coloro che
     * sono in attesa di essere svegliati dalla pausa.
     */
    public synchronized void goWake() {
        this.paused = false;
        this.notifyAll();
    }
}
