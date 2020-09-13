import java.util.Set;
import java.util.TreeSet;

public class Particle implements Comparable<Particle>{
    public static double v;
    // Use public for efficiency reasons
    public double x, y;
    public final double radius;
    public int id;
    public static int ID_SEQUENCE = 1;
    public double vx;
    public double vy;
    public double mass;

    public Particle(double radius,double mass) {
        this.radius = radius;
        this.id = ID_SEQUENCE++;
        this.mass = mass;
    }

    public double distanceTo(final Particle particle) {
        return Math.hypot(x - particle.x, y - particle.y)
                - radius
                - particle.radius;
    }

    public void moveParticle(final double time) {
        x = x + vx * time;
        y= y + vy * time;
    }

    public double kineticEnergy() {

        return 1/2.0d * mass * Math.pow(v, 2);
    }

    public double getSpeed() {
        return Math.hypot(vx, vy);
    }

    public int compareTo(Particle o) {
        return id - o.id;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}