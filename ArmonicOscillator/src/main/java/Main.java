import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    private static double dt;
    private static double tf = 5;
    private static double c = 0.0, m = 70.0, k = 10e4, y = 100.0;
    private static double A = 1.0;


    public static void main(String[] args) throws IOException {
        CliParser.parseOptions(args);
        run(false);

    }
    public static void run(boolean analytic) throws IOException {
        dt = CliParser.base * Math.pow(10, -CliParser.exp);

        Oscillator os = new Oscillator(dt, m, k, y, c, A);
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
        }


        if(analytic){
            printRealPositions(times, analyticPositions, CliParser.outputDirectory + "/evolution.tsv");
        } else {
            printPositions(times, positions, CliParser.outputDirectory + "/evolution.tsv");
            printErrors(times, errors, CliParser.outputDirectory + "/errors.tsv");
        }

    }

    private static void printRealPositions(List<Double> times,
                                       List<Double> positions, String filename) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        FileWriter fr = new FileWriter(file, true);

        try (PrintWriter writer = new PrintWriter(fr)) {
            //writer.println("algorithm" + "\t" + "exponent" + "\t" + "time" + "\t" + "position" );
            for(int i = 0; i < times.size(); i++){
                writer.println("Analytic" + "\t" + CliParser.exp + "\t" + times.get(i) + "\t" + positions.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private static void printPositions(List<Double> times,
                                       List<Double> positions, String filename) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        FileWriter fr = new FileWriter(file, true);

        try (PrintWriter writer = new PrintWriter(fr)) {
            //writer.println("algorithm" + "\t" + "exponent" + "\t" + "error" );
            // TODO: change step
            for(int i = 0; i < times.size(); i++){
                writer.println(CliParser.algorithm + "\t" + CliParser.exp + "\t" + times.get(i) + "\t" +positions.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void printErrors(List<Double> times,
                                       List<Double> errors, String filename) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        FileWriter fr = new FileWriter(file, true);

        try (PrintWriter writer = new PrintWriter(fr)) {
            //writer.println("algorithm" + "\t" + "exponent" + "\t" + "time" + "\t" + "error" + "\t" + "error_norm");

                writer.println(CliParser.algorithm + "\t" + CliParser.exp + "\t" + errors.stream()
                        .mapToDouble(x->x).average().getAsDouble() );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}