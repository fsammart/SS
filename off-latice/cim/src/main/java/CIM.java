import java.io.File;
import java.nio.file.AtomicMoveNotSupportedException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class CIM {
    // implements CIM method

    public static List<List<Particle>> cells;
    public static boolean periodicContour;
    public static int M;
    public static double L;
    public static double interactionRadius;

    private static int  getMValue(double L, double maxRadius1, double maxRadius2) {
        return (int) Math.ceil(L / (interactionRadius +  maxRadius1 + maxRadius2)) - 1;
    }

    public static void compute(List<? extends Particle> particles,
                               double L, boolean periodicContour, double interactionRadius, double maxRadius){
        CIM.periodicContour = periodicContour;
        CIM.L = L;
        CIM.interactionRadius = interactionRadius;
        CIM.M = getMValue(L, maxRadius, maxRadius);

        checkLMCondition(L,M,interactionRadius, maxRadius, maxRadius);
        populateMatrix(particles, M, L);
        cim();
    }

    public static void compute(List<? extends Particle> particles,
                               double L, boolean periodicContour,
                               double interactionRadius, double maxRadius1, double maxRadius2){
        CIM.periodicContour = periodicContour;
        CIM.L = L;
        CIM.interactionRadius = interactionRadius;
        CIM.M = getMValue(L, maxRadius1, maxRadius2);

        checkLMCondition(L,M,interactionRadius, maxRadius1, maxRadius2);
        populateMatrix(particles, M, L);
        cim();
    }

    public static boolean checkLMCondition( double L, int M,
                                            double interactionRadius, double maxRadius1, double maxRadius2){
        assert(L > 0);
        assert (M > 0);
        assert (interactionRadius >= 0);
        /* check for worst condition when 2 particles with the greatest radius)
        * If radius is 0, then this condition is also guaranteed*/
        return L > (interactionRadius + (maxRadius1 + maxRadius2)) * M;
    }

    public static void cim(){

        cells.stream().filter(particles -> !particles.isEmpty()).forEach(particles -> {

            int cellX = particles.get(0).getCellX();
            int cellY = particles.get(0).getCellY();

            particles.stream().forEach(p -> {
                findNeighbours(p, cellX, cellY);
                findNeighbours(p, cellX, cellY + 1);
                findNeighbours(p, cellX + 1, cellY + 1);
                findNeighbours(p, cellX + 1, cellY);
                findNeighbours(p, cellX + 1, cellY - 1);
            });
        });
    }

    public static void findNeighbours(Particle p, int cellX, int cellY){


        if (periodicContour) {
            cellX = (cellX + M) % M;
            cellY = (cellY + M) % M;

        } else {
            if (cellX >= M || cellX < 0 || cellY >= M || cellY < 0) {
                // No neighbours to find outside matrix
                return;
            }
        }

        int adjCellNumber = cellY * M + cellX;

        List<Particle> cellParticles = cells.get(adjCellNumber);

        for (Particle adjacentParticle : cellParticles){

            if (adjacentParticle.id != p.id){ // Avoid checking the same particle

                double distance;

                if (periodicContour){
                    distance = p.contourDistanceTo(adjacentParticle);
                } else {
                    distance = p.distanceTo(adjacentParticle);
                }

                if (distance < interactionRadius){
                    p.addNeighbour(adjacentParticle);
                    adjacentParticle.addNeighbour(p);
                }

            }
        }

    }

    public static void populateMatrix(List<? extends Particle> particles, int M, double L){
        cells = new ArrayList<List<Particle>>(M*M);

        for (int i = 0; i < M*M ; i++){
            cells.add(new ArrayList<>(particles.size()));
        }

        double lm = L/M;
        particles.stream().forEach(p -> {
            int cellX = (int) Math.floor(p.getX() / lm);
            int cellY = (int) Math.floor(p.getY() / lm);
            int cellNumber = cellY * M + cellX;

            List <Particle> cellParticles = cells.get(cellNumber);
            p.setCellX(cellX);
            p.setCellY(cellY);
            cellParticles.add(p);
        });
    }

}
