
/**
 * @author aliHaydarSucu 23050111019
 */
import java.util.ArrayList;
import java.util.List;

public abstract class Geometry {

    protected List<Point> points;

    public Geometry(List<Point> points) {
        this.points = new ArrayList<>(points);
    }

    public List<Point> getPoints() {
        return new ArrayList<>(points);
    }

    public abstract double calculateArea();

    public int size() {
        return points.size();
    }
}
