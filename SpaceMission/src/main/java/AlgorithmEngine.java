import java.util.List;

public class AlgorithmEngine {
    public double dt, dt2,dt3,dt4,dt5;
    static double G = 6.693e-11;
    public List<Particle> particles;


    public AlgorithmEngine(double dt, List<Particle> particles){
        this.dt = dt;
        this.particles = particles;
        dt2 = Math.pow(dt, 2);
        dt3 = Math.pow(dt, 3);
        dt4 = Math.pow(dt, 4);
        dt5 = Math.pow(dt, 5);
    }


    private static final double a0 = 3.0 / 20, a1 = 251.0 / 360, a2 = 1, a3 = 11.0 / 18, a4 = 1.0 / 6, a5 = 1.0 / 60;


    private void predictLinear(List<Double> r){
        double r0 = r.get(0);
        double r1 = r.get(1);
        double r2 = r.get(2);
        double r3 = r.get(3);
        double r4 = r.get(4);
        double r5 = r.get(5);

        double r0p = r0 + r1 * dt + r2 * dt2 / 2 + r3 * dt3 / 6 + r4 * dt4 / 24 + r5 * dt5 / 120;
        double r1p = r1 + r2 * dt + r3 * dt2 / 2 + r4 * dt3 / 6 + r5 * dt4 / 24;
        double r2p = r2 + r3 * dt + r4 * dt2 / 2 + r5 * dt3 / 6;
        double r3p = r3 + r4 * dt + r5 * dt2 / 2;
        double r4p = r4 + r5 * dt;
        double r5p = r5;
        r.set(0, r0p);
        r.set(1, r1p);
        r.set(2, r2p);
        r.set(3, r3p);
        r.set(4, r4p);
        r.set(5, r5p);
    }

    public void predictPositions(){

        for(Particle p: particles){
            predictLinear(p.rx);
            predictLinear(p.ry);
        }

    }

    public void calculateForces(){
        for(int i = 0; i < particles.size(); i++){
            for(int j=i+1; j < particles.size(); j++){
                applyForce(particles.get(i), particles.get(j));
            }
        }
    }

    private void updateLinear(List<Double> r, double f, double m){
        double dr2 = (f/m - r.get(2)) * dt2 / 2;

        double r0c = r.get(0) + a0 * dr2;
        double r1c = r.get(1) + a1 * dr2/dt;
        double r2c = r.get(2) + a2 * 2 *dr2/dt2;
        double r3c = r.get(3) + a3 * 6 * dr2/dt3;
        double r4c = r.get(4) + a4 * 24 * dr2/dt4;
        double r5c = r.get(5) + a5 * 120 * dr2/dt5;

        r.set(0, r0c);
        r.set(1, r1c);
        r.set(2, r2c);
        r.set(3, r3c);
        r.set(4, r4c);
        r.set(5, r5c);
    }

    public void updatePositions(){

        for(Particle p: particles){
            updateLinear(p.rx, p.fx, p.mass);
            updateLinear(p.ry, p.fy, p.mass);
        }

    }

    private void clearForces(){
        for(Particle p: particles){
            p.fx = 0;
            p.fy = 0;
        }
    }

    public void executeTimestepGear() {

        clearForces();

        predictPositions();

        calculateForces();

        updatePositions();


    }

    public static void applyForce(Particle p1, Particle p2) {
        double dist_sq = p1.distance_sq(p2);
        double angle = p1.angle(p2);

        double force = G  * p1.mass * p2.mass / dist_sq;

        double fx = force * Math.cos(angle);
        double fy = force * Math.sin(angle);

        p1.fx += fx;
        p1.fy += fy;
        p2.fx -= fx;
        p2.fy -= fy;
    }


}
