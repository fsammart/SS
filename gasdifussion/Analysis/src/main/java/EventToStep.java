import java.io.*;
import java.util.Locale;
import java.util.Scanner;

public class EventToStep {
    /**
     * Convert Event Simulation output to step one
     */

    public static void main(String[] args) throws FileNotFoundException {
        /** We receive Step and dynamic archive
         */

        CliParserStep.parse(args);
        toStep();
    }

    public static void toStep() throws FileNotFoundException{

        final String dynamicFileName = CliParserStep.dynamicFile;
        final String outputDirectory = CliParserStep.outputDirectory;
        final double dt = CliParserStep.dt;

        File dynamicFile = new File(CliParserStep.outputDirectory  + "/dynamic/" + dynamicFileName);
        Scanner sc = new Scanner(dynamicFile);
        sc.useLocale(Locale.US);

        File resultsFile = new File(CliParserStep.outputDirectory  + "/" + dynamicFileName);
        Scanner sc2 = new Scanner(resultsFile);
        sc2.useLocale(Locale.US);

        File outputFile = new File(CliParserStep.outputDirectory + "/dynamicStep/" + dynamicFile.getName());
        outputFile.getParentFile().mkdirs();
        File resultsFileStep = new File(CliParserStep.outputDirectory + "/ResultsStep/" + dynamicFile.getName());
        resultsFileStep.getParentFile().mkdirs();

        double radiusNormal = 0.0015;
        double radiusSquare = 0.0004;
        double currentTime = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {

            while(sc.hasNextInt()) {
                int numberOfParticles = sc.nextInt();
                double time = sc.nextDouble();
                boolean write = false;
                if(time >= currentTime) {
                    currentTime += dt;
                    write = true;
                    final StringBuilder sb = new StringBuilder();
                    final StringBuilder sb1 = new StringBuilder();
                    int realNumber = numberOfParticles + drawSquare(sb, radiusSquare);
                    sb1.append(realNumber).append('\n');
                    sb1.append(time).append('\n');
                    writer.write(sb1.toString());
                    writer.write(sb.toString());
                }
                pasteResultsFile(sc2, resultsFileStep, write);
                for (int i = 0; i < numberOfParticles; i++) {
                    double x = sc.nextDouble();
                    double y = sc.nextDouble();
                    double vx = sc.nextDouble();
                    double vy = sc.nextDouble();
                    if(write) {
                        final StringBuilder sb = new StringBuilder();

                        sb.append(x).append('\t').append(y).append('\t')
                                // velocity
                                .append(vx).append('\t').append(vy).append('\t').append(radiusNormal).append('\n');
                        writer.write(sb.toString());
                    }
                }

            }
            try (BufferedWriter writer2 = new BufferedWriter(new FileWriter(resultsFileStep, true))) {
                // now copy last lines
                while (sc2.hasNextByte()) {
                    writer2.write(sc2.nextByte());
                }
            }catch (IOException e){
                e.printStackTrace();
                return ;
            }


        } catch (IOException e) {
            System.out.println("An IO Exception ocurred jeje");
            System.exit(1);

        }




    }


    private static int drawSquare(StringBuilder sb,double radius) throws IOException {
        double interval = 0.001;
        int numberOfParticles = 0;

        for (double i = 0; i < (CliParserStep.L - CliParserStep.g) / 2; i += interval) {
            double space = CliParser.L - i;

            sb.append(CliParserStep.W/2 + "\t" + i + "\t0.0\t0.0\t" +radius +"\n");
            sb.append(CliParserStep.W/2 + "\t" + space + "\t0.0\t0.0\t" +radius + "\n");

            numberOfParticles += 2;
        }

        sb.append("0.0\t0.0\t0.0\t0.0\t" +radius +"\n");
        sb.append(CliParserStep.W + "\t0.0\t0.0\t0.0\t" +radius + "\n");
        sb.append("0.0\t" + CliParserStep.L+ "\t0.0\t0.0\t" +radius + "\n");

        numberOfParticles += 3;
        return numberOfParticles;
    }

    private static void pasteResultsFile(Scanner sc2, File resultsFileStep, boolean paste){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultsFileStep, true))) {
            int iteration = sc2.nextInt();
            double realTime = sc2.nextDouble();
            double rightFraction = sc2.nextDouble();
            double pressure = sc2.nextDouble();
            double kinetic = sc2.nextDouble();
            if(paste){
                final StringBuilder sb = new StringBuilder();

                sb.append(iteration).append('\t').append(realTime).append('\t')
                        // velocity
                        .append(rightFraction).append('\t').append(pressure).append('\t')
                        .append(pressure).append('\t').append(kinetic).append('\n');
                writer.write(sb.toString());
            }



        }catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
