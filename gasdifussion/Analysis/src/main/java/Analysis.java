import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;


public class Analysis {

    public static void main(String[] args) throws IOException {

        Main.equilibriumReached = false;
        Main.systemTime = 0;
        Main.iteration = 0;
        Main.lastFps = new LinkedList<>();

        CliParser.L = 0.09;
        CliParser.W = 0.24;
        CliParser.random = true;
        CliParser.m = 1;
        CliParser.radius = 0.0015;

        CliParser.N  = 100;
        CliParser.v = 0.01;
        CliParser.g = 0.02;

        // to print dynamic file
        CliParser.dynamicOutput = false;
        CliParserStep.dynamicOutput = CliParser.dynamicOutput;

        CliParser.outputDirectory = "results";




        Wall.HORIZONTAL.setLength(CliParser.W);
        Wall.VERTICAL.setLength(CliParser.L);
        Wall.MIDDLE_VERTICAL.setLength((CliParser.L - CliParser.g)/2.0);


        Main.name = "groove_stats2.tsv";
        generateEquilibriumTimeFileHeader();
        for(double a = 0.01; a < 0.09; a+= 0.01){
            CliParser.g = a;
            for(int i=1; i<= 5; i++) {
                Main.systemTime = 0;
                Main.lastFps = new LinkedList<>();
                Main.iteration = 0;
                List<Particle> particles = GRandom.
                        getRandomParticles(CliParser.N,CliParser.L,CliParser.radius,
                                CliParser.m, CliParser.v, CliParser.W);
                Main.witnesses = new TreeMap<>();
                particles.forEach(point -> Main.witnesses.put(point.id, point));

                Main.gasDiffusion = new GasDiffusion2D(CliParser.L, CliParser.W, CliParser.g, particles);

                Main.runGasDiffusion(particles, false);

                int eqIteration = Main.iteration;
                double eqSystemTime = Main.systemTime;
                Main.gasDiffusion.totalPressure += Main.gasDiffusion.currentPressure;
                Main.gasDiffusion.currentPressure = 0;
                Main.equilibriumReached = true;

                Main.runGasDiffusion(particles, true);


                generateEquilibriumTimeFile(eqIteration, eqSystemTime, Main.systemTime - eqSystemTime);

                //toStep(0.1, Main.name);
            }
        }

    }

    public static void toStep(double dt, String name) throws FileNotFoundException {
        CliParserStep.g = CliParser.g;
        CliParserStep.L = CliParser.L;
        CliParserStep.N = CliParser.N;
        CliParserStep.W = CliParser.W;
        CliParserStep.dt = dt;
        CliParserStep.outputDirectory = "results";
        CliParserStep.dynamicFile = name;
        if(CliParserStep.dynamicOutput){
            EventToStep.toStep();
        }
        EventToStep.toStepResults();
    }

    public static void generateEquilibriumTimeFile(final int i,
                                              final double eqTime,
                                              final double timeSinceEq) {

        double meanPressure = 0;

        if (Main.gasDiffusion != null && timeSinceEq > 0) {
            meanPressure = Main.gasDiffusion.totalPressure; // timeSinceEq;
        }

        final double temperature = 1/2.0d * CliParser.m* Math.pow(CliParser.v, 2);

        final StringBuilder sb = new StringBuilder();

        final String lineSeparator = "\t";
        sb.append(CliParser.N).append(lineSeparator)
                .append(CliParser.L).append(lineSeparator)
                .append(CliParser.W).append(lineSeparator)
                .append(CliParser.g).append(lineSeparator)
                .append(CliParser.m).append(lineSeparator)
                .append(CliParser.v).append(lineSeparator)
                .append(i).append(lineSeparator)
                .append(eqTime).append(lineSeparator)
                .append(meanPressure).append(lineSeparator)
                .append(temperature).append("\n");

        writeOutputFileStats(sb);
    }

    public static void generateEquilibriumTimeFileHeader() {


        final StringBuilder sb = new StringBuilder();

        final String lineSeparator ="\t";
        sb.append("N").append(lineSeparator)
                .append("L").append(lineSeparator)
                .append("W").append(lineSeparator)
                .append("g").append(lineSeparator)
                .append("mass").append(lineSeparator)
                .append("speed").append(lineSeparator)
                .append("eq_iter").append(lineSeparator)
                .append("eq_time").append(lineSeparator)
                .append("mean_pressure").append(lineSeparator)
                .append("temperature").append("\n");

        writeOutputFileStats(sb);
    }

    private static void writeOutputFileStats(StringBuilder sb) {
        File file = new File(CliParser.outputDirectory + "/" + Main.name);
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("An IO Exception ocurred 1");
            System.exit(1);

        }
    }
}
