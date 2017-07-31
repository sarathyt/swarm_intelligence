package pso_basic_functions;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */
// this is the heart of the PSO program
// the code is for 2-dimensional space problem
// but you can easily modify it to solve higher dimensional space problem
import animation.ScatterPlotMove;
import java.awt.EventQueue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.JFrame;

public class PSO_chemo_Process implements PSOConstants {

    public static Vector<Particle> swarm = new Vector<Particle>();
    public static double[] pBest = new double[SWARM_SIZE];
    public static Vector<Location> pBestLocation = new Vector<Location>();
    public static double gBest;
    public static Location gBestLocation;
    public static double[] fitnessValueList = new double[SWARM_SIZE];
    static Timer timer;
    static int t = 0;
    static ScatterPlotMove demo;

    Random generator = new Random();
    String title = "Nanobots";

    public void execute() {
        timer = new Timer();
        initializeSwarm();
        Particle.updateFitnessList();
        demo = new ScatterPlotMove(title, swarm, 3.0, 0.5);
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.pack();
        demo.setLocationRelativeTo(null);
        demo.setVisible(true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.print(e.getMessage());
        }

        for (int i = 0; i < SWARM_SIZE; i++) {
            pBest[i] = fitnessValueList[i];
            pBestLocation.add(swarm.get(i).getLocation());
//            EventQueue.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    ScatterPlotMove demo = new ScatterPlotMove(title, swarm, 3.0, 0.5);
//                    demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    demo.pack();
//                    demo.setLocationRelativeTo(null);
//                    demo.setVisible(true);
//                }
//            });

        }

        int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
        if (t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
            gBest = fitnessValueList[bestParticleIndex];
            gBestLocation = swarm.get(bestParticleIndex).getLocation();
        }

        for (int x = 0; x < SWARM_SIZE; x++) {
            timer.schedule(swarm.get(x), 1000, 5000);
        }

        System.out.println("\nSolution at iteration " + (t) + ", Particle " + bestParticleIndex +" the solutions is:");
        System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
        System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
    }

    public void initializeSwarm() {
        Particle p;
        for (int i = 0; i < SWARM_SIZE; i++) {
            p = new Particle();

            // randomize location inside a space defined in Problem Set
            double[] loc = new double[PROBLEM_DIMENSION];
            loc[0] = ProblemSet.LOC_X_LOW + generator.nextDouble() * (ProblemSet.LOC_X_HIGH - ProblemSet.LOC_X_LOW);
            loc[1] = ProblemSet.LOC_Y_LOW + generator.nextDouble() * (ProblemSet.LOC_Y_HIGH - ProblemSet.LOC_Y_LOW);
            Location location = new Location(loc);

            // randomize velocity in the range defined in Problem Set
            double[] vel = new double[PROBLEM_DIMENSION];
            vel[0] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
            vel[1] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
            Velocity velocity = new Velocity(vel);

            p.setLocation(location);
            p.setVelocity(velocity);
            swarm.add(p);
        }
    }

}
