import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParticleCollision extends Event{
    private static final int X = 0;
    private static final int Y = 1;

    private final Particle p1, p2;

    public ParticleCollision(final double time, final Particle p1, final Particle p2) {
        super(time);
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Elastic collision
     */
    @Override
    public void  execute() {

        p1.moveParticle(time);
        p2.moveParticle(time);

        final double sigma = p1.radius + p2.radius;
        final double[] deltaR = {
                p2.x - p1.x,
                p2.y - p1.y
        };
        final double[] deltaV = {
                p2.vx - p1.vx,
                p2.vy - p1.vy
        };

        final double deltaV_deltaR = deltaV[X] * deltaR[X] + deltaV[Y] * deltaR[Y];


        final double j = (2 * p1.mass * deltaV_deltaR)/
                (sigma * (p1.mass / p2.mass + 1));

        final double jx = j * deltaR[X] / sigma;
        final double jy = j * deltaR[Y] / sigma;

        final double moduleBefore =
                Math.hypot(p1.vx - p2.vx, p1.vy - p2.vy);

        final double newVx1 = p1.vx + jx / p1.mass;
        final double newVy1 = p1.vy + jy / p1.mass;
        final double newVx2 = p2.vx - jx / p2.mass;
        final double newVy2 = p2.vy - jy / p2.mass;

        p1.vx = newVx1;
        p1.vy = newVy1;
        p2.vx = newVx2;
        p2.vy = newVy2;

        final double moduleAfter =
                Math.hypot(p1.vx - p2.vx,p1.vy - p2.vy);

        if (Math.abs(moduleAfter - moduleBefore) >= 1e-6) {
            throw new IllegalStateException("Kinetic energy is being modified on a particle colision.");
        }

    }

    @Override
    public double getPressure() {
        return 0; // no pressure at a particle's collision
    }

    @Override
    public boolean participates(Particle p) {
        return p1.equals(p) || p2.equals(p);
    }

    @Override
    public List<Particle> getParticipants() {
        return List.of(p1,p2);
    }
}
