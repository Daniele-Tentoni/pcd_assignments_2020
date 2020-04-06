package pcd.pinballs.masterslave;

import pcd.pinballs.Simulator;

import java.util.ArrayList;

public class MSSimulator extends Simulator {

    private ArrayList<SlaveWorker> slaves;

    public MSSimulator(int nSlave, int nIter, int nBody) {
        super(nIter, nBody);

        Master master = new Master(nIter, this.bodies, nSlave);
        this.slaves = new ArrayList<>();
        for(int i = 0; i < nSlave; i++) {
            this.slaves.add(new SlaveWorker(i, this.bounds, master));
        }
    }

    @Override
    public long execute() {
        long start = System.currentTimeMillis();

        for(SlaveWorker slave: this.slaves) {
            slave.start();
        }

        for(SlaveWorker slave: this.slaves) {
            try {
                slave.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        return end - start;
    }
}
