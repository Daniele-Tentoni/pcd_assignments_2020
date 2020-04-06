package pcd.pinballs.masterslave;

import javafx.util.Pair;
import pcd.pinballs.Body;
import pcd.pinballs.components.Boundary;
import pcd.pinballs.worker.Worker;

public class SlaveWorker extends Worker {
    private Boundary bounds;
    private Master master;

    public SlaveWorker(final int index, final Boundary bounds, final Master master) {
        super(index);

        this.bounds = bounds;
        this.master = master;
    }

    public void run() {
        double dt = 0.1;
        while(this.master.getNextIteration()){

            Body body;
            while ((body = this.master.getBodyToUpdate(this)) != null) {
                body.updatePos(dt);
            }

            /*List<Body> bodies;
            while((bodies = this.master.getBodyToCollide(this)) != null) {
                body = bodies.get(0);
                for(int i = 1; i < bodies.size(); i++) {
                    Body tmp = bodies.get(i);
                    if(body.collideWith(tmp)) {
                        Body.solveCollision(body, tmp);
                    }
                }
                body.checkAndSolveBoundaryCollision(bounds);
            }*/

            Pair<Body, Body> pair;
            while((pair = this.master.getBodiesToCollide(this)) != null) {
                if(pair.getKey().collideWith(pair.getValue()))
                    Body.solveCollision(pair.getKey(), pair.getValue(), this);
            }

            while ((body = this.master.getBodyToCollideWithBounds(this)) != null) {
                body.checkAndSolveBoundaryCollision(bounds);
            }

            this.master.syncSlaves(this);

            /*while ((body = this.master.getBodyToView(this)) != null) {
                // TODO: Secondo step.
                log("Dovrei aggiornare la gui per " + body.getIndex());
            }*/
        }

    }
}
