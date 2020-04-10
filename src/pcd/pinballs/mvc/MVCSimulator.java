package pcd.pinballs.mvc;

import pcd.pinballs.Simulator;
import pcd.pinballs.SimulatorViewer;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class MVCSimulator extends Simulator {

    private ArrayList<Worker> workers;
    private Pauser pauser;

    public MVCSimulator(int nThread,
                        int nIter,
                        int nBodies,
                        SimulatorViewer viewer) {
        super(nIter, nBodies);

        CyclicBarrier barrier = new CyclicBarrier(nThread);

        pauser = new Pauser();

        workers = new ArrayList<>();
        for (int i = 0; i < nThread; i++) {
            workers.add(
                    new MVCSimulatorWorker(
                            i, nIter, bounds, barrier, bodies, pauser, viewer));
        }
    }

    @Override
    public long execute() {

        for(Worker worker: workers) {
            worker.start();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.startSimulation();
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
