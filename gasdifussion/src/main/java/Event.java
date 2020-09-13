import java.util.List;
import java.util.Set;

public abstract class Event implements Comparable<Event> {

    final double time;

    /* Package Private */
    Event(final double time) {
        this.time = time;
    }

    public int compareTo(final Event otherEvent) {
        if (this.time < otherEvent.time) {
            return -1;
        }
        if (this.time > otherEvent.time) {
            return 1;
        }
        else return 0;
    }

    public double getTime() {
        return time;
    }

    public abstract void execute();

    public abstract double getPressure();

    /*
    Checks if particle participates in event
     */
    public abstract boolean participates(Particle p);

    public abstract List<Particle> getParticipants();
}
