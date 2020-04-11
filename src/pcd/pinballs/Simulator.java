package pcd.pinballs;

import pcd.pinballs.components.Boundary;
import pcd.pinballs.components.Position;
import pcd.pinballs.components.Velocity;

import java.util.ArrayList;
import java.util.Random;

public abstract class Simulator {
    /* bodies in the field */
    protected ArrayList<Body> bodies;
    protected Boundary bounds;
    protected int nIterations;

    public Simulator(int maxIterations, int nBody) {
        /* initializing boundary and bodies */
        /* boundary of the field */
        bounds = new Boundary(-1.0, -1.0, 1.0, 1.0);
        this.initBodies(nBody, bounds);
        this.nIterations = maxIterations;
    }

    public abstract long execute();

    public void initBodies(int nBody, Boundary bounds) {
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
    }

    /**
     * Avvia la simulazione.
     */
    public abstract void startSimulation();

    /**
     * Ferma la simulazione.
     */
    public abstract void pauseSimulation();
}
