package pcd.pinballs;

import pcd.pinballs.components.Boundary;
import pcd.pinballs.components.Position;
import pcd.pinballs.components.Velocity;
import pcd.pinballs.worker.SimulatorWorker;
import pcd.pinballs.worker.Worker;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Simulator {
    /* bodies in the field */
    ArrayList<Body> bodies;

    private ArrayList<Worker> workers;

    public Simulator(int nThread, int maxIterations, int nBody) {

        /* initializing boundary and bodies */
        /* boundary of the field */
        Boundary bounds = new Boundary(-1.0, -1.0, 1.0, 1.0);


        CyclicBarrier barrier = new CyclicBarrier(nThread);

        /* test with 100 small bodies */
        Random rand = new Random(System.currentTimeMillis());
        bodies = new ArrayList<>();
        for (int i = 0; i < nBody; i++) {
            double x = bounds.getX0() + rand.nextDouble()*(bounds.getX1() - bounds.getX0());
            double y = bounds.getX0() + rand.nextDouble()*(bounds.getX1() - bounds.getX0());
            double dx = -1 + rand.nextDouble() * 2;
            double speed = rand.nextDouble() * 0.05;
            Position pos = new Position(x, y);
            Velocity vel = new Velocity(dx*speed,Math.sqrt(1 - dx*dx)*speed);
            Body b = new Body(pos, vel, 0.01, i);
            bodies.add(b);
        }

        workers = new ArrayList<>();
        for(int i = 0; i < nThread; i++) {
            workers.add(new SimulatorWorker(i, maxIterations, bounds, barrier, bodies));
        }
    }

    public long execute() {

        long start = System.currentTimeMillis();
        /* Manda in esecuzione i workers. */
        for(Worker worker: workers) {
            worker.start();
        }

        for(Worker worker: workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        return end - start;

    }
}
