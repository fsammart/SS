
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OffLaticeSim {

    public static double L;
    public static int N;
    public static double eta;
    public static List<Double> vaEvolution ;

    public static double TWO_PI = Math.PI * 2;
    public static double vel;


    public static void   simulate(double L, List<? extends Particle> particles, double eta, int time, String name) throws IOException {
        dynamicFile(particles, 0, name,L);
        vaEvolution = new ArrayList<>(time + 1);
        double vx;
        double vy;
        double velAvg;
        vel = ((SpeedParticle)particles.get(0)).vel;
        N = particles.size();
        Map<SpeedParticle, Double> newAngles = new ConcurrentHashMap<>();

        for (int i = 0; i < time; i++) {
            newAngles.clear();
            long start = System.currentTimeMillis();
            CIM.compute(particles,L,true, 1,0);
            long elapsed = System.currentTimeMillis() - start;
            vx = 0;
            vy = 0;
            particles.parallelStream().forEach(p -> {
                SpeedParticle sp = (SpeedParticle) p;
                // for va analysis


                // get new coordinates
                sp.move();
                newAngles.put(sp, newAngle(sp,eta));
            });

            //update angles after calculation

            for (Particle p: particles) {

                SpeedParticle sp = (SpeedParticle) p;
                vx += sp.vel * Math.cos(sp.angle);
                vy += sp.vel * Math.sin(sp.angle);

                sp.angle = newAngles.get(sp);
                p.neighbours.clear();

            }

            velAvg = Math.hypot(vx , vy) / (N * vel);
            vaEvolution.add(velAvg);
            dynamicFile(particles, i + 1, name,L);
        }

        printStatistics(name);
    }

    private static double newAngle(SpeedParticle p, double eta) {

        double sinSum = Math.sin(p.angle);
        double cosSum = Math.cos(p.angle);
        Set<Particle> neighbours = p.getNeighbours();

       for(Particle n : neighbours) {
            SpeedParticle sp = (SpeedParticle) n;
            sinSum += Math.sin(sp.angle);
            cosSum += Math.cos(sp.angle);
        }

       /* plus 1 for the chosen particle*/
        // TODO: check
       double sinAvg = sinSum/(neighbours.size() + 1);
       double cosAvg = cosSum/(neighbours.size() + 1);

       double newAngle = Math.atan2(sinAvg, cosAvg);

       double noise = RandomParticles.randomDouble(-eta/2,eta/2);
       // TODO: check
       return normalizeAngle(newAngle + noise);

    }

    public static double normalizeAngle(double angle) {
        return angle - TWO_PI * Math.floor((angle + Math.PI) / TWO_PI);
    }

    public static void printStatistics(String name) throws IOException {
        File file = new File(name + "/statistics" + ".txt");
        if(file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        PrintStream syso = System.out;
        System.setOut(ps);

        System.out.println(CliParser.N);
        System.out.println(CliParser.L);
        System.out.println(CliParser.eta);
        System.out.println(CliParser.time);

        vaEvolution.forEach(System.out::println);
        System.setOut(syso);

    }

    public static void dynamicFile(List<? extends Particle> l, int time, String name, double L) throws IOException {
        File file = new File(name +"/t" + "-" + time + ".txt");
        if(file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.format("%d\n\n", l.size() + 3);
            writer.println("0 0 0 0 0");
            writer.println(L + " 0 0 0 0");
            writer.print("0 " + L+ " 0 0 0");

        l.forEach(p -> {
            writer.println("");
            SpeedParticle sp = (SpeedParticle) p;
            double vx = sp.vel * Math.cos(sp.angle);
            double vy = sp.vel * Math.sin(sp.angle);

            writer.print(String.format(Locale.US,"%.3f", sp.getX()) + " "
                    + String.format(Locale.US,"%.3f", sp.getY()) + " "
                    + String.format(Locale.US,"%.3f", vx) + " "
                    + String.format(Locale.US,"%.3f", vy) + " " + sp.angle);
        });

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);

        }
    }
}
