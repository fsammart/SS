import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        CliParser.parse(args);

        int N = CliParser.N; // Number of particles
        double L = CliParser.L; // Size of square side
        int M = CliParser.M; // MxM matrix
        boolean periodicContour = CliParser.periodicContour;
        double interactionRadius = CliParser.interactionRadius;
        String strategy = CliParser.strategy;

        if(!CIM.checkLMCondition(L,M,interactionRadius)){
            throw new IllegalArgumentException("L/M condition");
        }

        List<Particle> l ;

        if(CliParser.random){
            l = RandomParticles.getRandomParticles(N, L, CliParser.rMin,CliParser.rMax);
            generateFiles(l, N, L);
        } else {
            l = FileParser.particleList;
        }



        List<Particle> l2 = new ArrayList<>(l);

        switch(strategy){
            case "CIM": {
                CIM.compute(l, M, L, periodicContour, interactionRadius);
            }break;

            case "BF": {
                BruteForce.compute(N, l2, periodicContour, interactionRadius);
            }
        }

        logResults(l);
    }

    public static void generateFiles(List<Particle> particles, int N, double L) throws FileNotFoundException {

        generateStaticFile(particles, N, L);
        generateDynamicFile(particles, N, L);

    }

    private static void generateStaticFile(List<Particle> particles, int N, double L) throws FileNotFoundException {

        File file = new File("files/staticFiles/static");
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);
        System.out.println(N);
        System.out.println(L);
        particles.forEach(p -> {
            System.out.println(p.radius + " " + String.valueOf(1.0));
        });

    }

    private static void generateDynamicFile(List<Particle> particles, int N, double L) throws FileNotFoundException{

        File file = new File("files/dynamicFiles/dynamic");
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);
        System.out.println(0);
        particles.forEach(p -> {
            System.out.println(p.x + "  " + p.y);
        });


    }

    public static void logResults(List<Particle> particles) throws FileNotFoundException {
        final String filename = "./" + CliParser.outputFile;
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);
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
