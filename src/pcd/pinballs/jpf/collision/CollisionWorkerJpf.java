package pcd.pinballs.jpf.collision;

import gov.nasa.jpf.vm.Verify;
import pcd.pinballs.Body;
import pcd.pinballs.components.Boundary;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CollisionWorkerJpf extends Worker {
    private long maxIteration, iter;
    private Boundary bounds;
    private CyclicBarrier barrier;
    private ArrayList<Body> bodies;

    public CollisionWorkerJpf(int index,
                           long maxIteration,
                           Boundary bounds,
                           CyclicBarrier barrier,
                           ArrayList<Body> bodies) {
        super(index);

        this.barrier = barrier;
        this.bodies = bodies;
        this.bounds = bounds;
        this.maxIteration = maxIteration;
    }

    /**
     * Ritorna l'iterazione corrente alla quale si trova il worker.
     * Utile solamente ai fini di log del pauser attualmente.
     * @return Iterazione corrente.
     */
    public long getCurrentIter() {
        return this.iter;
    }

    @Override
    public void run() {
        iter = 0;
        while (iter < this.maxIteration) {
            for (int i = 0; i < bodies.size(); i++) {
                Body b1 = bodies.get(i);
                Verify.beginAtomic();
                if (b1.takeCollide()) {
                    b1.incrementIter();
                    for (int j = i + 1; j < bodies.size(); j++) {
                        Body b2 = bodies.get(j);
                        if (b1.collideWith(b2)) {
                            Body.solveCollision(b1, b2);
                        }

                        /*
                        Questa proprietà verifica che l'iterazione locale del corpo b1
                        rispetto l'iterazione locale del corpo b2, successivo per ordine
                        di array al corpo b1, sia maggiore o uguale.
                        Questo per controllare che non ci siano palline successive ad una
                        iterazione locale maggiore, segnale che b1 sia rimasto indietro.
                         */
                        assert b1.getCurrentIter() >= b2.getCurrentIter();
                    }

                    b1.checkAndSolveBoundaryCollision(this.bounds);
                }
                Verify.endAtomic();

                /*
                Questa proprietà controlla che nessuna pallina si trovi ad una iterazione
                diversa da quella corrente prima del controllo delle collisioni e in una
                iterazione diversa da quella successiva dopo il check.
                */
                assert (b1.getCurrentIter() == this.iter && b1.isCollided()) ||
                        (b1.getCurrentIter() == this.iter + 1 && !b1.isCollided());
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            iter++;
            for(Body body : this.bodies) {
                body.takeUpdate();
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
