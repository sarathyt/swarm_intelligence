package pso_basic_functions;

import animation.ScatterPlotMove;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import static pso_basic_functions.PSOConstants.C1;
import static pso_basic_functions.PSOConstants.C2;
import static pso_basic_functions.PSOConstants.MAX_ITERATION;
import static pso_basic_functions.PSOConstants.PROBLEM_DIMENSION;
import static pso_basic_functions.PSOConstants.SWARM_SIZE;
import static pso_basic_functions.PSOConstants.W_LOWERBOUND;
import static pso_basic_functions.PSOConstants.W_UPPERBOUND;
import static pso_basic_functions.PSO_chemo_Process.demo;
import static pso_basic_functions.PSO_chemo_Process.fitnessValueList;
import static pso_basic_functions.PSO_chemo_Process.gBest;
import static pso_basic_functions.PSO_chemo_Process.gBestLocation;
import static pso_basic_functions.PSO_chemo_Process.pBestLocation;
import static pso_basic_functions.PSO_chemo_Process.swarm;
import static pso_basic_functions.PSO_chemo_Process.t;
import static pso_basic_functions.PSO_chemo_Process.timer;

public class Particle extends TimerTask {

    private double fitnessValue;
    private Velocity velocity;
    private Location location;
    static double err = 9999;

    public Particle() {
        super();
    }

    public Particle(double fitnessValue, Velocity velocity, Location location) {
        super();
        this.fitnessValue = fitnessValue;
        this.velocity = velocity;
        this.location = location;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getFitnessValue() {
        fitnessValue = ProblemSet.evaluate(location);
        return fitnessValue;
    }

    @Override
    public void run() {
        if (t >= MAX_ITERATION && err <= ProblemSet.ERR_TOLERANCE) {
            timer.cancel();
        }
        
        double w;
        Random generator = new Random();
        
        // step 1 - update pBest
        for (int i = 0; i < SWARM_SIZE; i++) {
            if (PSO_chemo_Process.fitnessValueList[i] < PSO_chemo_Process.pBest[i]) {
                PSO_chemo_Process.pBest[i] = PSO_chemo_Process.fitnessValueList[i];
                PSO_chemo_Process.pBestLocation.set(i, PSO_chemo_Process.swarm.get(i).getLocation());
            }
        }

        // step 2 - update gBest
        int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
        if (t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
            gBest = fitnessValueList[bestParticleIndex];
            gBestLocation = swarm.get(bestParticleIndex).getLocation();
        }

        w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);

        for (int i = 0; i < SWARM_SIZE; i++) {
            double r1 = generator.nextDouble();
            double r2 = generator.nextDouble();

            Particle p = swarm.get(i);

            // step 3 - update velocity
            double[] newVel = new double[PROBLEM_DIMENSION];
            newVel[0] = (w * p.getVelocity().getPos()[0])
                    + (r1 * C1) * (pBestLocation.get(i).getLoc()[0] - p.getLocation().getLoc()[0])
                    + (r2 * C2) * (gBestLocation.getLoc()[0] - p.getLocation().getLoc()[0]);
            newVel[1] = (w * p.getVelocity().getPos()[1])
                    + (r1 * C1) * (pBestLocation.get(i).getLoc()[1] - p.getLocation().getLoc()[1])
                    + (r2 * C2) * (gBestLocation.getLoc()[1] - p.getLocation().getLoc()[1]);
            Velocity vel = new Velocity(newVel);
            p.setVelocity(vel);

            // step 4 - update location
            double[] newLoc = new double[PROBLEM_DIMENSION];
            newLoc[0] = p.getLocation().getLoc()[0] + newVel[0];
            newLoc[1] = p.getLocation().getLoc()[1] + newVel[1];
            Location loc = new Location(newLoc);
            p.setLocation(loc);
        }

        err = ProblemSet.evaluate(gBestLocation) - 0; // minimizing the functions means it's getting closer to 0

        System.out.println("ITERATION " + t + " , at particle " + bestParticleIndex );
        System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
        System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
        System.out.println("     Fitness Value: " + ProblemSet.evaluate(gBestLocation));

        t++;
        updateFitnessList();

        demo.action(swarm, 3.0, 0.5);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            // Handle the exception
        }

    }

    public static void updateFitnessList() {
        for (int i = 0; i < SWARM_SIZE; i++) {
            fitnessValueList[i] = swarm.get(i).getFitnessValue();
        }
    }

}
