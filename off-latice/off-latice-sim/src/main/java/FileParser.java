import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileParser {
    public static int N;
    public static double L;
    public static List<SpeedParticle> particleList = new ArrayList<>();
    public static double maxRadius1 = 0;
    public static double maxRadius2 = 0;

    public static double minRadius = -1;

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

        initializeParticles(sc);

    }

    private static void initializeParticles(Scanner sc){

        for (int i = 0; i < N; i++) {
            double radius = sc.nextDouble();
            double property = sc.nextDouble();
            if(radius > maxRadius1 ){
                maxRadius2 = maxRadius1;
                maxRadius1 = radius;
            }
            if(minRadius == -1 || radius < minRadius){
                minRadius = radius;
            }

            particleList.add(new SpeedParticle(radius,property));
        }
    }

    private static void dynamicParse(String dynamicFile) throws FileNotFoundException {
        File dFile = new File(dynamicFile);
        Scanner sc = new Scanner(dFile);
        sc.useLocale(Locale.US);
        sc.nextInt();   /* Discard first time value */
        for (int i = 0; i < N; i++){
            double x = sc.nextDouble();
            double y = sc.nextDouble();
            double angle = sc.nextDouble();
            SpeedParticle particle = particleList.get(i);
            particle.setX(x);
            particle.setY(y);
            particle.setAngle(angle);
            particle.setVel(0.03);
        }

    }

}
