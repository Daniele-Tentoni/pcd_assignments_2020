package pcd.pinballs.mvc;

import pcd.pinballs.SimulationViewer;
import pcd.pinballs.Simulator;
import pcd.pinballs.SimulatorViewer;
import pcd.pinballs.worker.SimulatorWorker;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class MVCSimulator extends Simulator {

    private SimulatorViewer viewer;
    private ArrayList<Worker> workers;

    public MVCSimulator(int nThread,
                        int nIter,
                        int nBodies,
                        SimulatorViewer viewer) {
        super(nIter, nBodies);

        this.viewer = viewer;
        CyclicBarrier barrier = new CyclicBarrier(nThread, () -> {
            /*if( Mi devo stoppare )
                for(Worker worker: workers) {
                    worker.interrupt();
                }*/
        });

        workers = new ArrayList<>();
        for (int i = 0; i < nThread; i++) {
            workers.add(
                    new SimulatorWorker(
                            i, nIter, bounds, barrier, bodies, viewer));
        }
    }

    @Override
    public long execute() {
        return 0;
    }
}
