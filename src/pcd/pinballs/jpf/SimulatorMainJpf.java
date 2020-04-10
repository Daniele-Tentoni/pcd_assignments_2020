package pcd.pinballs.jpf;

import pcd.pinballs.Simulator;
import pcd.pinballs.mvc.MVCSimulator;

public class SimulatorMainJpf {
    public static void main(String[] args) {
        BounderViewBuffer buffer = new BounderViewBuffer(10);
        SimulationViewerJpf viewer = new SimulationViewerJpf(buffer);
        ViewWorker view = new ViewWorker(3, 100, buffer);
        Simulator sim = new MVCSimulator(2, 100, 100, viewer);

        view.start();
        long start = System.currentTimeMillis();
        sim.execute();
        try {
            view.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long stop = System.currentTimeMillis();
        System.out.println("Time: " + (stop - start));
    }
}