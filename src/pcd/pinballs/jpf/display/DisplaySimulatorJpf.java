package pcd.pinballs.jpf.display;

import pcd.pinballs.Simulator;
import pcd.pinballs.SimulatorViewer;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class DisplaySimulatorJpf extends Simulator {
    private ArrayList<DisplayWorkerJpf> workers;

    public DisplaySimulatorJpf(int nThread,
                        int nIter,
                        int nBodies,
                        SimulatorViewer viewer) {
        super(nIter, nBodies);

        CyclicBarrier barrier = new CyclicBarrier(nThread);

        workers = new ArrayList<>();
        for (int i = 0; i < nThread; i++) {
            workers.add(
                    new DisplayWorkerJpf(
                            i, nIter, barrier, bodies, viewer));
        }
    }

    @Override
    public long execute() {
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