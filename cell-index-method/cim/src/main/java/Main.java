import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        CliParser.parse(args);

        // TODO: parse arguments from command line.
        int N = CliParser.N; // Number of particles
        double L = CliParser.L; // Size of square side
        int M = CliParser.M; // MxM matrix
        boolean periodicContour = CliParser.periodicContour;
        double interactionRadius = CliParser.interactionRadius;

        String output = CliParser.outputFile;

        if(!CIM.checkLMCondition(L,M,interactionRadius)){
            throw new IllegalArgumentException("L/M condition");
        }

        List<Particle> l = getRandomParticles(N, L);
        List<Particle> l2 = new ArrayList<>(l);

        System.out.println("CIM");

        long startTime = System.currentTimeMillis();

        CIM.compute(l, M, L, periodicContour, interactionRadius);

        long endTime = System.currentTimeMillis();
        long elapsedTimeCIM = endTime - startTime;


        System.out.println("Brute Force");

        startTime = System.currentTimeMillis();

        BruteForce.compute(N, l2, periodicContour, interactionRadius);

        endTime = System.currentTimeMillis();
        long elapsedTimeBF = endTime - startTime;

        boolean same = false;
        if(l.containsAll(l2) && l2.containsAll(l)){
            same = true;
        }
        logResults(elapsedTimeCIM, elapsedTimeBF, same, l);
    }

    public static List<Particle> getRandomParticles(int N, double L){
        Random r = new Random(1000);
        List<Particle> particles = new ArrayList<>();
        r.doubles(N , 0, L)
                .forEach(x -> particles.add(new Particle(x, r.nextDouble() * L ,0)));

        return particles;
    }

    public static void logResults(long elapsedTimeCIM, long elapsedTimeBF,boolean same, List<Particle> particles) throws FileNotFoundException {
        System.out.println("Output logged to file");
        final String filename = "./" + CliParser.outputFile;
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);
        System.out.println("Time CIM (ms) ->" + String.valueOf(elapsedTimeCIM));
        System.out.println("Time BF (ms) ->" + String.valueOf(elapsedTimeBF));
        System.out.println("Result is equal to BF ->" + String.valueOf(same) );
        particles.forEach( particle ->
                System.out.println(
                        particle.getId() + " " +
                                particle.getNeighbours().stream().map(Particle::getId).collect(Collectors.toList())
                )
        );
    }

}
