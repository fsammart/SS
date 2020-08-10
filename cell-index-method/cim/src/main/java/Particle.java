import java.util.Set;
import java.util.TreeSet;

public class Particle implements Comparable<Particle>{

    // Use public for efficiency reasons
    public double x, y;
    public final double radius;
    public Set<Particle> neighbours; // other particles in same cell
    public int id;
    public int cellX, cellY;
    public static int ID_SEQUENCE = 1;

    public Particle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.id = ID_SEQUENCE++;
        neighbours = new TreeSet<>();
    }

    public double distanceTo(final Particle particle) {
        return Math.hypot(x - particle.x, y - particle.y)
                - radius
                - particle.radius;
    }

    public double contourDistanceTo(final Particle particle) {
        final double L = CIM.L;
        final double mcx = Math.abs(x - particle.x);
        final double mcy = Math.abs(y - particle.y);
        final double r = radius + particle.radius;

        // TODO: check for optimizations
        double dx = mcx;
        double dy = mcy;
        if ((L - mcx - r) < (mcx - r)) dx = L - mcx;
        if ((L - mcy - r) < (mcy - r)) dy = L - mcy;
        return Math.hypot(dx, dy) - r;
    }


    public int getId() {
        return id;
    }

    public void addNeighbour(Particle particle) {
        neighbours.add(particle);
    }

    public Set<Particle> getNeighbours() {
        return neighbours;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public int getCellX() {
        return cellX;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
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
}