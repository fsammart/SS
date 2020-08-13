import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.*;

public class Main {

    private static Map.Entry<Long,Long> run(int N, double L, int M, double interactionRadius, double radius) throws Exception {
        List<Particle> l ;
        l = RandomParticles.getRandomParticles(N, L, radius,radius);
        List<Particle> l2 = new ArrayList<>(l);

        long startTime = System.nanoTime();

        CIM.compute(l, M, L, false, interactionRadius);

        long endTime = System.nanoTime();
        long elapsedTimeCIM = endTime - startTime;

        startTime = System.nanoTime();

        BruteForce.compute(N, l2, false, interactionRadius);

        endTime = System.nanoTime();
        long elapsedTimeBF = endTime - startTime;

        boolean same = false;
        if(l.containsAll(l2) && l2.containsAll(l)){
            same = true;
        } else {
            throw new IllegalStateException("Wrong Prcessing");
        }

        //logResults(elapsedTimeCIM,elapsedTimeBF,same,l);
        return new AbstractMap.SimpleEntry(elapsedTimeCIM,  elapsedTimeBF);

    }

    public static void main(String[] args) throws Exception {


        double L = 20; // Size of square side

        boolean periodicContour = false;
        double interactionRadius = 1;
        double radius = 0.25;
        double areaParticle = Math.PI * radius * radius;
        int REPEAT = 5;

        FileParser.minRadius = radius;

        final String filename = "./tp1" + System.currentTimeMillis() ;
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);

        int fitParticles = (int) Math.floor((L * L)/areaParticle);

        System.out.println("N,M,D,CIM1,BF1,CIM2,BF2,CIM3,BF3,CIM4,BF4,CIM5,BF5,CIM,BF");
        for(int N = 1; N <= fitParticles; N ++ ) {
            for(int M = 1; CIM.checkLMCondition(L,M,interactionRadius); M++) {
                long totalCIM = 0;
                long totalBF = 0;
                System.out.print(N + "," + M + "," + N/(L*L) + ",");
                for(int i=0; i < REPEAT; i++) {
                    Map.Entry<Long,Long> e = run(N, L, M, interactionRadius, radius);
                    totalBF += e.getValue();
                    totalCIM += e.getKey();
                    System.out.print(e.getKey() + ",");
                    System.out.print(e.getValue() + ",");
                }
                System.out.print(totalCIM/5 + ",");
                System.out.print(totalBF/5);
                System.out.println("");
            }

        }

    }

    public static void logResults(long elapsedTimeCIM, long elapsedTimeBF,boolean same, List<Particle> particles) throws FileNotFoundException {
        final String filename = "./prueba" + System.currentTimeMillis() ;
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        PrintStream stdout = System.out;
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
        System.setOut(stdout);
    }


}
