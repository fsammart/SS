import java.util.*;
import java.util.stream.Collectors;

public class GasDiffusion2D {

    private double L;
    private double W;
    private double groove;

    // dynamic system data
    public double rightSideFraction;
    public double collisionTime;
    public double currentPressure;
    public double totalPressure;
    public List<Particle> particles;
    public static boolean equilibriumReached;

    public GasDiffusion2D(double L, final double W, double groove, List<Particle> particles) {
        this.L = L;
        this.W = W;
        this.groove = groove;
        this.particles = particles;
        this.equilibriumReached = false;
        this.totalPressure = 0;
        this.currentPressure = 0;
        this.rightSideFraction = 0;
        this.collisionTime = 0;
    }

    public void run(List<Particle> particles){
        final List<Event> minEvents;


        if (particles == null || particles.size() == 0) {
            // nothing to process;
            return;
        }

        minEvents = predictCollisions(particles);

        if (minEvents.isEmpty()) {
            return;
        }

        // Get Time for collision. Move every particle except the ones to collide.
        final double tc = minEvents.get(0).getTime();
        for(final Particle point : particles) {
            point.moveParticle(tc);
            // If particle is on the right side => add it to the right side
            if (point.x > W / 2) { // calculate rightSide after update
                rightSideFraction++;
            }
        }

        for(Event event : minEvents){
            event.execute();

            if(event instanceof WallEvent && equilibriumReached) {
                //Main.witnesses.remove(((WallEvent)event).particle.id);
            }

            // Get the event's resulting currentPressure
            currentPressure += event.getPressure();
        }
        final int N = particles.size();
        rightSideFraction /= N; // (1) division by zero avoided as noticed at that reference
        collisionTime = tc;

    }

    /**
     * Predict the next events (collisions)
     * @param points the collection of points to be checked against the given point
     */
    private List<Event> predictCollisions(final List<Particle> points) {
        List<Event> eventList = new ArrayList<>();
        double tc;

        for(int i = 0; i < points.size(); i++) {
            final Particle point = points.get(i);

            // Calculate the closest collision between the current particle and all the others
            for(int j = i + 1; j < points.size(); j++) {
                final Particle pointToCompare = points.get(j);

                tc = Equations.collisionTime(point, pointToCompare);

                if(!Double.isInfinite(tc) &&(eventList.isEmpty() || tc < eventList.get(0).time)) {
                    eventList.clear();
                    eventList.add(new ParticleCollision(tc, point, pointToCompare));
                } else if(!Double.isInfinite(tc) && !eventList.isEmpty() && Double.compare( tc , eventList.get(0).time) == 0){
                    eventList.add(new ParticleCollision(tc, point, pointToCompare));
                }
            }

            // Calculate the collision between the given point and one of the horizontal walls
            tc = Equations.timeToHitWall(point, Wall.HORIZONTAL, 0, L);

            if(!Double.isInfinite(tc) &&(eventList.isEmpty() || tc < eventList.get(0).time)) {
                eventList.clear();
                eventList.add( new WallEvent(tc, point, Wall.HORIZONTAL));
            } else if(!Double.isInfinite(tc) && !eventList.isEmpty() && Double.compare( tc , eventList.get(0).time) == 0){
                eventList.add( new WallEvent(tc, point, Wall.HORIZONTAL));
            }


            // Calculate the collision between the given point and one of the vertical walls
            tc = Equations.timeToHitWall(point, Wall.VERTICAL, 0, W);

            if(!Double.isInfinite(tc) &&(eventList.isEmpty() || tc < eventList.get(0).time)) {
                eventList.clear();
                eventList.add( new WallEvent(tc, point, Wall.VERTICAL));
            } else if(!Double.isInfinite(tc) && !eventList.isEmpty() && Double.compare( tc , eventList.get(0).time) == 0){
                eventList.add( new WallEvent(tc, point, Wall.VERTICAL));
            }

            // Calculate the collision between the given point and the middle wall
            tc = Equations.timeToHitMiddleWall(
                    point, W / 2, L, groove);

            if(!Double.isInfinite(tc) &&(eventList.isEmpty() || tc < eventList.get(0).time)) {
                eventList.clear();
                eventList.add( new WallEvent(tc, point, Wall.MIDDLE_VERTICAL));
            } else if(!Double.isInfinite(tc) && !eventList.isEmpty() && Double.compare( tc , eventList.get(0).time) == 0){
                eventList.add( new WallEvent(tc, point, Wall.MIDDLE_VERTICAL));
            }

        }

        return eventList;
    }
}
