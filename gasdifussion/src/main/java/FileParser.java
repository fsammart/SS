import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class FileParser {
    public static int N;
    public static double L;
    public static List<Particle> particleList = new ArrayList<>();
    public static double radius;
    public static double m;
    public static double v;
    public static double W;
    public static double g;

    public static void parseFiles(String staticFile, String dynamicFile) throws FileNotFoundException {
        staticParse(staticFile);
        dynamicParse(dynamicFile);
    }

    private static void staticParse(String staticFile) throws FileNotFoundException {
        File sFile = new File(staticFile);
        Scanner sc = new Scanner(sFile);
        sc.useLocale(Locale.US);
        N = sc.nextInt();
        L = sc.nextDouble();
        W = sc.nextDouble();
        g = sc.nextDouble();
        m = sc.nextDouble();
        v = sc.nextDouble();
        radius = sc.nextDouble();

        initializeParticles();

    }

    private static void initializeParticles(){
        for (int i = 0; i < N; i++) {
            particleList.add(new Particle(radius,m));
        }
    }

    private static void dynamicParse(String dynamicFile) throws FileNotFoundException {
        // x y vx vy
        File dFile = new File(dynamicFile);
        Scanner sc = new Scanner(dFile);
        sc.useLocale(Locale.US);
        sc.nextInt();   /* Discard first time value */
        for (int i = 0; i < N; i++){
            double x = sc.nextDouble();
            double y = sc.nextDouble();
            double vx = sc.nextDouble();
            double vy = sc.nextDouble();

            Particle particle = particleList.get(i);
            particle.setX(x);
            particle.setY(y);
            particle.setVx(vx);
            particle.setVy(vy);
        }

    }

}
