import java.io.FileNotFoundException;
import java.io.IOException;
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
        CliParser.dynamicOutput = true;


        CliParser.outputDirectory = "results";

        Main.name = String.valueOf(System.currentTimeMillis());


        List<Particle> particles = GRandom.
                getRandomParticles(CliParser.N,CliParser.L,CliParser.radius,
                        CliParser.m, CliParser.v, CliParser.W);

        Main.witnesses = new TreeMap<>();
        particles.forEach(point -> Main.witnesses.put(point.id, point));

        Wall.HORIZONTAL.setLength(CliParser.W);
        Wall.VERTICAL.setLength(CliParser.L);
        Wall.MIDDLE_VERTICAL.setLength((CliParser.L - CliParser.g)/2.0);

        Particle.v = CliParser.v;

        Main.gasDiffusion = new GasDiffusion2D(CliParser.L, CliParser.W, CliParser.g, particles);

        Main.runGasDiffusion(particles, false);

        int eqIteration = Main.iteration;
        double eqSystemTime = Main.systemTime;
        Main.gasDiffusion.totalPressure += Main.gasDiffusion.currentPressure;
        Main.gasDiffusion.currentPressure = 0;
        Main.equilibriumReached = true;
        Main.runGasDiffusion( particles, true);

        Main.generateOutputDataFile(eqIteration, eqSystemTime, Main.systemTime - eqSystemTime);

        toStep(0.01, Main.name);

    }

    public static void toStep(double dt, String name) throws FileNotFoundException {
        CliParserStep.g = CliParser.g;
        CliParserStep.L = CliParser.L;
        CliParserStep.N = CliParser.N;
        CliParserStep.W = CliParser.W;
        CliParserStep.dt = dt;
        CliParserStep.outputDirectory = "results";
        CliParserStep.dynamicFile = name;
        EventToStep.toStep();
    }
}
