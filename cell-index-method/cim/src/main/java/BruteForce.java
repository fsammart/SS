import java.util.List;

public class BruteForce {
    public static void compute(int N, List<Particle> particles, boolean periodicContour, double interactionRadius) {
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N & j != i; j++) {
                Particle p1 = particles.get(i);
                Particle p2 = particles.get(j);
                double distance;

                if (periodicContour){
                    distance = p1.contourDistanceTo(p2);
                }else{
                    distance = p1.distanceTo(p2);
                }

                if (distance < interactionRadius){
                    p1.addNeighbour(p2);
                    p2.addNeighbour(p1);
                }

            }
        }
    }
}
