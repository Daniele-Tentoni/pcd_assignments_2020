package pcd.pinballs.jpf.display;

import pcd.pinballs.Simulator;

public class DisplayMainJpf {
    public static void main(String[] args) {
        BounderViewBuffer buffer = new BounderViewBuffer(10);
        SimulationViewerJpf viewer = new SimulationViewerJpf(buffer);
        ViewWorker view = new ViewWorker(3, 10, buffer);
        Simulator sim = new DisplaySimulatorJpf(2, 3, 3, viewer);

        view.start();
        sim.execute();
        try {
            view.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
