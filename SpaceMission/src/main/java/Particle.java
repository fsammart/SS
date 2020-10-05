import java.util.List;

public class Particle {

    // rx has derivatives from 0 to 5 of position.
    public List<Double> rx;
    public List<Double> ry;


    public double fx;
    public double fy;

    public double radius;
    public double mass;
    public String name;

    public Particle(List<Double> rx, List<Double> ry, double radius, double mass, String name) {
        this.rx = rx;
        this.ry=ry;
        this.radius = radius;
        this.mass = mass;
        this.name = name;
    }

    public double kineticEnergy() {
        return 0.5 * mass * (rx.get(1)*rx.get(1) + ry.get(1)*ry.get(1));
    }

    public double distance_sq(Particle other) {
        double dx = other.rx.get(0) - this.rx.get(0);
        double dy = other.ry.get(0) - this.ry.get(0);

        return Math.pow(dx, 2) + Math.pow(dy, 2);
    }

    public double angle(Particle other) {
        double dx = other.rx.get(0) - this.rx.get(0);
        double dy = other.ry.get(0) - this.ry.get(0);

        return Math.atan2(dy, dx);
    }
}
