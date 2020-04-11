package pcd.pinballs.worker;

import gov.nasa.jpf.vm.Verify;
import pcd.pinballs.Simulator;
import pcd.pinballs.mvc.MVCSimulatorWorker;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;


public class OldSimulator extends Simulator {

    private ArrayList<SimulatorWorker> workers;

    public OldSimulator(int nThread,
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

        long start = System.currentTimeMillis();

        /* Verifica con JPF. */
        Verify.beginAtomic();
        /* Manda in esecuzione i workers. */
        for(Worker worker: workers) {
            worker.start();
        }
        Verify.endAtomic();

        for(SimulatorWorker worker: workers) {
            assert worker.getCurrentIter() >= 0;
            // assert this.nIterations == worker.getCurrentIter();
        }


        for(Worker worker: workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        //assert end - start < 100;
        return end - start;
    }

    @Override
    public void startSimulation() {
        // Non fa nulla.
    }

    @Override
    public void pauseSimulation() {
        // Non fa nulla.
    }
}
