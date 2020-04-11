package pcd.pinballs.jpf.collision;

import pcd.pinballs.Simulator;
import pcd.pinballs.jpf.update.UpdateSimulatorJpf;

public class UpdateMainJpf {
    public static void main(String[] args) {
        Simulator sim = new CollisionSimulatorJpf(2, 3, 3);
        sim.execute();
    }
}
