package pcd.pinballs.worker;

import pcd.pinballs.Body;
import pcd.pinballs.components.Boundary;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SimulatorWorker extends Worker {
    private long maxIteration, iter;
    private Boundary bounds;
    private CyclicBarrier barrier;
    private ArrayList<Body> bodies;

    public SimulatorWorker(int index,
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
        /* simulation loop */
        /* init virtual time */
        double vt = 0;
        double dt = 0.1;
        iter = 0;

        while (iter < this.maxIteration) {
            /* compute bodies new pos */
            for (Body b : this.bodies) {
                if (b.takeUpdate()) { // Prendo la pallina se non l'ha già fatto un altro.
                    b.updatePos(dt);
                }
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            /* check collisions */
            for (int i = 0; i < bodies.size(); i++) {
                Body b1 = bodies.get(i);
                if (b1.takeCollide()) {
                    for (int j = i + 1; j < bodies.size(); j++) {
                        Body b2 = bodies.get(j);
                        if (b1.collideWith(b2)) {
                            Body.solveCollision(b1, b2);
                        }
                    }

                    b1.checkAndSolveBoundaryCollision(this.bounds);
                }
            }

            /* update virtual time */
            vt = vt + dt;
            iter++;
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}