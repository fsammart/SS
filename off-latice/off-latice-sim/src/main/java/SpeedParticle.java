import java.util.TreeSet;

public class SpeedParticle extends Particle {

    // public for efficiency
    public double vel, angle;
    public double L;

    public SpeedParticle(double radius, double property) {
        super(radius,property);
    }

    public SpeedParticle(double radius, double property,double vel, double angle, double L) {
        super(radius,property);
        this.vel = vel;
        this.angle = angle;
        this.L = L;
    }
    public SpeedParticle(double x, double y, double radius,double vel, double angle, double L) {
        super(x, y, radius);
        this.vel = vel;
        this.angle = angle;
        this.L = L;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public void setVel(double vel) {
        this.vel = vel;
    }

    public void move(){
        // TODO: check
        this.x = (this.x + this.vel * Math.cos(this.angle) + L) % L;
        this.y = (this.y + this.vel * Math.sin(this.angle) + L) % L;
    }
}
