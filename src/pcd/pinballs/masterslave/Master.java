package pcd.pinballs.masterslave;

import javafx.util.Pair;
import pcd.pinballs.Body;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Worker master che dispensa i compiti ai suoi slave worker.
 * Utilizziamo la cyclic barrier di barrier per gestire l'aggiornamento
 * dell'iterazione alla fine di tutte le operazioni.
 */
public class Master {
    private ArrayList<Body> bodies;
    private int bodyToUpdate, bodyToCollide, bodyToCollideWith, bodyToBound, bodyToView;
    private long iteration, maxIteration;
    private CyclicBarrier barrier;

    public Master(long maxIteration, ArrayList<Body> bodies, int nThread) {
        this.bodyToUpdate =
                this.bodyToCollide =
                                this.bodyToBound =
                                        this.bodyToView = 0;
        this.bodyToCollideWith = 1;
        this.iteration = 0;
        this.maxIteration = maxIteration;
        this.bodies = bodies;
        this.barrier = new CyclicBarrier(nThread, () -> {
            // Resetto tutti i contatori e incremento l'iterazione.
            this.iteration++;
            this.bodyToUpdate =
                    this.bodyToCollide =
                                    this.bodyToBound =
                                            this.bodyToView = 0;
            this.bodyToCollideWith = 1;
        });
    }

    /**
     * Permette di capire se deve esserci una nuova iterazione oppure no.
     * Gli slave saranno liberi di fare ciò che vogliono quando
     * avranno terminato i loro compiti con questo master.
     * @return True se deve essercene un'altra, false altrimenti.
     */
    public synchronized boolean getNextIteration() {
        return this.iteration < this.maxIteration;
    }

    /**
     * Dispensa il prossimo body da aggiornare allo slave che glielo richiede.
     * Se non ci sono altri body da aggiornare, semplicemente ritorna null.
     * A quel punto il task slave può passare a fare un'altro compito e
     * tornare qui quando ci saranno altre palline da aggiornare.
     * @param slave Worker che richiede questo metodo.
     * @return Prossima pallina da gestire oppure null se non ce ne sono.
     */
    public synchronized Body getBodyToUpdate(Worker slave) {
        if(bodyToUpdate == bodies.size()) {
            // slave.log("Ho terminato gli update.");
            return null;
        }

        // slave.log("Assegno l'update di " + bodyToUpdate + " a " + slave.getName());
        return bodies.get(bodyToUpdate++);
    }

    /**
     * Ritorna un array contenente come primo elemento il corpo su cui devono
     * essere computate tutte le collisioni con le successive palline.
     * Se non ci sono altre palline da fare collidere, arriverà un array con una
     * sola pallina (l'ultima) su cui non ci saranno collisioni da calcolare se
     * non quella con i bordi, e successivamente null, come per l'update.
     * @param slave Worker che richiede le palline da collidere.
     * @return Array di palline da fare collidere.
     */
    public synchronized List<Body> getBodyToCollide(Worker slave) {
        if(bodyToCollide == bodies.size()) {
            // slave.log("Ho terminato le collisioni.");
            return null;
        }

        // slave.log("Assegno le collisioni di " + bodyToCollide + " a " + slave.getName());
        return bodies.subList(bodyToCollide++, bodies.size());
    }

    public synchronized Pair<Body, Body> getBodiesToCollide(Worker slave) {
        // Check come prima
        if(bodyToCollideWith >= bodies.size()) {
            bodyToCollide++;

            if(bodyToCollide >= bodies.size() - 1) {
                return null;
            }

            bodyToCollideWith = bodyToCollide + 1;
        }

        // slave.log("Collidono " + bodyToCollide + " con " + bodyToCollideWith);
        Body b1 = bodies.get(bodyToCollide);
        Body b2 = bodies.get(bodyToCollideWith++);

        return new Pair<>(b1, b2);
    }

    public synchronized Body getBodyToCollideWithBounds(Worker slave) {
        if(bodyToBound == bodies.size())
            return null;

        return bodies.get(bodyToBound++);
    }

    /**
     * Ritorna il prossimo corpo su cui deve essere fatto un aggiornamento
     * sulla view.
     * TODO: Questo metodo deve essere usato per il secondo step.
     * @param slave Worker che richiede il corpo da aggiornare.
     * @return Body da aggiornare sulla view.
     */
    public synchronized Body getBodyToView(Worker slave) {
        if(this.bodyToView == this.bodies.size()) {
            // Mettiamo una barriera qui per aspettare che tutti abbiano
            // finito gli aggiornamenti alla view.
            return null;
        }

        return this.bodies.get(this.bodyToView++);
    }

    public void syncSlaves(Worker slave) {
        // slave.log("Sbatto all'iterazione " + this.iteration);
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        // slave.log("Sblocco all'iterazione " + this.iteration);
    }
}
