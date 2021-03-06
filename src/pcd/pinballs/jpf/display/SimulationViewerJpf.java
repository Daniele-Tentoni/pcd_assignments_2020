package pcd.pinballs.jpf.display;

import pcd.pinballs.Body;
import pcd.pinballs.SimulatorViewer;

import java.util.ArrayList;

public class SimulationViewerJpf implements SimulatorViewer {
    BounderViewBuffer buffer;

    public SimulationViewerJpf(BounderViewBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void display(ArrayList<Body> bodies, double vt, long iter) {
        try {
            this.buffer.put(iter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
