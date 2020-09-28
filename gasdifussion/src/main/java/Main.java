

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;

public class Main {

    public static final int SYSTEM_PARTICLES_INDEX = 0;
    public static final int KINETIC_ENERGY_INDEX = 1;
    public static double MAX_TIME_AFTER_EQUILIBRIUM = 100;

    public static double  systemTime;
    public static int iteration;
    public static Map<Integer, Particle> witnesses;
    public static boolean equilibriumReached;
    public static GasDiffusion2D gasDiffusion;
    public static String name ;

    public static void main(String[] args) throws IOException {
        CliParser.parse(args);
        equilibriumReached = false;
        systemTime = 0;
        iteration = 0;
        CliParser.dynamicOutput = true;

        name = String.valueOf(System.currentTimeMillis());


        List<Particle> particles = GRandom.
                getRandomParticles(CliParser.N,CliParser.L,CliParser.radius,
                        CliParser.m, CliParser.v, CliParser.W);

        witnesses = new TreeMap<>();
        particles.forEach(point -> witnesses.put(point.id, point));

        Wall.HORIZONTAL.setLength(CliParser.W);
        Wall.VERTICAL.setLength(CliParser.L);
        Wall.MIDDLE_VERTICAL.setLength((CliParser.L - CliParser.g)/2.0);


        gasDiffusion = new GasDiffusion2D(CliParser.L, CliParser.W, CliParser.g, particles);

        runGasDiffusion(particles, false);

        int eqIteration = iteration;
        double eqSystemTime = systemTime;
        gasDiffusion.totalPressure += gasDiffusion.currentPressure;
        gasDiffusion.currentPressure = 0;
        gasDiffusion.equilibriumReached = true;
        equilibriumReached = true;
        CliParser.dynamicOutput = true;
        runGasDiffusion( particles, true);

        generateOutputDataFile(eqIteration, eqSystemTime, systemTime - eqSystemTime);

    }

    public static List<Particle> runGasDiffusion(List<Particle> points,
                                                  boolean equilibriumReached) throws IOException {
        double tc;
        double timeAfterEquilibrium = 0;
        double delta;

        do {
            gasDiffusion.run(points);

            points = gasDiffusion.particles;
            if (points.isEmpty()) { // there was no collision indeed => end with a log message
                System.out.println("There is no collision at any time with the given parameters.\n" +
                        "Please check that the system is properly set up.");
                exit(1);
            }

            tc = gasDiffusion.collisionTime; // Time left to reach dt2

            systemTime += tc;

            generateOutputDataFile(iteration, systemTime); // save to file the current configuration
            iteration++;
            gasDiffusion.totalPressure += gasDiffusion.currentPressure;
            gasDiffusion.currentPressure = 0;


            if(equilibriumReached)
                timeAfterEquilibrium += tc;

        } while (!equilibriumReached ? gasDiffusion.rightSideFraction < 0.5 : timeAfterEquilibrium < MAX_TIME_AFTER_EQUILIBRIUM);

        return points;
    }


    public static void generateOutputDataFile(
                                              final int iteration,
                                              final double realTime) throws IOException {
        /* write the new output.dat file */
//        final String[] data = pointsToString(systemData.getW(), systemData.getParticles(), iteration);
        final String[] data = pointsToString(CliParser.W, witnesses.values(), iteration);

        final StringBuilder sb = new StringBuilder();
        sb.append(iteration).append('\t')
                .append(realTime).append('\t')
                .append(gasDiffusion.rightSideFraction).append('\t')
                .append(gasDiffusion.currentPressure).append('\t')
                .append(data[KINETIC_ENERGY_INDEX]).append('\n');

        writeOutputFile(sb);
        if(CliParser.dynamicOutput)
            writeDynamicFile(data);

    }

    public static String[] pointsToString(final double W, final Collection<Particle> pointsSet, final long iteration) {
        final StringBuilder sb = new StringBuilder();
//        sb.append(iteration).append('\n');
        sb.append(systemTime).append('\n');
        double vx, vy;
        double kineticEnergy = 0;

        for (final Particle point : pointsSet) {
            vx = point.vx;
            vy = point.vy;

            sb.append(point.id).append('\t').append(point.x).append('\t').append(point.y).append('\t')
                    // velocity
                    .append(vx).append('\t').append(vy).append('\n');
            kineticEnergy += point.kineticEnergy();
        }

        // calculate the current va, assuming the average of all point's speeds (works for the current case)
        // 1/(N * v/N) = 1/v for this case, assuming the above is valid

        if (!pointsSet.isEmpty()) {
            kineticEnergy /= pointsSet.size();
        } else {
            kineticEnergy = 0;
        }

        final String[] answer = new String[2];
        answer[SYSTEM_PARTICLES_INDEX] = sb.toString();
        answer[KINETIC_ENERGY_INDEX] = String.valueOf(kineticEnergy);

        return answer;
    }

    public static void generateOutputDataFile(final int i,
                                              final double eqTime,
                                              final double timeSinceEq) {

        double meanPressure = 0;

        if (gasDiffusion != null && timeSinceEq > 0) {
            meanPressure = gasDiffusion.totalPressure; // timeSinceEq;
        }

        final double temperature = 1/2.0d * CliParser.m* Math.pow(CliParser.v, 2);

        final StringBuilder sb = new StringBuilder();

        final String lineSeparator = System.lineSeparator();
        sb.append("N ").append(CliParser.N).append(lineSeparator)
                .append("L ").append(CliParser.L).append(lineSeparator)
                .append("W ").append(CliParser.W).append(lineSeparator)
                .append("mass ").append(CliParser.m).append(lineSeparator)
                .append("speed ").append(CliParser.v).append(lineSeparator)
                .append("Equilibrium iteration ").append(i).append(lineSeparator)
                .append("Equilibrium real Time (in seconds) ").append(eqTime).append(lineSeparator)
                .append("Mean pressure ").append(meanPressure).append(lineSeparator)
                .append("Temperature ").append(temperature).append(lineSeparator);

        writeOutputFileStats(sb);
    }

    private static void writeOutputFileStats(StringBuilder sb) {
        File file = new File(CliParser.outputDirectory + "/" + name+ "-stats");
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("An IO Exception ocurred 1");
            System.exit(1);

        }
    }

    private static void writeOutputFile(StringBuilder sb) {
        File file = new File(CliParser.outputDirectory + "/" + name);
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("An IO Exception ocurred 1");
            System.exit(1);

        }
    }

    private static void writeDynamicFile(String[] data) {
        File file = new File(CliParser.outputDirectory + "/dynamic/" + name);
        file.getParentFile().mkdirs();


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            StringBuilder squareSb = new StringBuilder();

            int numberOfParticles = witnesses.size();
            writer.write(String.valueOf(numberOfParticles));
            writer.write("\n");

            writer.write(data[Main.SYSTEM_PARTICLES_INDEX]);
            writer.write(squareSb.toString());

        } catch (IOException e) {
            System.out.println("An IO Exception ocurred 2");
            System.exit(1);

        }
    }


}
