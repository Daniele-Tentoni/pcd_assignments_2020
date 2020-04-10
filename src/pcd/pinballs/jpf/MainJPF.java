package pcd.pinballs.jpf;

import pcd.pinballs.Simulator;
import pcd.pinballs.worker.OldSimulator;

public class MainJPF {

    public static void main(String[] args) {
        Simulator sim = new OldSimulator(3, 10, 10);
        long time = sim.execute();
        System.out.println(time);
    }
}