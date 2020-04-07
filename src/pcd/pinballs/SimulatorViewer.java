package pcd.pinballs;

import java.util.ArrayList;

public interface SimulatorViewer {
    void display(ArrayList<Body> bodies, double vt, long iter);
}
