import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //CliParser.parseOptions(args);



        // TODO: parse arguments from command line.
        int N = 1000; // Number of particles
        double L = 10; // Size of square side
        int M = 50; // MxM matrix
        boolean periodicContour = true;
        double interactionRadius = 0.1; // L/M > rc

        // TODO: check condition L/M > interactionRadius

        List<Particle> l = getRandomParticles(N, L);
        List<Particle> l2 = new ArrayList<>(l);

        System.out.println("CIM");

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

        printResults(elapsedTime, l2);

        if(l.containsAll(l2) && l2.containsAll(l)){
            System.out.println("Same Result for BF and CIM");
        }
    }

    public static List<Particle> getRandomParticles(int N, double L){
        Random r = new Random(1000);
        List<Particle> particles = new ArrayList<>();
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
