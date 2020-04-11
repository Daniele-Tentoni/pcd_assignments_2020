package pcd.pinballs.jpf.update;

import pcd.pinballs.Simulator;

public class UpdateMainJpf {

    public static void main(String[] args) {
        Simulator sim = new UpdateSimulatorJpf(2, 3, 3);
        sim.execute();
    }
}
