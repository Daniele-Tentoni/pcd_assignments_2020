package pcd.pinballs.worker;

import pcd.pinballs.SimulationViewer;
import pcd.pinballs.Simulator;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class OldSimulator extends Simulator {

    private SimulationViewer viewer;
    private ArrayList<Worker> workers;

    public OldSimulator(int nThread,
                        int nIter,
                        int nBodies,
                        SimulationViewer viewer) {
        super(nIter, nBodies);

        this.viewer = viewer;
        CyclicBarrier barrier = new CyclicBarrier(nThread);

        workers = new ArrayList<>();
        for(int i = 0; i < nThread; i++) {
            workers.add(
                    new SimulatorWorker(
                            i, nIter, bounds, barrier, bodies, viewer));
        }
    }

    public long execute() {

        double vt = 0;

        long start = System.currentTimeMillis();
        /* Manda in esecuzione i workers. */
        for(Worker worker: workers) {
            worker.start();
        }

        for(Worker worker: workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        return end - start;
    }
}
