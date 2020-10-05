import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static double dt;
    private static double tf = 60 * 60 * 24 * 200;

    public static void main(String[] args) throws IOException {
        CliParser.parseOptions(args);
        run();

    }

    public static void run() throws IOException {
        dt = CliParser.dt;
        List<Double> sun_rx = new ArrayList<Double>(6);
        List<Double> sun_ry = new ArrayList<Double>(6);
        sun_rx.add(0,0.0);
        sun_rx.add(1,0.0);
        sun_rx.add(2,0.0);
        sun_rx.add(3,0.0);
        sun_rx.add(4,0.0);
        sun_rx.add(5,0.0);

        sun_ry.add(0,0.0);
        sun_ry.add(1,0.0);
        sun_ry.add(2,0.0);
        sun_ry.add(3,0.0);
        sun_ry.add(4,0.0);
        sun_ry.add(5,0.0);
        double sun_mass = 1988500e24;
        double sun_radius= 696000 * 1000;

        List<Double> earth_rx = new ArrayList<Double>(6);
        List<Double> earth_ry = new ArrayList<Double>(6);
        //earth_rx.add(0,1.49318892963666e8 * 1000);
        //earth_rx.add(1,-3.113279917782445 * 1000);
        earth_rx.add(0,9.144837571509564e+07 * 1000);
        earth_rx.add(1,2.328688878608865e+01 * 1000);
        earth_rx.add(2,0.0);
        earth_rx.add(3,0.0);
        earth_rx.add(4,0.0);
        earth_rx.add(5,0.0);

        //earth_ry.add(0,1.318936357931255e07 * 1000);
        //earth_ry.add(1,2.955205189256462e01 * 1000);
        earth_ry.add(0,-1.212542997509061e+08 * 1000);
        earth_ry.add(1,1.782952288872059e+01 * 1000);
        earth_ry.add(2,0.0);
        earth_ry.add(3,0.0);
        earth_ry.add(4,0.0);
        earth_ry.add(5,0.0);

        double earth_mass =5.97219e24; //kg
        double earth_radius=6378.137 * 1000; // to meters

        List<Double> mars_rx = new ArrayList<Double>(6);
        List<Double> mars_ry = new ArrayList<Double>(6);
        //mars_rx.add(0,2.059448551842169e08 * 1000);
        //mars_rx.add(1,-3.717406842095575e0 * 1000);
        mars_rx.add(0,1.845926818453264e+08 * 1000);
        mars_rx.add(1,1.180038069484242e+01 * 1000);
        mars_rx.add(2,0.0);
        mars_rx.add(3,0.0);
        mars_rx.add(4,0.0);
        mars_rx.add(5,0.0);

        //mars_ry.add(0,4.023977946528339e07 * 1000);
        //mars_ry.add(1,2.584914078301731e01 * 1000);
        mars_ry.add(0,-9.272024311856113e+07 * 1000);
        mars_ry.add(1,2.372295882384511e+01 * 1000);
        mars_ry.add(2,0.0);
        mars_ry.add(3,0.0);
        mars_ry.add(4,0.0);
        mars_ry.add(5,0.0);

        double mars_mass =6.4171e23; //kg
        double mars_radius=3389.92 * 1000; // to meters

        //hallar posición nave y velocidad.
        /*
        List<Double> spaceship_rx = new ArrayList<Double>(6);
        List<Double> spaceship_ry = new ArrayList<Double>(6);
        spaceship_rx.add(0,2.059448551842169e08);
        spaceship_rx.add(1,-3.717406842095575e0);
        spaceship_rx.add(2,0.0);
        spaceship_rx.add(3,0.0);
        spaceship_rx.add(4,0.0);
        spaceship_rx.add(5,0.0);

        spaceship_ry.add(0,4.023977946528339e07);
        spaceship_ry.add(1,2.584914078301731e01);
        spaceship_ry.add(2,0.0);
        spaceship_ry.add(3,0.0);
        spaceship_ry.add(4,0.0);
        spaceship_ry.add(5,0.0);

        double spaceship_mass =6.4171e23; //kg
        double spaceship_radius=3389.92 * 1000; // to meters

         */

        Particle sun = new Particle(sun_rx, sun_ry,  sun_radius,sun_mass,"SUN");
        Particle earth = new Particle(earth_rx,earth_ry,earth_radius,earth_mass, "EARTH");
        Particle mars = new Particle(mars_rx, mars_ry, mars_radius, mars_mass, "MARS");
        //Particle spaceship = new Particle()

        List<Particle> particles = new ArrayList<>(5);
        particles.add(sun);
        particles.add(earth);
        particles.add(mars);
        
        addSpaceship(particles, sun, earth);
        launch(particles.get(3),sun, earth);
        AlgorithmEngine os = new AlgorithmEngine(dt, particles);


        List<Double> times = new ArrayList<>((int) (tf / dt));
        double t = 0;

        times.add(t);

        String name = "dynamic/" + System.currentTimeMillis();

        boolean launched = true;
        while (t < tf) {
           t += dt;

           if(!launched && t >= 10*24*60*60){
               if(checkAligned(particles.get(0),particles.get(1), particles.get(3))){
                   launch(particles.get(3),sun, earth);
                   launched = true;
               }

           }

            os.executeTimestepGear();

            printSolarSystem(t, particles, name);

        }


    }

    private static boolean checkAligned(Particle sun, Particle earth, Particle spaceship) {
        double angle1 = sun.angle(earth);
        double angle2 = sun.angle(spaceship);
        return angle2-angle1 >= 0.00001;
    }

    private static void launch(Particle spaceship, Particle sun, Particle earth){
        double angle1 = sun.angle(earth);
        double angle2 = angle1 +  Math.PI/2;
        //double v0 = 8 * 1000;
        double v0 = 5 * 1000;

        double vx = spaceship.rx.get(1) + v0*Math.cos(angle2);
        double vy = spaceship.ry.get(1) + v0*Math.sin(angle2);

        spaceship.rx.set(1,vx);
        spaceship.ry.set(1,vy);

    }

    private static void addSpaceship(List<Particle> particles, Particle sun, Particle earth) {
        double angle1 = sun.angle(earth);
        double angle2 = angle1 +  Math.PI/2;
        double distance_earth =  earth.radius + 1500 * 1000;
        double delta_v =  7.12 * 1000;


        double spaceship_positionx = earth.rx.get(0) + distance_earth * Math.cos(angle1);
        double spaceship_positiony = earth.ry.get(0) + distance_earth * Math.sin(angle1);


        double vy = earth.ry.get(1) + delta_v * Math.sin(angle2) ;
        double vx = earth.rx.get(1) +  delta_v * Math.cos(angle2) ;

        List<Double> spaceship_rx = new ArrayList<Double>(6);
        List<Double> spaceship_ry = new ArrayList<Double>(6);
        spaceship_rx.add(0,spaceship_positionx);
        spaceship_rx.add(1,vx);
        spaceship_rx.add(2,0.0);
        spaceship_rx.add(3,0.0);
        spaceship_rx.add(4,0.0);
        spaceship_rx.add(5,0.0);

        spaceship_ry.add(0,spaceship_positiony);
        spaceship_ry.add(1,vy);
        spaceship_ry.add(2,0.0);
        spaceship_ry.add(3,0.0);
        spaceship_ry.add(4,0.0);
        spaceship_ry.add(5,0.0);

        double spaceship_mass =5e5; //kg
        double spaceship_radius= 1000; // to meters
        Particle spaceship = new Particle(spaceship_rx, spaceship_ry, spaceship_radius, spaceship_mass, "spaceship");
        particles.add(spaceship);

    }

    private static void printSolarSystem(double t, List<Particle> particles, String filename) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        FileWriter fr = new FileWriter(file, true);

        try (PrintWriter writer = new PrintWriter(fr)) {
            int i = 0;
            writer.println(particles.size());
            writer.println(t);
            for(Particle p: particles){
                writer.println( i++ + "\t" + p.rx.get(0)/1e8 + "\t" + p.ry.get(0)/1e8 + "\t"  + p.radius/10000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
