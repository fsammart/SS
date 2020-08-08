import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //CliParser.parseOptions(args);



        // TODO: parse arguments from command line.
        int N = 10000; // Number of particles
        double L = 300; // Size of square side
        int M = 10; // MxM matrix
        boolean periodicContour = false;
        double interactionRadius = 10;

        List<Particle> l = getRandomParticles(N, L);
        List<Particle> l2 = new ArrayList<>(l);

        long startTime = System.currentTimeMillis();

        CIM.compute(l, M, L, periodicContour, interactionRadius);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        printResults(elapsedTime, l);
        System.out.println("Brute Force");

        startTime = System.currentTimeMillis();

        BruteForce.compute(N, l2, periodicContour, interactionRadius);

        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;

        printResults(elapsedTime, l);
    }

    public static List<Particle> getRandomParticles(int N, double L){
        Random r = new Random(System.currentTimeMillis());
        List<Particle> particles = new ArrayList<>();
        r.doubles(N , 0, L)
                .forEach(x -> particles.add(new Particle(x, r.nextDouble() * L ,0)));
        r.doubles(N , 0, L)
                .forEach(x -> particles.add(new Particle(x, r.nextDouble() * L ,0)));
        return particles;
    }

    public static void printResults(long elapsedTime, List<Particle> particles){
        System.out.println("Elapsed time: " +  elapsedTime + " ms");
        /*
        particles.stream().filter(p -> !p.getNeighbours().isEmpty()).forEach(p ->{

            System.out.println(
                    p.getId() + " (" + p.getX() + ", " + p.getY() + "): " +
                            p.getNeighbours().stream().map(Particle::getId).collect(Collectors.toList())
            );
        });
         */
    }

}
