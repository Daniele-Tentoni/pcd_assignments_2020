package pcd.pinballs.jpf.display;

import pcd.pinballs.Body;
import pcd.pinballs.SimulatorViewer;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class DisplayWorkerJpf extends Worker {
    private long maxIteration;
    private CyclicBarrier barrier;
    private ArrayList<Body> bodies;
    private SimulatorViewer viewer;

    public DisplayWorkerJpf(int index,
                              long maxIteration,
                              CyclicBarrier barrier,
                              ArrayList<Body> bodies,
                              SimulatorViewer viewer) {
        super(index);

        this.barrier = barrier;
        this.bodies = bodies;
        this.maxIteration = maxIteration;
        this.viewer = viewer;
    }

    @Override
    public void run() {
        /* init virtual time */
        double vt = 0;
        double dt = 0.1;
        long iter = 0;
        while (iter < this.maxIteration) {
            /* update virtual time */
            vt = vt + dt;
            iter++;

            /* display current stage */
            Body b = this.bodies.get(0);
            if (b.takeViewer()) { // Prendo la pallina se non l'ha già fatto un altro.
                viewer.display(bodies, vt, iter);
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

        }
    }
}