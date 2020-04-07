package pcd.pinballs.mvc;

import pcd.pinballs.Simulator;

public class MVCController {
    private Simulator simulator;

    public void attachSimulator(Simulator simulator) {
        this.simulator = simulator;
    }

    public void processEvent(String event) {
        try {
            new Thread(() -> {
                try {
                    if(event.equals("Start")) {
                        System.out.println("[Controller] Start simulation.");
                        this.simulator.startSimulation();
                    } else if (event.equals("Stop")) {
                        System.out.println("[Controller] Stop simulation.");
                        this.simulator.stopSimulation();
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
