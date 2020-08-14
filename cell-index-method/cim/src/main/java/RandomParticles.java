

import java.util.*;

public class RandomParticles {

    static Random r = new Random(System.currentTimeMillis());
    private static int MAX_COLLISIONS = 50000;

    private List<Particle> randomParticles ;

    public RandomParticles(double L, double radius){
        this.randomParticles = matrixRandomParticles(L,radius);
    }


    public int getMatrixRandomParticlesSize(){
        return randomParticles.size();
    }

    public List<Particle> getRandomParticlesFromMatrix(int size){
        Collections.shuffle(randomParticles);
        int i = 1;
        List<Particle> particles = randomParticles.subList(0,size);
        for(Particle p: particles){
            p.id = i++;
        }
        return particles;
    }
    static List<Particle> matrixRandomParticles(double L, double radius){
        List<Particle> particles = new ArrayList<>();
        Particle p = new Particle(radius,radius,radius);
        double x = radius;
        double y = radius;

        particles.add(p);

        assert 2 * radius < L;

        while(x + 2 * radius < L){
            while(y + 2 * radius < L){
                y += 2 * radius;
                p = new Particle(x,y,radius);
                particles.add(p);
            }
            y = radius;
            x += 2* radius;
        }

        return particles;
    }

    static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + (max - min) * r.nextDouble();
    }

    private static Particle mutateParticleCollides(Particle initialParticle, List<Particle> particles){
        Particle mutate = initialParticle;
        double x = initialParticle.x;
        double y = initialParticle.y;
        if(!collides(mutate, particles)){
            return mutate;
        }
        mutate = new Particle(initialParticle.y, initialParticle.x,initialParticle.radius);

        if(!collides(mutate, particles)){
            return mutate;
        }

        mutate = new Particle((x+y)/2, (x+y)/2,initialParticle.radius);

        if(!collides(mutate, particles)){
            return mutate;
        }

        return null;
    }

    public static List<Particle> getRandomParticles(int N, double L, double radiusMin, double radiusMax){
        List<Particle> particles = new ArrayList<>(N);

        if(radiusMin == radiusMax){
            RandomParticles r = new RandomParticles(L,radiusMax);
            return r.getRandomParticlesFromMatrix(N);
        }
        int collidingCounter = 0;

        do {
            Particle randomParticle = generateParticle(L, radiusMin,radiusMax);

            Particle p = mutateParticleCollides(randomParticle, particles);

            if(p != null) {
                particles.add(p);
                collidingCounter = 0;
            } else {
                collidingCounter++;
            }

        } while (particles.size() < N && collidingCounter < MAX_COLLISIONS);

        if(collidingCounter >= MAX_COLLISIONS){
            throw new IllegalStateException("Cannot create array");
        }

        int i = 1;
        for(Particle p: particles){
            p.id = i++;
        }
        return particles;
    }

    private static Particle generateParticle(double L, double radiusMin, double radiusMax) {
        double r = randomDouble(radiusMin,radiusMax);
        return new Particle(
                randomDouble(r, L - r),
                randomDouble(r ,L - r),
                r);
    }

    private static boolean collides(Particle particle, List<Particle> particles) {
        long count;

        return particles.stream()
                .anyMatch(particle1 ->
                        particle1.distanceTo(particle) < particle1.getRadius() + particle.getRadius());
    }
}
