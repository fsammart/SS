import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomParticles {

    static Random r = new Random(System.currentTimeMillis());
    private static int MAX_COLLISIONS = 100000;

    static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + (max - min) * r.nextDouble();
    }


    
    public static List<SpeedParticle> getRandomParticles(int N, double L){
        List<SpeedParticle> particles = new ArrayList<>(N);

        do {
            SpeedParticle randomParticle = generateParticle(L);
            particles.add(randomParticle);

        } while (particles.size() < N );

        return particles;
    }

    private static SpeedParticle generateParticle(double L) {
        return new SpeedParticle(
                randomDouble(0,L),
                randomDouble(0,L),
                0, 0.03, randomDouble(-Math.PI, Math.PI), L);
    }

    private static boolean collides(Particle particle, List<Particle> particles) {
        long count;

        return particles.stream()
                .anyMatch(particle1 ->
                        particle1.distanceTo(particle) < particle1.getRadius() + particle.getRadius());
    }
}
