package pcd.pinballs.mvc;

import pcd.pinballs.Simulator;
import pcd.pinballs.SimulatorViewer;

public class MVCSimulatorMain {
    static public void main(String[] args){

        MVCController controller = new MVCController();
        SimulatorViewer viewer = new MVCSimulatorViewer(controller, 620, 620);
        Simulator simulator = new MVCSimulator(4, 500, 100, viewer);


        // model.addObserver(view);

        // new MyAgent(model).start();
        controller.attachSimulator(simulator);
        simulator.execute();

    }
}
