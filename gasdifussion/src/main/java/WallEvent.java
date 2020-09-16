import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WallEvent extends Event{
    public final Particle particle;
    private Wall wall;

    public WallEvent(final double time, final Particle Particle, final Wall wall) {
        super(time);
        this.particle = Particle;
        this.wall = wall;
    }

    @Override
    public void execute() {
        if(wall == Wall.VERTICAL || wall == Wall.MIDDLE_VERTICAL) {
            particle.vx = particle.vx * (-1);
        } else if (wall == Wall.HORIZONTAL) {
            particle.vy = particle.vy * (-1);
        }
    }

    @Override
    public double getPressure() {
        if (wall.getLength() > 0) {
            if(wall == Wall.HORIZONTAL)
                return 2 * particle.mass * Math.abs(particle.vy) / wall.getLength();
            else
            return 2 * particle.mass * Math.abs(particle.vx) / wall.getLength();
        }
        return -1;
    }

    @Override
    public boolean participates(Particle p) {
        return particle.equals(p);
    }

    @Override
    public List<Particle> getParticipants() {
        return List.of(particle);
    }
}
