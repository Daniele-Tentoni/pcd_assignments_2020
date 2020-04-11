package pcd.pinballs.jpf.display;

import pcd.pinballs.worker.Worker;

public class ViewWorker extends Worker {
    private BounderViewBuffer buffer;
    private long maxIter;

    public ViewWorker(int index, long maxIter, BounderViewBuffer buffer) {
        super(index);
        this.setName("EDT");

        this.buffer = buffer;
        this.maxIter = maxIter;
    }

    @Override
    public void run() {
        long iter = 0;
        while(iter < maxIter) {
            try {
                iter++;
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
