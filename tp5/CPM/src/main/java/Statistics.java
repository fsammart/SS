import java.io.IOException;
import java.util.List;

public class Statistics {

    public static void main(String[] args) throws IOException {

        double L = 15;
        double W = 7;
        double maxRadius = 0.2;
        double minRadius = 0.1;
        double x_goal = 15;
        double y_goal = 3.5;
        double vMin = 0.8;
        double vMax = 1.3;

        double ap = 2;
        double bp = 0.5;
        double beta = 1;
        double tau = 0.5;

        for(ap = 0; ap <= 5; ap += 0.1) {
            for (int t = 0; t < 500; t++) {
                CPM cpm = new CPM(L, W, minRadius, maxRadius, x_goal, y_goal, vMax, ap, bp, beta, tau);
                List<CPMParticle> ps = CPMRandomParticles.getObstacles(15, L, W, vMin, vMax, maxRadius);

                CPMParticle particle = new CPMParticle(0, 3.5, maxRadius);
                particle.is_obstacle = false;
                particle.vel = 0;

                cpm.simulate(particle, 0.05, 0.05, ps, 40, false);
            }
        }


    }
}
