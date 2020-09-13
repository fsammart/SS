import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

public class Equations {

    static double distanceBetween(final Particle p1, final Particle p2) {
        return Math.hypot(p2.x - p1.x, p2.y - p1.y) - p1.radius - p2.radius;
    }

    /**
     * Calculates collision time,
     * returns Double.infinity in d<0  & deltav * deltar >=0
     */
    public static double collisionTime(final Particle p1, final Particle p2) {
        // Components of Delta v
        final double deltaVx = p2.vx - p1.vx;
        final double deltaVy = p2.vy - p1.vy;

        // Components of Delta r
        final double deltaRx = p2.x - p1.x;
        final double deltaRy = p2.y - p1.y;

        // deltaV * deltaR
        final double vr = deltaVx * deltaRx + deltaVy * deltaRy;

        if(vr >= 0) {
            return Double.POSITIVE_INFINITY;
        }

        final double rr = pow(deltaRx, 2) + pow(deltaRy, 2);
        final double vv = pow(deltaVx, 2) + pow(deltaVy, 2);


        final double sigma = p1.radius + p2.radius;

        //final double d = pow(vr, 2) - pow(vv, 2) * (pow(rr, 2) - pow(σ,2));
        final double d = pow(vr, 2) - vv * (rr - pow(sigma,2));

        if(d < 0) {
            return Double.POSITIVE_INFINITY;
        }

        return -1 * (vr + sqrt(d)) / (vv);
    }

    /**
     * Calculates the time when it collides with a horizontal wall
     * @param particle a given particle
     * @param wall The direction of the wall
     * @param negativeBound the position of the wall in case velocity is negative
     * @param positiveBound the position of the wall in case velocity is positive
     * @return the time to reach the wall
     */
    public static double timeToHitWall(final Particle particle, final Wall wall,
                                       final double negativeBound, final double positiveBound) {
        double v = 0.0;
        double r = 0.0;

        switch(wall){
            case HORIZONTAL:{
                v = particle.vy;
                r = particle.y;
            } break;
            case VERTICAL:{
                v = particle.vx;
                r = particle.x;
            }break;
        }

        if(v == 0) {
            return Double.POSITIVE_INFINITY;
        }
        if(v < 0) {
            return (negativeBound + particle.radius - r) / v;
        }
        return (positiveBound - particle.radius - r) / v;
    }

    public static double timeToHitMiddleWall(final Particle particle, final double xWall, final double wallHeight, final double opening) {
        double tc, newY, openingUpperLimit, openingLowerLimit;
        final double v = particle.vx;
        final double r = particle.x;

        if(v == 0) {
            return Double.POSITIVE_INFINITY;
        }

        // Check that if particle is travelling left, it's currently positioned to the right of the wall. (Same if its going left)
        if(v < 0) {
            if( (r-particle.radius) > xWall){ // r+radio is used to make sure the particle isn't in contact with the wall
                tc = (xWall + particle.radius - r) / v;
            } else{
                return Double.POSITIVE_INFINITY;
            }
        } else{
            if( (r+particle.radius) < xWall){
                tc = (xWall - particle.radius - r) / v;
            } else{
                return Double.POSITIVE_INFINITY;
            }
        }

        // check what happens at the new y position
        newY = particle.y + particle.vy * tc;
        openingUpperLimit = (wallHeight/2) + opening/2;
        openingLowerLimit = (wallHeight/2) - opening/2;

        if (     newY-particle.radius > openingLowerLimit
                && newY+particle.radius < openingUpperLimit){
            // If the particle is travelling towards the opening, then there's no collision,
            // and Infinity is returned
            // TODO: there should be other particle more close to impact.
            tc = Double.POSITIVE_INFINITY;
        }

        return tc;
    }
}

