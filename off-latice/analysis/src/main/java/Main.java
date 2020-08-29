import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        int time = 3000; // use last 2000 state for stdev

        calculateVariableEta(time);
        calculateVariableDensity(time);
        System.exit(0);
    }


    public static void calculateVariableEta(int time) throws IOException {
        double eta;
        double L;
        int N;

        int[] nValues = {40, 100, 400, 4000, 10000};
        float[] lValues = {3.1f, 5f, 10f, 31.6f, 50f};
        List<SpeedParticle> l;
        List<Double> vaEvolution;

        File file = new File("./VariableEta/Statistics" + System.currentTimeMillis() + ".tsv");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        PrintStream syso = System.out;
        System.setOut(ps);
        System.out.println("eta\tN\tL\tva\ttime");
        System.setOut(syso);

        for(float i = 0; i <= 1; i += 0.25) {
            eta = i;
            for(int j = 0; j <= 4; j++) {
                N = nValues[j];
                L = lValues[j];
                for(int k = 0 ; k <= 4; k ++) {
                    l = RandomParticles.getRandomParticles(N, L);
                    OffLaticeSim.simulate(L, l, eta, time, String.format("./VariableEta/Feta%.2f-N%d-L%.2f-%d", eta, N, L, k));
                    vaEvolution = OffLaticeSim.vaEvolution;
                    printCSV(vaEvolution, eta, N, L, time, ps);
                }
            }
        }
    }


    public static void calculateVariableDensity(int time) throws IOException {
        double eta = 2.5;
        double L = 25;
        int minN = 100;
        int step = 250;
        int maxN = 6250; // density = 10
        int N;

        List<SpeedParticle> l;
        List<Double> vaEvolution;

        File file = new File("./VariableDensity/Statistics" + System.currentTimeMillis() + ".tsv");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        PrintStream syso = System.out;
        System.setOut(ps);
        System.out.println("eta\tN\tL\tva\ttime");
        System.setOut(syso);

        for(N = minN; N <= maxN ; N += step) { // density up to 10
            for(int k = 0 ; k <= 2; k ++) {
                l = RandomParticles.getRandomParticles(N, L);
                OffLaticeSim.simulate(L, l, eta, time, String.format("./VariableDensity/Feta%.2f-N%d-L%.2f-%d", eta, N, L, k));
                vaEvolution = OffLaticeSim.vaEvolution;
                printCSV(vaEvolution, eta, N, L, time, ps);
            }
        }
    }


    public static void printCSV(List<Double> vaEvolution, double eta, int N, double L, int time, PrintStream ps) throws IOException {
        PrintStream syso = System.out;
        System.setOut(ps);
        for(int i = time - 2000; i < time; i ++){
            System.out.println(String.format("%.2f\t%d\t%.2f\t%.6f\t%d",eta, N, L, vaEvolution.get(i), i));
        }
        System.setOut(syso);
    }

}
