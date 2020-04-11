package pcd.pinballs.jpf;

import gov.nasa.jpf.vm.Verify;
import pcd.pinballs.Body;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class UpdateWorkerJpf extends Worker {
    private long maxIteration, iter;
    private CyclicBarrier barrier;
    private ArrayList<Body> bodies;

    public UpdateWorkerJpf(int index,
                           long maxIteration,
                           CyclicBarrier barrier,
                           ArrayList<Body> bodies) {
        super(index);

        this.barrier = barrier;
        this.bodies = bodies;
        this.maxIteration = maxIteration;
    }

    public long getCurrentIter() {
        return this.iter;
    }

    @Override
    public void run() {
        double dt = 0.1;
        iter = 0;

        while (iter < this.maxIteration) {
            // log("*** INIZIO ITERAZIONE " + iter + " ***");

            /* compute bodies new pos */

            for (Body b : this.bodies) {
                /* Verifica con JPF. */
                Verify.beginAtomic();

                if (b.takeUpdate()) { // Prendo la pallina se non l'ha già fatto un altro.
                    // log("Aggiorno la posizione di %d", b.getIndex());
                    b.updatePos(dt);
                    // log("Aggiornata la posizione di %d", b.getIndex());
                }

                Verify.endAtomic();
                assert (b.getCurrentIter() <= this.iter && b.isUpdated()) ||
                        (b.getCurrentIter() > this.iter && !b.isUpdated());
            }

            try {
                // log("Mi sto schiantando contro la barriera.");
                barrier.await();
                // log("***___ Ho superato la barriera update " + iter + " ___***");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
