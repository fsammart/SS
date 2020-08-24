
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

public class OffLaticeSim {

    public double L;
    public int N;
    public double eta;

    public static double TWO_PI = Math.PI * 2;


    public static void simulate(double L, List<? extends Particle> particles, double eta, int time, String name) throws IOException {
        dynamicFile(particles, 0, name,L);

        for (int i = 1; i < time; i++) {

            CIM.compute(particles,L,true, 1,0);

            for(Particle p : particles){
                SpeedParticle sp = (SpeedParticle) p;

                // get new coordinates
                sp.move();
                sp.setAngle(newAngle(sp,eta));
            }

            dynamicFile(particles, i, name,L);
        }
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

    public static void dynamicFile(List<? extends Particle> l, int time, String name, double L) throws IOException {
        File file = new File(name + "-" + time + ".txt");
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
            writer.print(sp.getX() + " " + sp.getY() + " " + vx + " " + vy + " " + sp.angle);
        });

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);

        }
    }
}
