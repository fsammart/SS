import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        CliParser.parse(args);

        // TODO: parse arguments from command line.
        int N = CliParser.N; // Number of particles
        double L = CliParser.L; // Size of square side
        int M = CliParser.M; // MxM matrix
        boolean periodicContour = CliParser.periodicContour;
        double interactionRadius = CliParser.interactionRadius;

        if(!CIM.checkLMCondition(L,M,interactionRadius)){
            throw new IllegalArgumentException("L/M condition");
        }

        List<Particle> l ;

        if(CliParser.random){
            l = RandomParticles.getRandomParticles(N, L, CliParser.rMin,CliParser.rMax);
        } else {
            l = FileParser.particleList;
        }

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
        assert(same);
        logResults(elapsedTimeCIM, elapsedTimeBF, same, l);
    }

    public static void logResults(long elapsedTimeCIM, long elapsedTimeBF,boolean same, List<Particle> particles) throws FileNotFoundException {
        System.out.println("Output logged to file");
        final String filename = "./" + CliParser.outputFile;
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);
//        System.out.println("Time CIM (ms) ->" + String.valueOf(elapsedTimeCIM));
//        System.out.println("Time BF (ms) ->" + String.valueOf(elapsedTimeBF));
//        System.out.println("Result is equal to BF ->" + String.valueOf(same) );
        particles.forEach( particle ->
                System.out.println(
                        particle.getId() +
                                particle.getNeighbours().stream()
                                        .map(Particle::getId)
                                        .map(String::valueOf)
                                        .reduce("", (accum,x) ->  accum + "," + x)
                )
        );
    }

}
