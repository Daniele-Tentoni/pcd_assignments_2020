package pcd.pinballs.jpf;

import pcd.pinballs.Simulator;
import pcd.pinballs.mvc.MVCSimulator;

public class SimulatorMainJpf {
    public static void main(String[] args) {
        BounderViewBuffer buffer = new BounderViewBuffer(100);
        SimulationViewerJpf viewer = new SimulationViewerJpf(buffer);
        ViewWorker view = new ViewWorker(5, 1000, buffer);
        Simulator sim = new MVCSimulator(4, 1000, 1000, viewer);

        view.start();
        long time = sim.execute();
        try {
            view.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Time: " + time);
    }
}