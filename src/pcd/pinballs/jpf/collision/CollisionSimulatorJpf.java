package pcd.pinballs.jpf;

import gov.nasa.jpf.vm.Verify;
import pcd.pinballs.Simulator;
import pcd.pinballs.worker.SimulatorWorker;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class CollisionSimulatorJpf extends Simulator {
    private ArrayList<SimulatorWorker> workers;

    public CollisionSimulatorJpf(int nThread,
                        int nIter,
                        int nBodies) {
        super(nIter, nBodies);

        CyclicBarrier barrier = new CyclicBarrier(nThread);

        workers = new ArrayList<>();
        for(int i = 0; i < nThread; i++) {
            workers.add(
                    new SimulatorWorker(
                            i, nIter, bounds, barrier, bodies));
        }
    }

    public long execute() {
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
        return 0;
    }

    @Override
    public void startSimulation() {}

    @Override
    public void pauseSimulation() {}
}
