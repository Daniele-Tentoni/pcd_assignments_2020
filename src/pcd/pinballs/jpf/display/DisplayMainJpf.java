package pcd.pinballs.jpf.display;


public class DisplayMainJpf {
    public static void main(String[] args) {
        BounderViewBuffer buffer = new BounderViewBuffer(2);
        SimulationViewerJpf viewer = new SimulationViewerJpf(buffer);
        DisplaySimulatorJpf sim = new DisplaySimulatorJpf(1, 3, 3, viewer, buffer);
        sim.execute();
    }
}
