import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        int N;
        double L;
        double eta;
        int time;


        CliParser.parse(args);

        eta = CliParser.eta;
        time = CliParser.time;

        List<SpeedParticle> l;

        if(CliParser.random){
            N = CliParser.N;
            L = CliParser.L;
            l = RandomParticles.getRandomParticles(N, L);
        } else {
            L = FileParser.L;
            l = FileParser.particleList;
        }

        OffLaticeSim.simulate(L,l,eta,time,CliParser.outputDirectory + "/t");
    }



}
