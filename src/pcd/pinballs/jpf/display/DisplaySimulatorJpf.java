package pcd.pinballs.jpf.display;

import gov.nasa.jpf.vm.Verify;
import pcd.pinballs.Simulator;
import pcd.pinballs.SimulatorViewer;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class DisplaySimulatorJpf extends Simulator {
    private ArrayList<DisplayWorkerJpf> workers;
    private ViewWorker edt;

    public DisplaySimulatorJpf(int nThread,
                               int nIter,
                               int nBodies,
                               SimulationViewerJpf viewer,
                               BounderViewBuffer buffer) {
        super(nIter, nBodies);

        CyclicBarrier barrier = new CyclicBarrier(nThread);

        edt = new ViewWorker(3, nIter, buffer);

        workers = new ArrayList<>();
        for (int i = 0; i < nThread; i++) {
            workers.add(
                    new DisplayWorkerJpf(i, nIter, barrier, bodies, viewer));
        }
    }

    /**
     * Ritorna l'iterazione del worker con l'iterazione massima.
     * @return Iterazione
     */
    public long getMaxIteration() {
        long max = 0;
        for (DisplayWorkerJpf worker: this.workers) {
            long currentIter = worker.getCurrentIter();
            if(max < currentIter) max = currentIter;
        }
        return max;
    }

    @Override
    public long execute() {
        Verify.beginAtomic();
        edt.start();
        for(Worker worker: workers) {
            worker.start();
        }
        Verify.endAtomic();
        assert edt.getCurrentIter() <= this.getMaxIteration();
        try {
            edt.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
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