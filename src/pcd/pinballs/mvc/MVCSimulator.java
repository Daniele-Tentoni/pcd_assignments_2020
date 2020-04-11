package pcd.pinballs.mvc;

import gov.nasa.jpf.vm.Verify;
import pcd.pinballs.Simulator;
import pcd.pinballs.SimulatorViewer;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class MVCSimulator extends Simulator {

    private ArrayList<MVCSimulatorWorker> workers;
    private Pauser pauser;
    private int nThread;

    public MVCSimulator(int nThread,
                        int nIter,
                        int nBodies,
                        SimulatorViewer viewer) {
        super(nIter, nBodies);
        this.nThread = nThread;

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
        Verify.beginAtomic();
        for(Worker worker: workers) {
            worker.start();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.startSimulation();
        Verify.endAtomic();

        for(MVCSimulatorWorker worker: workers) {
            assert worker.getCurrentIter() >= 0;
            // assert this.nIterations == worker.getCurrentIter();
        }

        //assert ( 11 == 10);
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
