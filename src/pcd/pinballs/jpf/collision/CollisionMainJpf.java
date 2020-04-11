package pcd.pinballs.jpf.collision;

import pcd.pinballs.Simulator;

public class CollisionMainJpf {
    public static void main(String[] args) {
        Simulator sim = new CollisionSimulatorJpf(2, 3, 3);
        sim.execute();
    }
}
