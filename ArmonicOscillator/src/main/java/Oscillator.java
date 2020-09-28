

public class Oscillator {
    public double dt;
    public double r, v, t, prev_r, prev_v, prev_a;
    public double c , m , k, y ;
    public double A ;

    public Oscillator(double dt, double m, double k, double y, double c, double A){
        this.dt = dt;
        this.m = m;
        this.k = k;
        this.y = y;
        this.c=c;
        this.A = A;
    }

    public void  initialize(){
        double ri = A, vi = -A * y / (2 * m);
        t = 0.0;
        r = ri;
        v = vi;
        prev_r = ri - dt * vi + 2 * dt * dt * Oscillator.derivative(r, v,k,y,m);
        prev_v = vi - dt * Oscillator.derivative(r, v,k,y,m);
        prev_a = Oscillator.derivative(prev_r, prev_v,k,y,m);
    }


    private static final double a0 = 3.0 / 16, a1 = 251.0 / 360, a2 = 1, a3 = 11.0 / 18, a4 = 1.0 / 6, a5 = 1.0 / 60;

    public double derivative(double x,double dx){
        return Oscillator.derivative(x, dx, k, y,  m);
    }
    public double position(double t){
        return Oscillator.position(t,A,y,m,k);
    }

    public static double derivative(double x, double dx, double k, double y, double m) {
        return (-(k * x) - (y * dx)) / m;
    }

    public static double position(double t, double A, double y, double m, double k) {
        return A * Math.exp( -(y / (2 * m)) * t) * Math.cos(Math.sqrt((k / m) - (y * y / (4 * m * m))) * t);
    }

    public void executeTimestepGear() {
        double r0 = r;
        double r1 = v;
        double r2 = derivative(r0 - c, r1);
        double r3 = derivative(r1, r2);
        double r4 = derivative(r2, r3);
        double r5 = derivative(r3, r4);

        double dt2 = Math.pow(dt, 2), dt3 = Math.pow(dt, 3), dt4 = Math.pow(dt, 4), dt5 = Math.pow(dt, 5);

        double r0p = r0 + r1 * dt + r2 * dt2 / 2 + r3 * dt3 / 6 + r4 * dt4 / 24 + r5 * dt5 / 120;
        double r1p = r1 + r2 * dt + r3 * dt2 / 2 + r4 * dt3 / 6 + r5 * dt4 / 24;
        double r2p = r2 + r3 * dt + r4 * dt2 / 2 + r5 * dt3 / 6;
        double r3p = r3 + r4 * dt + r5 * dt2 / 2;
        double r4p = r4 + r5 * dt;
        double r5p = r5;

        double dr2 = (derivative(r0p - c, r1p) - r2p) * dt2 / 2;

        double r0c = r0p + a0 * dr2;
        double r1c = r1p + a1 * dr2;

        r = r0c;
        v = r1c;
    }

    public void executeTimestepVerlet() {
        double dt2 = Math.pow(dt, 2);
        double a = derivative(r, v,k,y,m);
        double aux_prev_r = r;
        r = 2 * r - prev_r + dt2 * a;
        v = (r - prev_r) / (2 * dt);
        prev_r = aux_prev_r;
    }

    public void executeTimestepBeeman() {
        double dt2 = Math.pow(dt, 2);
        double a = derivative(r, v,k,y,m);
        double pred_v = v + (3.0/2) * a * dt - 0.5 * prev_a * dt;
        r += v * dt + (2.0/3) * a * dt2 - (1.0/6) * prev_a * dt2;
        v += (1.0/3) * derivative(r, pred_v,k,y,m) * dt + (5.0/6) * a * dt - (1.0/6) * prev_a * dt;
        prev_a = a;
    }

}
