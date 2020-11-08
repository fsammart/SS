import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class CPM {

    public double L; //x axis
    public double W; // y axis
    public double minRadius,maxRadius, x_goal, y_goal, vMax;

    public CPM(double L, double W, double minRadius,
               double maxRadius, double x_goal, double y_goal,
               double vMax, double ap, double bp, double beta, double tau) {
        this.L = L;
        this.W = W;
        this.maxRadius = maxRadius;
        this.minRadius = minRadius;
        this.x_goal = x_goal;
        this.y_goal = y_goal;
        this.vMax = vMax;
        CPMParticle.v_max = vMax;
        CPMParticle.r_min = minRadius;
        CPMParticle.r_max = maxRadius;
        CPMParticle.ap = ap;
        CPMParticle.bp = bp;
        CPMParticle.beta = beta;
        CPMParticle.goal_x = x_goal;
        CPMParticle.goal_y = y_goal;
        CPMParticle.tau = tau;

    }



    public void simulate(CPMParticle particle, double dt, double dt2,
                         List<CPMParticle> obstacles, double time, boolean printPosition) throws IOException {
        double current_time = 0;
        Set<CPMParticle> escaped = new HashSet<>();
        Set<CPMParticle> reached = new HashSet<>();
        double ir = -Math.log(0.01)*CPMParticle.bp;
        int collisions = 0;
        double distance = 0;
        double vel_avg =0;
        int i = 0;
        while (current_time <= time) {
            if (printPosition) {
                System.out.println("TIME -----" + current_time);
                dynamicFile(particle,obstacles, i, L, W);
                i++;
            }

            escaped.clear();
            reached.clear();

            boolean collision = false;

            List<CPMParticle> ordered = obstacles.
                    stream().sorted((x,y) -> Double.compare(particle.contourDistanceTo(x,L,W) ,particle.contourDistanceTo(y,L,W)))
                    .collect(Collectors.toList());
            CPMParticle closest = ordered.get(0);

            if(particle.distanceTo(closest) <= (CPMParticle.r_max - particle.radius) ){
                CPMParticle.escape(particle, closest,L,W);
                collisions++;
                collision = true;
            }
            if(!collision){
                for(CPMParticle obs: ordered){
                    //elude
                    int res = particle.elude(closest, L, W);
                    if(res == 1) {
                        break;
                    }
                }
                particle.vel = particle.getRadiusVelocity();
                particle.radius = particle.getNextRadius(dt);
            }

            distance += particle.move(dt, L, W);
            obstacles.stream().forEach(o -> {
                o.move(dt, L, W);
            });

            vel_avg += particle.vel;

            if (particle.distanceToGoal() < (maxRadius)) {
                //reached
                System.out.println("Particle reached goal:" + current_time);
                printStatistics(current_time,collisions,distance, CPMParticle.ap, CPMParticle.bp,vel_avg/current_time);
                return;
            }


            // take the ones that reached the goal

            current_time += dt;
        }
    }

    public  void dynamicFile(CPMParticle particle, List<? extends Particle> l, int time, double L, double W) throws IOException {
        File file = new File("simulation/t" + "-" + time + ".xyz");
        if(file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.format("%d\n\n", l.size() + 4 + 1);
            writer.println("0 0 0 0 0 0.01 0");
            writer.println(L + " 0 0 0 0 0.01 0");
            writer.print("0 " + W+ " 0 0 0 0.01 0");

            l.forEach(p -> {
                writer.println("");
                CPMParticle sp = (CPMParticle) p;
                double vx = sp.vel * Math.cos(sp.angle);
                double vy = sp.vel * Math.sin(sp.angle);

                String category = "2";

                writer.print(String.format(Locale.US,"%.3f", sp.getX()) + " "
                        + String.format(Locale.US,"%.3f", sp.getY()) + " "
                        + String.format(Locale.US,"%.3f", vx) + " "
                        + String.format(Locale.US,"%.3f", vy) + " " + sp.angle + " " + sp.radius + " "
                        +category);
            });
            writer.println("");
            //Add Particle
            double vx = particle.vel * Math.cos(particle.angle);
            double vy = particle.vel * Math.sin(particle.angle);
            writer.print(String.format(Locale.US,"%.3f",particle.x) + " "
                    + String.format(Locale.US,"%.3f",particle.y) + " "
                    + String.format(Locale.US,"%.3f", vx) + " "
                    + String.format(Locale.US,"%.3f", vy) + " "
                    + particle.angle + " "+ particle.radius + " " + "1");
            writer.println("");
            //Add Target
            writer.print(String.format(Locale.US,"%.3f",x_goal) + " "
                    + String.format(Locale.US,"%.3f",y_goal) + " "
                    + String.format(Locale.US,"%.3f", 0.0) + " "
                    + String.format(Locale.US,"%.3f", 0.0) + " " + "0.0 0.4" + " " + "3");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);

        }
    }

    public  void printStatistics(double current_time, int collisions, double distance, double a, double v, double vel) throws IOException {
        File file = new File("results/statistics.tsv");
        if(file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fr = new FileWriter(file, true);
        BufferedWriter br = new BufferedWriter(fr);
        try (PrintWriter writer = new PrintWriter(br)) {
            writer.format("%f %d %f %f %f %f\n", current_time, collisions, distance, a , v, vel);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);

        }
        br.close();
        fr.close();
    }
}
