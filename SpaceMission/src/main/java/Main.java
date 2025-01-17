import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static double dt;
    private static double tf = 60 * 60 * 24 * 365;


    public static void main(String[] args) throws IOException {
        CliParser.parseOptions(args);

        runHour(192, 15000, "dynamic/evolution.csv");
        //run();
//        int day;
//        for(day=0; day <= 3*365; day+=1){
//            run(day);
//        }
//        runDT(1);
//        for(dt=10; dt <= 20000; dt+=10){
//            runDT(dt);
//        }
    }

    public static void main2(String[] args) throws IOException {
        CliParser.parseOptions(args);
        int day;
        double prevDistance = -1;
        boolean analyzed = false;
        for(day=120; day <= 9*30; day+=1){
            double distance = run(day);
            if(distance <= 0){
                distance =0;
            }
            if(prevDistance == -1){
                prevDistance = distance;
            }
            if(!analyzed && prevDistance < distance){
                // we are growing
//                analyzeHour(day-2);
                analyzed = true;
            }
            prevDistance = distance;

        }
        //run();
//        for(dt=0.1; dt <= 5; dt+=10){
//            run(dt);
//        }

    }

    public static void analyzeHour(int day) throws IOException {
        String name = "dynamic/dtm_hour.csv";
        double prevDistance = -1;
        boolean analyzed = false;
        for(int d = day; d < day + 2; d++){
            for(int sec =0; sec < 60*60*24; sec += 60*60){
                double distance = runHour(d, sec, name);
                if(distance <= 0){
                    distance =0;
                }
                if(prevDistance == -1){
                    prevDistance = distance;
                }
                if(!analyzed && prevDistance < distance){
                    // we are growing
                    analyzeSecs(d, sec - 2*60*60);
                    analyzed = true;
                }
                prevDistance = distance;
            }
        }
    }

    public static void analyzeSecs(int day, int secs) throws IOException {
        String name = "dynamic/dtm_seconds.csv";
        for(int sec =secs; sec < secs + 2*60*60; sec += 30){
            runHour(day, sec, name);
        }

    }

    public static double run(int day) throws IOException {
        dt = CliParser.dt;
        List<Particle> particles = new ArrayList<>(5);
        setUpSolarSystem(particles);

        AlgorithmEngine os = new AlgorithmEngine(dt, particles);

        List<Double> times = new ArrayList<>((int) (tf / dt));
        double t = 0;

        times.add(t);

        String name = "dynamic/evolution.csv";// + System.currentTimeMillis();

        boolean launched = false;
        double min_distance = Double.MAX_VALUE;
        double min_time;
        int launch_day = day*24*60*60;
        while (t < launch_day+tf) {
            t += dt;
            if(!launched && t >= launch_day){
                launch(particles);
                launched = true;
                System.out.println("Launched on day " + day);
            }
            if(launched){
                double current_distance = distanceToMars(particles);
                // System.out.println("MIN " + min_distance + "- Current " +current_distance);
                if(current_distance <=0){
                    min_distance = 0;
                    break;
                }
                if (current_distance >=0 && current_distance < min_distance){
                    min_distance = current_distance;
                    min_time = t;
                }

            }
            os.executeTimestepGear();
        }
        //printSolarSystem(t, particles, name);
        //printSpaceshipDistanceToMars(day, name, min_distance, t, particles);
        return min_distance;
    }

    public static void runDT(double dt) throws IOException {
        if(tf % dt !=0){
            System.out.println("Dt not valid -" + dt);
            return;
        }

        List<Particle> particles = new ArrayList<>(5);
        setUpSolarSystem(particles);

        AlgorithmEngine os = new AlgorithmEngine(dt, particles);

        List<Double> times = new ArrayList<>((int) (tf / dt));
        double t = 0;

        times.add(t);

        String name = "dynamic/position_dt.csv";// + System.currentTimeMillis();

        boolean launched = false;
        double min_distance = Double.MAX_VALUE;
        while (t < tf) {
            t += dt;
            os.executeTimestepGear();
        }
        printSolarSystemRelativeToSun(t, particles, name);
    }

    public static double runHour(int day, int secs, String name) throws IOException {
        dt = CliParser.dt;
        List<Particle> particles = new ArrayList<>(6);
        setUpSolarSystem(particles);

        AlgorithmEngine os = new AlgorithmEngine(dt, particles);

        List<Double> times = new ArrayList<>((int) (tf / dt));
        double t = 0;

        times.add(t);

//        String name = "dynamic/dtmh.csv";// + System.currentTimeMillis();

        boolean launched = false;
        double min_distance = Double.MAX_VALUE;
        int launch_day = day*24*60*60 + secs;
        double min_time = 0.0;
        while (t < launch_day+tf) {
//            if(t%CliParser.dt2 ==0) {
//                printSolarSystem(t, particles, "dynamic/resultsXYZ");
//            }
            t += dt;
            if(!launched && t >= launch_day){
                launch(particles);
                launched = true;
                System.out.println("Launched on day " + day + "and secs" + secs);
            }
            if(launched){
                double current_distance = distanceToMars(particles);
                // System.out.println("MIN " + min_distance + "- Current " +current_distance);
                if(current_distance <=0){
                    min_distance = 0;
                    printSpaceshipDistanceToMarsHourly(day,secs, name, min_distance, min_time, particles);
                    break;
                }
                if (current_distance < min_distance){
                    min_distance = current_distance;
                    min_time = t;
                }
                printSpaceshipDistanceToMarsHourly(day,secs, name, min_distance, min_time, particles);
            }
            os.executeTimestepGear();
        }

        //printSpaceshipDistanceToMarsHourly(day,secs, name, min_distance, min_time, particles);
        return min_distance;
    }

    private static void setUpSolarSystem(List<Particle> particles){
        double sun_mass = 1988500e24;
        double sun_radius= 696000 * 1000;
        double sun_position_x = 0;
        double sun_position_y = 0;
        double sun_velocity_x = 0;
        double sun_velocity_y = 0;

        double earth_mass =5.97219e24; //kg
        double earth_radius=6378.137 * 1000; // to meters
        // 1/1/20
        // X =-2.488497169862896E+07 Y = 1.449783471212823E+08 Z =-6.171784579284489E+03
        // VX=-2.984892046591452E+01 VY=-5.162374739569864E+00 VZ= 7.366656604983479E-04
        double earth_position_x = -2.488497169862896e+07 * 1000;
        double earth_position_y = 1.449783471212823e+08 * 1000;
        double earth_velocity_x = -2.984892046591452e+01 * 1000;
        double earth_velocity_y = -5.162374739569864e+00 * 1000;

        double mars_mass =6.4171e23; //kg
        double mars_radius=3389.92 * 1000; // to meters
        // 1/1/20
        // X =-1.974852867957307E+08 Y =-1.325074306424199E+08 Z = 2.068800267463274E+06
        // VX= 1.440720082952704E+01 VY=-1.804659323598330E+01 VZ=-7.316474588259982E-01
        double mars_position_x = -1.974852867957307e+08 * 1000;
        double mars_position_y = -1.325074306424199e+08 * 1000;
        double mars_velocity_x = 1.440720082952704e+01 * 1000;
        double mars_velocity_y = -1.804659323598330e+01 * 1000;

        double moon_mass =7.348e22; //kg
        double moon_radius=1737.5 * 1000; // to meters
        // 1/1/20
//        X =-2.449478606006278E+07 Y = 1.448800063584762E+08 Z =-4.062144616185874E+04
//        VX=-2.960019274200946E+01 VY=-4.226637542798546E+00 VZ=-3.430462726334782E-02
        double moon_position_x = -2.449478606006278e+07 * 1000;
        double moon_position_y = 1.448800063584762e+08 * 1000;
        double moon_velocity_x = -2.960019274200946e+01 * 1000;
        double moon_velocity_y = -4.226637542798546e+00 * 1000;


        Particle sun = setUpParticle(sun_mass, sun_radius, sun_position_x, sun_position_y, sun_velocity_x, sun_velocity_y, "SUN");
        Particle earth = setUpParticle(earth_mass, earth_radius, earth_position_x, earth_position_y, earth_velocity_x, earth_velocity_y, "EARTH");
        Particle mars = setUpParticle(mars_mass, mars_radius, mars_position_x, mars_position_y, mars_velocity_x, mars_velocity_y, "MARS");
        Particle moon = setUpParticle(moon_mass, moon_radius, moon_position_x, moon_position_y, moon_velocity_x, moon_velocity_y, "MOON");
        particles.add(sun);
        particles.add(earth);
        particles.add(mars);
        //particles.add(moon);
    }

    private static Particle setUpParticle(double mass, double radius, double position_x, double position_y, double velocity_x, double velocity_y, String name){
        List<Double> rx = new ArrayList<Double>(6);
        List<Double> ry = new ArrayList<Double>(6);
        rx.add(0,position_x);
        rx.add(1,velocity_x);
        rx.add(2,0.0);
        rx.add(3,0.0);
        rx.add(4,0.0);
        rx.add(5,0.0);

        ry.add(0,position_y);
        ry.add(1,velocity_y);
        ry.add(2,0.0);
        ry.add(3,0.0);
        ry.add(4,0.0);
        ry.add(5,0.0);

        return new Particle(rx, ry, radius, mass, name);
    }

    private static boolean checkAligned(Particle sun, Particle earth, Particle spaceship) {
        double angle1 = sun.angle(earth);
        double angle2 = sun.angle(spaceship);
        return angle2-angle1 >= 0.00001;
    }

    private static void launch(List<Particle> particles){
        addSpaceship(particles);
        Particle sun = particles.get(0);
        Particle earth = particles.get(1);
        Particle spaceship = particles.get(3);
        double angle1 = sun.angle(earth);
        double angle2 = angle1 +  Math.PI/2;
        double v0 = 8 * 1000;
        //double v0 = 5 * 1000;

        double vx = spaceship.rx.get(1) + v0*Math.cos(angle2);
        double vy = spaceship.ry.get(1) + v0*Math.sin(angle2);

        spaceship.rx.set(1,vx);
        spaceship.ry.set(1,vy);
    }

    private static void addSpaceship(List<Particle> particles) {
        Particle sun = particles.get(0);
        Particle earth = particles.get(1);

        double angle1 = sun.angle(earth);
        double angle2 = angle1 +  Math.PI/2;
        double distance_earth =  earth.radius + 1500 * 1000;
        double delta_v =  7.12 * 1000;

        double spaceship_positionx = earth.rx.get(0) + distance_earth * Math.cos(angle1);
        double spaceship_positiony = earth.ry.get(0) + distance_earth * Math.sin(angle1);

        double vy = earth.ry.get(1) + delta_v * Math.sin(angle2) ;
        double vx = earth.rx.get(1) +  delta_v * Math.cos(angle2) ;

        double spaceship_mass =5e5; //kg
        double spaceship_radius= 1000; // to meters

        Particle spaceship = setUpParticle(spaceship_mass, spaceship_radius, spaceship_positionx, spaceship_positiony, vx, vy, "spaceship");
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
                double radius = p.radius;
                if(i==0){
                    radius /= 50;
                }
                if(i==3){
                    radius *= 100;
                }
                writer.println( i++ + "\t" + p.rx.get(0) + "\t" + p.ry.get(0) + "\t"  + radius);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printSolarSystemRelativeToSun(double t, List<Particle> particles, String filename) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        FileWriter fr = new FileWriter(file, true);


        try (PrintWriter writer = new PrintWriter(fr)) {
            int i = 0;
            for(Particle p: particles){
                double rel_x = p.rx.get(0) - particles.get(0).rx.get(0);
                double rel_y = p.ry.get(0) - particles.get(0).ry.get(0);
                writer.println( p.name + "\t" + rel_x + "\t" + rel_y + "\t" + dt );// + "\t"  + p.radius/10000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printSpaceshipDistanceToMars(int day, String filename, double distance ) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        FileWriter fr = new FileWriter(file, true);

        try (PrintWriter writer = new PrintWriter(fr)) {
            writer.println( day + "\t" + distance);
            System.out.println(day + " - " + distance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printSpaceshipDistanceToMarsHourly(int day,int secs, String filename, double distance, double time, List<Particle> particles ) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        FileWriter fr = new FileWriter(file, true);
        Particle spaceship = particles.get(3);

        double vx = spaceship.rx.get(1);
        double vy = spaceship.ry.get(1);

        try (PrintWriter writer = new PrintWriter(fr)) {
            writer.println( day + "\t" + secs + "\t" + distance + "\t" + time + "\t" + vx + "\t" + vy);
            //System.out.println(day + " - " + secs + "-" + distance + "\t" + time + "\t" + vx + "\t" + vy );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double distanceToMars(List<Particle> particles){
        Particle spaceship = particles.get(3);
        Particle mars = particles.get(2);

        double dx = spaceship.rx.get(0) - mars.rx.get(0);
        double dy = spaceship.ry.get(0) - mars.ry.get(0);
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) - mars.radius;
    }
}
