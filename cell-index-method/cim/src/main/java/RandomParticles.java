import java.util.*;

public class RandomParticles {

    static Random r = new Random(System.currentTimeMillis());
    private static int MAX_COLLISIONS = 100000;

    static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + (max - min) * r.nextDouble();
    }

    public static List<Particle> getRandomParticles(int N, double L, double radiusMin, double radiusMax){
        List<Particle> particles = new ArrayList<>(N);
        int collidingCounter = 0;

        do {
            Particle randomParticle = generateParticle(L, radiusMin,radiusMax);

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

    private static Particle generateParticle(double L, double radiusMin, double radiusMax) {
        return new Particle(
                randomDouble(0,L),
                randomDouble(0,L),
                randomDouble(radiusMin,radiusMax));
    }

    private static boolean collides(Particle particle, List<Particle> particles) {
        long count;

        return particles.stream()
                .anyMatch(particle1 ->
                        particle1.distanceTo(particle) < particle1.getRadius() + particle.getRadius());
    }
}
