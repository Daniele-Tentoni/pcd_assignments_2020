package pcd.pinballs;

import java.util.ArrayList;

public class SimulatorMain {

    public static void main(String[] args) {

        // SimulationViewer viewer = new SimulationViewer(620,620);
        class Test {
            public int nThread;
            public int iter;
            public int nBody;
            private long time;
            public ArrayList<Long> times;

            public Test(int nThread, int iter, int body) {
                this.nThread = nThread;
                this.iter = iter;
                this.nBody = body;
                this.times = new ArrayList<>();
            }

            public void addTime(long time) {
                this.time = time;
                this.times.add(time);
            }

            public double getTimesAverage() {
                long sum = 0;
                for (Long time: times) {
                    sum += time;
                }
                return (double)sum / times.size();
            }
        }

        ArrayList<Test> tests = new ArrayList<>();
        tests.add(new Test(5, 500, 100));
        tests.add(new Test(5, 1000, 100));
        tests.add(new Test(5, 5000, 100));
        /*tests.add(new Test(5, 500, 1000));
        tests.add(new Test(5, 1000, 1000));
        tests.add(new Test(5, 5000, 1000));
        tests.add(new Test(5, 500, 5000));
        tests.add(new Test(5, 1000, 5000));
        tests.add(new Test(5, 5000, 5000));*/

        int nTests = 1;

        for(int i = 0; i < nTests; i++) {
            for (Test test : tests) {
                Simulator sim = new Simulator(test.nThread, test.iter, test.nBody);
                test.addTime(sim.execute());
                System.out.println("************************");
                System.out.println("Prova con:"
                        + "\n\tN Thread:\t" + test.nThread
                        + "\n\tN Iter:\t\t" + test.iter
                        + "\n\tBody:\t\t" + test.nBody
                        + "\n\tTempo:\t\t" + test.time);
            }
        }

        for (Test test : tests) {
            System.out.println("************************");
            System.out.println("Prova con:"
                    + "\n\tN Thread:\t\t" + test.nThread
                    + "\n\tN Iter:\t\t" + test.iter
                    + "\n\tBody:\t\t" + test.nBody
                    + "\n\tMedia:\t\t" + test.getTimesAverage());
        }

    }
}