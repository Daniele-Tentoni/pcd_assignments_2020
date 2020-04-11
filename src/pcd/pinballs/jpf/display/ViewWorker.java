package pcd.pinballs.jpf.display;

import pcd.pinballs.worker.Worker;

/**
 * Worker che deve simulare l'EDT per il test con Jpf.
 */
public class ViewWorker extends Worker {
    private BounderViewBuffer buffer;
    private long maxIter, iter;

    public ViewWorker(int index, long maxIter, BounderViewBuffer buffer) {
        super(index);
        this.setName("EDT");

        this.buffer = buffer;
        this.maxIter = maxIter;
    }

    public long getCurrentIter() {
        return this.iter;
    }

    @Override
    public void run() {
        iter = 0;
        while(iter < maxIter) {
            try {
                this.buffer.get();
                iter++;
                Thread.sleep(0, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
