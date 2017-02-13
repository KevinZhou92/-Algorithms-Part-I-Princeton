import java.util.ArrayList;
import java.util.Arrays;

/**
 * A BruteForce solution for finding collinear points in a set of points.
 *
 * @author Pengcheng Zhou
 */
public class BruteCollinearPoints {
    
    private LineSegment[] seg;
    private int num;
    private Point min;
    private Point max;
    
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();
        int N = points.length;
        seg = new LineSegment[N];
        num = 0;
        
        for (int i = 0; i < N; i++) {
            if (points[i] == null)
                throw new NullPointerException();
        }
        
        for (int a = 0; a < N; a++) {
            for (int b = a + 1; b < N; b++) {
                min = points[a];
                max = points[a];
                if (points[b].compareTo(points[a]) == 0)
                    throw new IllegalArgumentException();
                if (points[b].compareTo(min) < 0) 
                    min = points[b];
                else if (points[b].compareTo(max) > 0)
                    max = points[b];
                for (int c = b + 1; c < N; c++) {
                    if (points[a].slopeTo(points[b]) == points[a].slopeTo(points[c])) {
                        if (points[c].compareTo(min) < 0)
                            min = points[c];
                        if (points[c].compareTo(max) > 0)
                            max = points[c];                       
                        for (int d = c + 1; d < N; d++) {
                            if (points[a].slopeTo(points[b]) == points[a].slopeTo(points[d])) {
                                if (points[d].compareTo(min) < 0)
                                    min = points[d];
                                if (points[d].compareTo(max) > 0)
                                    max = points[d];                                
                                seg[num++] = new LineSegment(min, max);                             
                            }    
                        }
                    }
                }
            }
        }
    }
    
    public int numberOfSegments() {
        return num;   
    }
    
    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[num];
        for (int i = 0; i < num; i++) 
            result[i] = seg[i];
        return result;
    }

}