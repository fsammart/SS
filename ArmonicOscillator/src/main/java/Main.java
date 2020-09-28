import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static double dt;
    private static double tf = 5;
    private static double c = 0.0, m = 70.0, k = 10e4, y = 100.0;
    private static double A = 1.0;


    public static void main(String[] args) {
        CliParser.parseOptions(args);
        dt = CliParser.base * Math.pow(10, -CliParser.exp);

        Oscillator os = new Oscillator(dt,m,k,y,c,A);
        os.initialize();


        int stepCount = 0;

        List<Double> times = new ArrayList<>((int) (tf / dt));
        List<Double> positions = new ArrayList<>((int) (tf / dt));
        List<Double> analyticPositions = new ArrayList<>((int) (tf / dt));
        List<Double> errors = new ArrayList<>((int) (tf / dt));
        List<Double> errorsNormalized = new ArrayList<>((int) (tf / dt));

        times.add(os.t);
        positions.add(os.r);
        analyticPositions.add(os.position(os.t));
        errors.add(0.0);
        errorsNormalized.add(0.0);

        while (os.t < tf) {
            os.t += dt;
            stepCount++;

            switch (CliParser.algorithm) {
                case GEAR:
                    os.executeTimestepGear();
                    break;
                case BEEMAN:
                    os.executeTimestepBeeman();
                    break;
                case VERLET:
                    os.executeTimestepVerlet();
                    break;
            }


            double pos = os.position(os.t);

            double error = os.r - pos;
            double error_sq = Math.pow(error, 2);

            times.add(os.t);
            positions.add(os.r);
            analyticPositions.add(pos);
            errors.add(error_sq);
            errorsNormalized.add(error_sq / stepCount);
        }

        printList(times, CliParser.outputDirectory + "/" + CliParser.base + "e" + CliParser.exp + "_" + CliParser.algorithm + "_times.csv");
        printList(positions, CliParser.outputDirectory + "/"  + CliParser.base + "e" + CliParser.exp + "_" + CliParser.algorithm + "_positions.csv");
        printList(analyticPositions, CliParser.outputDirectory + "/" + CliParser.base + "e" + CliParser.exp + "_" + CliParser.algorithm + "_analyticPositions.csv");
        printList(errors, CliParser.outputDirectory + "/"  + CliParser.base + "e" + CliParser.exp + "_" + CliParser.algorithm + "_errors.csv");
        printList(errorsNormalized, CliParser.outputDirectory + "/"  + CliParser.base + "e" + CliParser.exp + "_" + CliParser.algorithm + "_errorsNormalized.csv");
    }

    private static void printList(List<Double> list, String filename) {
        File file = new File(filename);
        file.getParentFile().mkdirs();

        try (PrintWriter writer = new PrintWriter(file)) {
            for (double d : list) {
                writer.println(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}