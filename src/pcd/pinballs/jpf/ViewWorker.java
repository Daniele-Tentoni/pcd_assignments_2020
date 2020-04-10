package pcd.pinballs.jpf;

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
        this.log("Partito");
        long iter = 0;
        while(iter < maxIter) {
            try {
                this.log("Uso risorsa grafica " + this.buffer.get());
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
