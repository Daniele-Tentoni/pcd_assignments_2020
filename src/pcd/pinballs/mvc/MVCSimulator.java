package pcd.pinballs.mvc;

import pcd.pinballs.Simulator;
import pcd.pinballs.SimulatorViewer;
import pcd.pinballs.worker.SimulatorWorker;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class MVCSimulator extends Simulator {

    private SimulatorViewer viewer;
    private ArrayList<Worker> workers;
    private Pauser pauser;

    public MVCSimulator(int nThread,
                        int nIter,
                        int nBodies,
                        MVCSimulatorViewer viewer) {
        super(nIter, nBodies);

        this.viewer = viewer;
        CyclicBarrier barrier = new CyclicBarrier(nThread);//, () -> {
            /*if( Mi devo stoppare )
                for(Worker worker: workers) {
                    worker.interrupt();
                }
        });*/

        pauser = new Pauser();

        workers = new ArrayList<>();
        for (int i = 0; i < nThread; i++) {
            workers.add(
                    new SimulatorWorker(
                            i, nIter, bounds, barrier, bodies, pauser, viewer));
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
    public void startSimulation() {
        this.pauser.goWake();
    }

    @Override
    public void pauseSimulation() {
        this.pauser.goSleep();
    }
}
