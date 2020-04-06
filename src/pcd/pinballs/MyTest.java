package pcd.pinballs;

import java.util.ArrayList;

public class MyTest {
    public int nThread;
    public int iter;
    public int nBody;
    private long time;
    public ArrayList<Long> times;

    public MyTest(int nThread, int iter, int body) {
        this.nThread = nThread;
        this.iter = iter;
        this.nBody = body;
        this.times = new ArrayList<>();
    }

    public void addTime(long time) {
        this.time = time;
        this.times.add(time);
    }

    public double getTimesAverage() {
        long sum = 0;
        for (Long time : times) {
            sum += time;
        }
        return (double) sum / times.size();
    }
}
