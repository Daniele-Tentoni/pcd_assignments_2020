package pcd.pinballs.worker;

import pcd.pinballs.Body;
import pcd.pinballs.SimulatorViewer;
import pcd.pinballs.components.Boundary;
import pcd.pinballs.mvc.Pauser;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SimulatorWorker extends Worker {
    private long maxIteration, iter;
    private Boundary bounds;
    private CyclicBarrier barrier;
    private ArrayList<Body> bodies;
    private SimulatorViewer viewer;
    private Pauser pauser;

    public SimulatorWorker(int index,
                           long maxIteration,
                           Boundary bounds,
                           CyclicBarrier barrier,
                           ArrayList<Body> bodies,
                           Pauser pauser,
                           SimulatorViewer viewer) {
        super(index);

        this.barrier = barrier;
        this.bodies = bodies;
        this.bounds = bounds;
        this.maxIteration = maxIteration;
        this.viewer = viewer;
        this.pauser = pauser;
    }

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
            try {
                this.pauser.checkIfIsPaused(this);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                return;
            }
            // log("*** INIZIO ITERAZIONE " + iter + " ***");

            /* compute bodies new pos */

            for (Body b : this.bodies) {
                if (b.takeUpdate()) { // Prendo la pallina se non l'ha già fatto un altro.
                    // log("Aggiorno la posizione di %d", b.getIndex());
                    b.updatePos(dt);
                    // log("Aggiornata la posizione di %d", b.getIndex());
                }
            }

            try {
                // log("Mi sto schiantando contro la barriera.");
                barrier.await();
                // log("***___ Ho superato la barriera update " + iter + " ___***");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            /* check collisions */

            for (int i = 0; i < bodies.size(); i++) {
                Body b1 = bodies.get(i);
                if (b1.takeCollide()) {
                    // log("Ho preso il permesso su " + i);
                    for (int j = i + 1; j < bodies.size(); j++) {
                        Body b2 = bodies.get(j);
                        // log("Provo a collidere "+i+"/"+j);
                        if (b1.collideWith(b2)) {
                            // log("*** Collidono "+i+"/"+j+" ***");
                            Body.solveCollision(b1, b2, this);
                        }
                    }

                    b1.checkAndSolveBoundaryCollision(this.bounds);
                }
            }

            /* update virtual time */

            vt = vt + dt;
            iter++;

            /* display current stage */

            Body b = this.bodies.get(0);
            if (b.takeViewer()) { // Prendo la pallina se non l'ha già fatto un altro.
                // log("Aggiorno la posizione di %d", b.getIndex());
                //System.out.println("display");
                viewer.display(bodies, vt, iter);
                // log("Aggiornata la posizione di %d", b.getIndex());
            }

            try {
                // log("Mi sto schiantando contro la barriera.");
                barrier.await();
                // log("***___ Ho superato la barriera boundary " + iter + " ___***");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

        }
    }
}