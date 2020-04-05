package pcd.pinballs.worker;

import pcd.pinballs.Body;
import pcd.pinballs.components.Boundary;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SimulatorWorker extends Worker {
    private CyclicBarrier barrier;
    private ArrayList<Body> bodies;

    public SimulatorWorker(int index,
                           long maxIteration,
                           Boundary boundaries,
                           CyclicBarrier barrier,
                           ArrayList<Body> bodies) {
        super(index, maxIteration, boundaries);

        this.barrier = barrier;
        this.bodies = bodies;
    }

    @Override
    public void run() {
        /* simulation loop */

        /* init virtual time */

        double vt = 0;
        double dt = 0.1;

        long iter = 0;

        while (iter < this.maxIteration){
            log("*** INIZIO ITERAZIONE " + iter + " ***");

            /* compute bodies new pos */

            for (Body b: this.bodies) {
                if(b.takeUpdate()) { // Prendo la pallina se non l'ha già fatto un altro.
                    // log("Aggiorno la posizione di %d", b.getIndex());
                    b.updatePos(dt);
                    // log("Aggiornata la posizione di %d", b.getIndex());
                }
            }

            try {
                // log("Mi sto schiantando contro la barriera.");
                barrier.await();
                log("***___ Ho superato la barriera update " + iter + " ___***");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            /* check collisions */

            for (int i = 0; i < bodies.size() - 1; i++) {
                Body b1 = bodies.get(i);
                if(b1.takeCollide()) {
                    // log("Ho preso il permesso su " + i);
                    for (int j = i + 1; j < bodies.size(); j++) {
                        Body b2 = bodies.get(j);
                        // log("Provo a collidere "+i+"/"+j);
                        if (b1.collideWith(b2)) {
                            // log("*** Collidono "+i+"/"+j+" ***");
                            Body.solveCollision(b1, b2);
                        }
                    }
                }
            }

            try {
                // log("Mi sto schiantando contro la barriera.");
                barrier.await();
                log("***___ Ho superato la barriera collide " + iter + " ___***");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            /* check boundaries */

            for (Body b: this.bodies) {
                if(b.takeBoundary()) {
                    try {
                        log("Controllo i bordi di " + b.getIndex());
                        b.checkAndSolveBoundaryCollision(this.bounds);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                // log("Mi sto schiantando contro la barriera.");
                barrier.await();
                log("***___ Ho superato la barriera boundary " + iter + " ___***");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            /* update virtual time */

            vt = vt + dt;
            iter++;

            /* display current stage */

            // viewer.display(bodies, vt, iter);

        }
    }
}
