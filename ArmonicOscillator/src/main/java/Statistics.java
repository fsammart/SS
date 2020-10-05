import javax.sound.sampled.Clip;
import java.io.IOException;

public class Statistics {

    public static void main(String[] args) throws IOException {
        for(int exp = 1; exp <=6; exp++){
            CliParser.exp = exp;
            CliParser.base = 10;
            CliParser.outputDirectory = "results";
            for(Algorithm a: Algorithm.values()){
                CliParser.algorithm = a;
                Main.run(false);
            }
            Main.run(true);
        }

    }
}
