package pcd.pinballs.jpf;

import gov.nasa.jpf.vm.Verify;
import pcd.pinballs.Simulator;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class UpdateSimulatorJpf extends Simulator {

    private ArrayList<UpdateWorkerJpf> workers;

    public UpdateSimulatorJpf(int nThread,
                        int nIter,
                        int nBodies) {
        super(nIter, nBodies);

        CyclicBarrier barrier = new CyclicBarrier(nThread);

        workers = new ArrayList<>();
        for (int i = 0; i < nThread; i++) {
            workers.add(
                    new UpdateWorkerJpf(
                            i, nIter, barrier, bodies));
        }
    }

    @Override
    public long execute() {
        // long start = System.currentTimeMillis();

        /* Manda in esecuzione i workers. */
        for(Worker worker: workers) {
            worker.start();
        }

        for(UpdateWorkerJpf worker: workers) {
            // assert worker.getCurrentIter() >= 0;
            // assert this.nIterations == worker.getCurrentIter();
        }


        for(Worker worker: workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // long end = System.currentTimeMillis();
        //assert end - start < 100;
        return 0;
    }

    @Override
    public void startSimulation() {
    }

    @Override
    public void pauseSimulation() {
    }
}
