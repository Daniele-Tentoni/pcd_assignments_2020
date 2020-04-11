package pcd.pinballs.mvc;

import pcd.pinballs.Simulator;

public class MVCSimulatorMain {
    static public void main(String[] args){
        MVCController controller = new MVCController();
        MVCSimulatorViewer viewer = new MVCSimulatorViewer(controller, 620, 620);
        Simulator simulator = new MVCSimulator(3, 50000, 5, viewer);

        controller.attachSimulator(simulator);
        simulator.execute();
    }
}
