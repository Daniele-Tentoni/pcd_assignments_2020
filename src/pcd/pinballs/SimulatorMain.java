package pcd.pinballs;

import pcd.pinballs.masterslave.MSSimulator;
import pcd.pinballs.worker.OldSimulator;

import java.util.ArrayList;

public class SimulatorMain {

    public static void main(String[] args) {

        SimulationViewer viewer = new SimulationViewer(620,620);

        // ArrayList<MyTest> testsMS = new ArrayList<>();

        /*testsMS.add(new MyTest(2, 500, 100));
        testsMS.add(new MyTest(3, 500, 100));
        testsMS.add(new MyTest(4, 500, 100));
        testsMS.add(new MyTest(2, 500, 100));
        testsMS.add(new MyTest(5, 500, 100));
        testsMS.add(new MyTest(8, 500, 100));
        testsMS.add(new MyTest(9, 500, 100));
        testsMS.add(new MyTest(12, 500, 100));
        testsMS.add(new MyTest(13, 500, 100));
        /*testsMS.add(new MyTest(5, 1000, 100));
        testsMS.add(new MyTest(5, 5000, 100));
        testsMS.add(new MyTest(5, 500, 1000));
        testsMS.add(new MyTest(5, 1000, 1000));
        testsMS.add(new MyTest(5, 5000, 1000));
        /*testsMS.add(new MyTest(5, 500, 5000));
        testsMS.add(new MyTest(5, 1000, 5000));
        testsMS.add(new MyTest(5, 5000, 5000));*/

        int nTests = 3;

        for (int i = 0; i < nTests; i++) {
            for (MyTest test : testsMS) {
                // Questo è il MSSimulator con master slave.
                Simulator sim = new MSSimulator(test.nThread, test.iter, test.nBody);
                test.addTime(sim.execute());
                System.out.println("************************");
                System.out.println("Prova con:"
                        + "\n\tN Thread:\t" + test.nThread
                        + "\n\tN Iter:\t\t" + test.iter
                        + "\n\tBody:\t\t" + test.nBody
                        + "\n\tTempo:\t\t" + test.time);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /*for (MyTest test : testsMS) {
            System.out.println("************************");
            System.out.println("Prova con:"
                    + "\n\tN Thread:\t\t" + test.nThread
                    + "\n\tN Iter:\t\t" + test.iter
                    + "\n\tBody:\t\t" + test.nBody
                    + "\n\tMedia:\t\t" + test.getTimesAverage());
        }*/
        /*System.out.println("*************************");
        System.out.println("* Test con Master Slave *");
        System.out.println("*************************");
        System.out.println("Prova con:");
        System.out.print("\n\tN Thread:");
        for (MyTest test : testsMS) {
            System.out.print("\t|\t" + test.nThread);
        }
        System.out.print("\n\tN Iter:\t");
        for (MyTest test : testsMS) {
            System.out.print("\t|\t" + test.iter);
        }
        System.out.print("\n\tN Body:\t");
        for (MyTest test : testsMS) {
            System.out.print("\t|\t" + test.nBody);
        }
        System.out.print("\n\tMedia:\t");
        for (MyTest test : testsMS) {
            System.out.print("\t|\t" + test.getTimesAverage());
        }
        System.out.println("\n************************");
*/
        ArrayList<MyTest> testsOld = new ArrayList<>();

        testsOld.add(new MyTest(2, 500, 100));
        testsOld.add(new MyTest(3, 500, 100));
        testsOld.add(new MyTest(4, 500, 100));
        testsOld.add(new MyTest(5, 500, 100));
        testsOld.add(new MyTest(2, 1000, 100));
        testsOld.add(new MyTest(3, 1000, 100));
        testsOld.add(new MyTest(4, 1000, 100));
        testsOld.add(new MyTest(5, 1000, 100));

        for (int i = 0; i < nTests; i++) {
            for (MyTest test : testsOld) {
                // Questo è l'old simulator senza master slave.
                Simulator sim = new OldSimulator(test.nThread, test.iter, test.nBody);
                test.addTime(sim.execute());
            }
        }

        System.out.println("***************************");
        System.out.println("* Test senza Master Slave *");
        System.out.println("***************************");
        for (MyTest test : testsOld) {
            System.out.println("************************");
            System.out.println("Prova con:"
                    + "\n\tN Thread:\t\t" + test.nThread
                    + "\n\tN Iter:\t\t" + test.iter
                    + "\n\tBody:\t\t" + test.nBody
                    + "\n\tMedia:\t\t" + test.getTimesAverage());
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