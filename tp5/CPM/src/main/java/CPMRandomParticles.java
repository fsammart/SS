import java.util.*;

public class CPMRandomParticles {

    static Random r = new Random(System.currentTimeMillis());
    private static int MAX_COLLISIONS = 100000;

    static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + (max - min) * r.nextDouble();
    }

    public static List<CPMParticle> getObstacles(int N, double L, double W, double vel_min, double vel_max, double radius){
        List<CPMParticle> particles = new ArrayList<>(N);
        double step = (L -2)/(N);

        for(double p = 1; p < L-1; p += step){
            CPMParticle randomParticle = generateParticleFixed(p,W, radius,radius);
            randomParticle.is_obstacle = true;
            randomParticle.vel = randomDouble(vel_min, vel_max);
            if(r.nextBoolean()){
                randomParticle.angle = Math.PI/2;
            }else{
                randomParticle.angle = -Math.PI/2;
            }
            particles.add(randomParticle);
        }

        return particles;
    }

    public static List<CPMParticle> getRandomParticles(int N, double L, double W, double radiusMin, double radiusMax, double vel){
        List<CPMParticle> particles = new ArrayList<>(N);
        int collidingCounter = 0;

        do {
            CPMParticle randomParticle = generateParticle(L,W, radiusMin,radiusMax);
            randomParticle.is_obstacle = true;
            if(r.nextBoolean()){
                randomParticle.angle = Math.PI/2;
            }else{
                randomParticle.angle = -Math.PI/2;
            }
            randomParticle.vel = vel;

            if(!collides(randomParticle, particles)) {
                particles.add(randomParticle);
                collidingCounter = 0;
            } else {
                collidingCounter++;
            }

        } while (particles.size() < N && collidingCounter < MAX_COLLISIONS);

        if(collidingCounter >= MAX_COLLISIONS){
            throw new IllegalStateException("Cannot create array");
        }
        return particles;
    }

    private static CPMParticle generateParticleFixed(double x, double W,double radiusMin, double radiusMax) {
        return new CPMParticle(
                // TODO: fix margin
                x,
                randomDouble(0,W),
                randomDouble(radiusMin,radiusMax));
    }

    private static CPMParticle generateParticle(double L, double W,double radiusMin, double radiusMax) {
        return new CPMParticle(
                // TODO: fix margin
                randomDouble(1,L -1),
                randomDouble(0,W),
                randomDouble(radiusMin,radiusMax));
    }

    private static boolean collides(CPMParticle particle, List<CPMParticle> particles) {
        long count;

        return particles.stream()
                .anyMatch(particle1 ->
                        particle1.distanceTo(particle) < particle1.getRadius() + particle.getRadius());
    }


}