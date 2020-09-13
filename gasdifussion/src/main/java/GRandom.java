import java.util.*;

public class GRandom {

    private static Random r = new Random(System.currentTimeMillis());

    private static int MAX_COLLISIONS = 1000;

    static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + (max - min) * r.nextDouble();
    }

    public static List<Particle> getRandomParticles(int N, double L, double radius, double mass, double v, double W){
        List<Particle> particles = new ArrayList<>(N);
        int collisions = 0;
        do {
            Particle randomParticle = generateParticle(L, radius, mass, v, W);
            if(!collides(randomParticle, particles)) {
                particles.add(randomParticle);
                collisions = 0;
            } else {
                collisions++;
            }

        } while (particles.size() < N &&  collisions < MAX_COLLISIONS );

        return particles;
    }

    private static Particle generateParticle(double L, double radius, double mass, double v, double W) {

        double vx = randomDouble(-v, v);
        double vy = Math.sqrt(v*v - vx*vx);
        vy = r.nextBoolean()? -vy: vy;

        double min = radius;
        double max = (W/ 2) - radius;
        double x = randomDouble(min,max);

        max = L - radius;
        double y =  randomDouble(min, max);

        Particle p = new Particle(radius, mass);
        p.vx = vx;
        p.vy = vy;
        p.x = x;
        p.y = y;
        return p;
    }


    private static boolean collides(Particle particle, List<Particle> particles) {
        boolean collides;

        collides = particles.stream()
                .anyMatch(particle1 ->
                        Math.pow(particle1.x - particle.x, 2) + Math.pow(particle1.y - particle.y, 2) < Math.pow(particle1.radius + particle.radius, 2));

        return collides;
    }
}
