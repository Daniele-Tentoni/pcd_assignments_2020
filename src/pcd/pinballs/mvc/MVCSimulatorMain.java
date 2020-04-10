package pcd.pinballs.mvc;

import pcd.pinballs.Simulator;

public class MVCSimulatorMain {
    static public void main(String[] args){
        MVCController controller = new MVCController();
        MVCSimulatorViewer viewer = new MVCSimulatorViewer(controller, 620, 620);
        Simulator simulator = new MVCSimulator(3, 5, 5, viewer);

        // NBody 100, 1000, 5000
        // NSteps 500, 1000, 5000
        controller.attachSimulator(simulator);
        simulator.execute();

    }
}
