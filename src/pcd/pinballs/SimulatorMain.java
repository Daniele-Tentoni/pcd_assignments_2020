package pcd.pinballs;

import java.util.ArrayList;

public class SimulatorMain {

    public static void main(String[] args) {

        // SimulationViewer viewer = new SimulationViewer(620,620);

        ArrayList<MyTest> tests = new ArrayList<>();
        tests.add(new MyTest(2, 5000, 100));
        tests.add(new MyTest(3, 5000, 100));
        tests.add(new MyTest(4, 5000, 100));
        // tests.add(new Test(5, 500, 100));
        // tests.add(new Test(5, 1000, 100));
        tests.add(new MyTest(5, 5000, 100));
        /*tests.add(new Test(5, 500, 1000));
        tests.add(new Test(5, 1000, 1000));
        tests.add(new Test(5, 5000, 1000));
        tests.add(new Test(5, 500, 5000));
        tests.add(new Test(5, 1000, 5000));
        tests.add(new Test(5, 5000, 5000));*/

        int nTests = 20;

        for (int i = 0; i < nTests; i++) {
            for (MyTest test : tests) {
                test.addTime(sim.execute());
                /*System.out.println("************************");
                System.out.println("Prova con:"
                        + "\n\tN Thread:\t" + test.nThread
                        + "\n\tN Iter:\t\t" + test.iter
                        + "\n\tBody:\t\t" + test.nBody
                        + "\n\tTempo:\t\t" + test.time);*/
            }
        }

        for (MyTest test : tests) {
            System.out.println("************************");
            System.out.println("Prova con:"
                    + "\n\tN Thread:\t\t" + test.nThread
                    + "\n\tN Iter:\t\t" + test.iter
                    + "\n\tBody:\t\t" + test.nBody
                    + "\n\tMedia:\t\t" + test.getTimesAverage());
        }

    }
}