
public class CPMParticle extends Particle{

    public double vel, angle;

    public boolean is_obstacle;


    public static double v_max;
    public static double ap;
    public static double bp;

    public static double beta;

    public static double goal_x;
    public static double goal_y;

    public static double r_max;
    public static double r_min;
    public static double tau;


    public double distanceToGoal(){
        double goalDx = this.x - goal_x;
        double goalDy = this.y - goal_y;

        return Math.sqrt(goalDx*goalDx + goalDy*goalDy);
    }


    public CPMParticle(double x, double y, double radius) {
        super(x, y, radius);
    }

    public double move(double dt, double L, double W){
        // TODO: check
        this.x = this.x + dt*this.vel * Math.cos(this.angle);
        this.y = (this.y + dt*this.vel * Math.sin(this.angle)+ W)%W;
        return this.vel * dt;
    }


    public static void escape(CPMParticle p1, CPMParticle p2,double L, double W){

        p1.radius = p1.r_min;
        // change v direction;
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        //contour check

        if ((W - Math.abs(dy) ) < Math.abs(dy)){
            dy = W - Math.abs(dy);
            if(dy > 0){
                dy *= -1;
            }
        }

        double angle = Math.atan2(dy,dx);
        p1.vel = v_max;
        p1.angle = angle - Math.PI;

        if(!p2.is_obstacle){
            p2.radius = p2.r_min;
            p2.vel = v_max;
            p2.angle = angle;
        }
    }

    public double getRadiusVelocity(){
        return v_max * Math.pow((radius-r_min)/(r_max-r_min), beta);
    }

    public int elude(CPMParticle toElude, double L, double W){

        if(!toElude.is_obstacle){
            return 0;
        }

        double dx = toElude.x - this.x;
        double dy = toElude.y - this.y;

        if ((W - Math.abs(dy) ) < Math.abs(dy)){
            dy = W - Math.abs(dy);
            if(dy > 0){
                dy *= -1;
            }
        }

        double dx_t = goal_x - this.x;
        double dy_t = goal_y - this.y;

        double dist = Math.hypot(dx,dy);

        double angleToElude = Math.atan2(dy,dx);
        double angleToTarget =  getAngleToTarget();
        //a = atan2d(x1*y2-y1*x2,x1*x2+y1*y2);
        double angle = Math.atan2(dx_t*dy-dy_t*dx, dx_t*dx+dy_t*dy);

        if(angle >= Math.PI/2 || angle <= -Math.PI/2) {
            // Do not elude
            this.angle = angleToTarget;
            return 0;
        }

        double coeff = ap * Math.exp(-dist/bp) * Math.cos(angle);


        double currentvx_direction = Math.cos(angleToTarget);
        double currentvy_direction = Math.sin(angleToTarget);

        double correctedAngle = angleToElude - Math.PI;

        double correctedvx = coeff * Math.cos(correctedAngle);
        double correctedvy = coeff * Math.sin(correctedAngle);

        double final_vx = currentvx_direction + correctedvx;
        double final_vy = currentvy_direction + correctedvy ;


        this.angle = Math.atan2(final_vy, final_vx);
        return 1;

    }

    public double getNextRadius(double dt) {
        double r = this.radius + r_max/(tau/dt);
        if(r >r_max){
            r = r_max;
        }
        return r;
    }

    public double getAngleToTarget() {
        double goalDx = goal_x - this.x;
        double goalDy = goal_y - this.y;
        return  Math.atan2(goalDy,goalDx);
    }
}
