package pcd.pinballs.jpf;

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
                /* Verifica con JPF. */
                Verify.beginAtomic();
                Body b1 = bodies.get(i);
                if (b1.takeCollide()) {
                    for (int j = i + 1; j < bodies.size(); j++) {
                        Body b2 = bodies.get(j);
                        if (b1.collideWith(b2)) {
                            Body.solveCollision(b1, b2, this);
                        }
                    }

                    b1.checkAndSolveBoundaryCollision(this.bounds);
                }
                Verify.endAtomic();

                assert (b1.getCurrentIter() <= this.iter && b1.isCollided()) ||
                        (b1.getCurrentIter() > this.iter && !b1.isCollided());
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
