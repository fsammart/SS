import java.util.Set;
import java.util.TreeSet;

public class Particle implements Comparable<Particle>{

    // Use public for efficiency reasons
    public double x, y;
    public  double radius;
    public Set<Particle> neighbours; // other particles in same cell
    public int id;
    public int cellX, cellY;
    public double property;
    public static int ID_SEQUENCE = 1;

    public Particle(double radius, double property){
        this.radius = radius;
        this.property = property;
        neighbours = new TreeSet<>();
        this.id = ID_SEQUENCE++;
    }
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

    public double contourDistanceTo(final Particle particle, double L, double W) {
        final double directDistancex = Math.abs(x - particle.x);
        final double directDistancey = Math.abs(y - particle.y);
        final double r = radius + particle.radius;

        double conoturDistancex = directDistancex;
        double contourDistancey = directDistancey;
        /* check if particles are near through contour */
        if ((L - directDistancex - r) < (directDistancex - r)){
            conoturDistancex = L - directDistancex;
        }
        if ((L - directDistancey - r) < (directDistancey - r)){
            contourDistancey = L - directDistancey;
        }
        return Math.hypot(conoturDistancex, contourDistancey) - r;
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

    public double getProperty() {
        return property;
    }
}