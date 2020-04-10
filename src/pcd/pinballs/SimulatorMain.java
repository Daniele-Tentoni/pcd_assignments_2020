package pcd.pinballs;

import pcd.pinballs.worker.OldSimulator;

import java.util.ArrayList;

public class SimulatorMain {

    public static void main(String[] args) {

        int nTests = 1;
        ArrayList<MyTest> testsOld = new ArrayList<>();

        testsOld.add(new MyTest(1, 500, 100));
        testsOld.add(new MyTest(5, 500, 100));
        testsOld.add(new MyTest(1, 1000, 100));
        testsOld.add(new MyTest(5, 1000, 100));
        testsOld.add(new MyTest(1, 5000, 100));
        testsOld.add(new MyTest(5, 5000, 100));
        testsOld.add(new MyTest(1, 500, 1000));
        testsOld.add(new MyTest(5, 500, 1000));
        testsOld.add(new MyTest(1, 1000, 1000));
        testsOld.add(new MyTest(5, 1000, 1000));
        testsOld.add(new MyTest(1, 5000, 1000));
        testsOld.add(new MyTest(5, 5000, 1000));
        testsOld.add(new MyTest(1, 500, 5000));
        testsOld.add(new MyTest(5, 500, 5000));
        testsOld.add(new MyTest(1, 1000, 5000));
        testsOld.add(new MyTest(5, 1000, 5000));
        testsOld.add(new MyTest(1, 5000, 5000));
        testsOld.add(new MyTest(5, 5000, 5000));

        for (int i = 0; i < nTests; i++) {
            for (MyTest test : testsOld) {
                // Questo è l'old simulator senza master slave.
                Simulator sim = new OldSimulator(test.nThread, test.iter, test.nBody);
                test.addTime(sim.execute());
                System.out.println("************************");
                System.out.println("Prova con:"
                        + "\n\tN Thread:\t\t" + test.nThread
                        + "\n\tN Iter:\t\t" + test.iter
                        + "\n\tBody:\t\t" + test.nBody
                        + "\n\tMedia:\t\t" + test.getTimesAverage());
            }
        }

        System.out.println("*************************");
        System.out.println("* Test con Master Slave *");
        System.out.println("*************************");
        System.out.println("Prova con:");
        System.out.print("\n\tN Thread:");
        for (MyTest test : testsOld) {
            System.out.print("\t|\t" + test.nThread);
        }
        System.out.print("\n\tN Iter:\t");
        for (MyTest test : testsOld) {
            System.out.print("\t|\t" + test.iter);
        }
        System.out.print("\n\tN Body:\t");
        for (MyTest test : testsOld) {
            System.out.print("\t|\t" + test.nBody);
        }
        System.out.print("\n\tMedia:\t");
        for (MyTest test : testsOld) {
            System.out.print("\t|\t" + test.getTimesAverage());
        }
        System.out.println("\n************************");
    }
}