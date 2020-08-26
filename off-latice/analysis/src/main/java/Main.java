import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        double eta;
        double L  =25;
        int N;
        int time = 3000; // use last 100 state for stdev
        List<SpeedParticle> l;
        List<Double> vaEvolution;

        File file = new File("Statistics" + System.currentTimeMillis() + ".csv");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        PrintStream syso = System.out;
        System.setOut(ps);
        System.out.println("eta,N,L,va,time");
        System.setOut(syso);

        for(double i = 0; i <=5; i += 0.25) {
            eta = i;
            for(N = 10; N <= 5000; N *= 2){
                l = RandomParticles.getRandomParticles(N, L);
                OffLaticeSim.simulate(L,l,eta,time, String.format("Feta%.2f-N%d-L%.2f",eta,N,L));
                vaEvolution = OffLaticeSim.vaEvolution;
                printCSV(vaEvolution, eta, N, L, time, ps);
            }

        }

    }

    public static void printCSV(List<Double> vaEvolution, double eta, int N, double L, int time, PrintStream ps) throws IOException {
        PrintStream syso = System.out;
        System.setOut(ps);
        for(int i = time - 100; i < time; i ++){
            System.out.println(String.format("%.2f,%d,%.2f,%.6f,%d",eta, N, L, vaEvolution.get(i), i));
        }
        System.setOut(syso);
    }
}
