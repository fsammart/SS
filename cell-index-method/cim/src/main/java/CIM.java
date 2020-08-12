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

    public static void compute(List<Particle> particles,
                               int M, double L, boolean periodicContour, double interactionRadius){
        CIM.periodicContour = periodicContour;
        CIM.L = L;
        CIM.M = M;
        CIM.interactionRadius = interactionRadius;

        if(interactionRadius == 0){
            cells = new LinkedList<>();
        }
        checkLMCondition(L,M,interactionRadius);
        populateMatrix(particles, M, L);
        cim();
    }
    public static boolean checkLMCondition( double L, int M, double interactionRadius){
        assert(L > 0);
        assert (M > 0);
        assert (interactionRadius >= 0);
        return L > interactionRadius * M;
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

    public static void populateMatrix(List<Particle> particles, int M, double L){
        cells = new ArrayList<List<Particle>>();

        for (int i = 0; i < M*M ; i++){
            cells.add(new ArrayList<>());
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
