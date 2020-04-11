package pcd.pinballs.jpf.update;

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
            /* compute bodies new pos */
            for (Body b : this.bodies) {
                /*
                 Mettiamo begin/end Atomic per controllare la proprietà sottostante
                 solamente prima e dopo aver risolto tutte le operazioni comprese.
                 */
                Verify.beginAtomic();
                if (b.takeUpdate()) { // Prendo la pallina se non l'ha già fatto un altro.
                    b.incrementIter();
                    b.updatePos(dt);
                }
                Verify.endAtomic();

                /*
                 Questa proprietà controlla che nessuna pallina si trovi ad una iterazione
                 diversa da quella corrente prima dell'update e in una iterazione diversa
                 da quella successiva dopo l'update.
                 */
                assert (b.getCurrentIter() == this.iter && b.isUpdated()) ||
                        (b.getCurrentIter() == this.iter + 1 && !b.isUpdated());
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            for (Body b : this.bodies) {
                // Restituisce il permesso di update alla pallina.
                b.takeCollide();
            }
            iter++;

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
