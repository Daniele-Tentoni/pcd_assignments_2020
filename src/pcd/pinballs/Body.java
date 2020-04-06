package pcd.pinballs;

import pcd.pinballs.components.Boundary;
import pcd.pinballs.components.Position;
import pcd.pinballs.components.Velocity;
import pcd.pinballs.worker.Worker;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Body {
    private volatile Position pos;
    private volatile Velocity vel;
    private double radius;
    private int index;

    private boolean updated, collided, viewed;
    private boolean velocityAvailable;
    private ReentrantLock mutex;
    private Condition velocityLock;

    public Body(Position pos, Velocity vel, double radius, int index) {
        this.pos = pos;
        this.vel = vel;
        this.radius = radius;
        this.index = index;

        /* Dispensatori di permessi. */
        this.updated = this.collided = this.viewed = true;

        /* Sincronizzatori. */
        this.velocityAvailable = true;
        this.mutex = new ReentrantLock();
        this.velocityLock = mutex.newCondition();
    }

    /**
     *
     * @return
     */
    public double getRadius() {
        return radius;
    }

    public int getIndex() {
        return this.index;
    }

    /**
     * Prende il diritto sull'aggiornamento della posizione.
     * Se non l'aveva nessuno, lo consegna al chiamante.
     * @return Il permesso di aggiornare la posizione.
     */
    public synchronized boolean takeUpdate() {
        if(this.updated) {
            this.updated = false;
            this.viewed = true; // Rilascia gli altri permessi.
            this.collided = true; // Occhio alle barriere.
            return true;
        }
        return false;
    }

    /**
     * Prende il diritto sulla collisione con altri corpi.
     * Se non l'aveva nessuno, lo consegna al chiamante.
     * @return Il permesso di verificare le collisioni.
     */
    public synchronized boolean takeCollide() {
        if (this.collided) {
            this.collided = false;
            this.updated = true; // Occhio alle barriere.
            return true;
        }
        return false;
    }

    /**
     * Prende il diritto sulla collisione con i bordi.
     * Se non l'aveva nessuno, lo consegna al chiamante.
     * @return Il permesso di verificare le collisioni.
     */
    public synchronized boolean takeViewer() {
        if(this.viewed) {
            this.viewed = false;
            this.updated = true; // Rilascia gli altri permessi.
            return true;
        }
        return false;
    }

    public synchronized Position getPos() {
        // try {
            // mutex.lock();

            // Non dovrebbe servire. Adesso che si schiantano tutti sulla barriera
            // e quindi non ci sarà nessuno in attesa.
            // while (!this.posAvailable) {
            //     this.positionLock.await();
            // }

            return pos;
        // } finally {
        //     mutex.unlock();
        // }
    }

    public Velocity getVel(Worker worker) throws InterruptedException {
        try{
            mutex.lock();

            while(!this.velocityAvailable) {
                worker.log("Velocità ferma " + this.index);
                this.velocityLock.await();
            }

            this.velocityAvailable = false;
            return vel;
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Update the position, according to current velocity
     *
     * @param dt time elapsed
     */
    public void updatePos(double dt) {
        try {
            mutex.lock();

            double newPosX = pos.getX() + vel.getX() * dt;
            double newPosY = pos.getY() + vel.getY() * dt;
            pos.change(newPosX, newPosY);

            // Non dovrebbe servire. Adesso che si schiantano tutti sulla barriera
            // e quindi non ci sarà nessuno in attesa.
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Change the velocity
     *
     * @param vx
     * @param vy
     */
    public void changeVel(double vx, double vy) {
        try{
            mutex.lock();

            vel.change(vx, vy);
            this.velocityAvailable = true;
            this.velocityLock.signalAll();
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Computes the distance from the specified body.
     * Non dovrebbe dare problemi di sincronizzazione.
     * @param b
     * @return
     */
    public double getDistance(Body b) {
        double dx = pos.getX() - b.getPos().getX();
        double dy = pos.getY() - b.getPos().getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Check if there is collision with the specified body
     *
     * @param b
     * @return
     */
    public boolean collideWith(Body b) {
        double distance = getDistance(b);
        return distance < radius + b.getRadius();
    }

    /**
     * Check if there collisions with the boundaty and update the
     * position and velocity accordingly
     *
     * @param bounds
     */
    public void checkAndSolveBoundaryCollision(Boundary bounds) {
        double x = pos.getX();
        double y = pos.getY();
        if (x > bounds.getX1()) {
            pos.change(bounds.getX1(), pos.getY());
            vel.change(-vel.getX(), vel.getY());
        } else if (x < bounds.getX0()) {
            pos.change(bounds.getX0(), pos.getY());
            vel.change(-vel.getX(), vel.getY());
        } else if (y > bounds.getY1()) {
            pos.change(pos.getX(), bounds.getY1());
            vel.change(vel.getX(), -vel.getY());
        } else if (y < bounds.getY0()) {
            pos.change(pos.getX(), bounds.getY0());
            vel.change(vel.getX(), -vel.getY());
        }
    }

    public static void solveCollision(Body b1, Body b2, Worker worker) {
        try {
            Position x1 = b1.getPos();
            Position x2 = b2.getPos();
            Velocity v1 = b1.getVel(worker);
            Velocity v2 = b2.getVel(worker);

            double x12dx = x1.getX() - x2.getX();
            double x12dy = x1.getY() - x2.getY();
            double v12dx = v1.getX() - v2.getX();
            double v12dy = v1.getY() - v2.getY();
            double fact12 = (x12dx * v12dx + x12dy * v12dy) / (x12dx * x12dx + x12dy * x12dy);
            double v1x = v1.getX() - x12dx * fact12;
            double v1y = v1.getY() - x12dy * fact12;

            double x21dx = x2.getX() - x1.getX();
            double x21dy = x2.getY() - x1.getY();
            double v21dx = v2.getX() - v1.getX();
            double v21dy = v2.getY() - v1.getY();
            double fact21 = (x21dx * v21dx + x21dy * v21dy) / (x21dx * x21dx + x21dy * x21dy);
            double v2x = v2.getX() - x21dx * fact21;
            double v2y = v2.getY() - x21dy * fact21;

            b1.changeVel(v1x, v1y);
            b2.changeVel(v2x, v2y);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}