import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Arrays;

public class FastCollinearPoints {
    
    private LineSegment[] seg;
    private int num;
    private int N;
    
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();
        
        N = points.length;
        seg = new LineSegment[N];
        Point[] other  = new Point[N];
        Point[] base = new Point[N];
        Point current;
        Point max;
        Point min;
        num = 0;

        for (int i = 0; i < N; i++) {
            if (points[i] == null)
                throw new NullPointerException();
            for (int j = i+1; j < N; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
            other[i] = points[i];
            base[i] = points[i];
        }   
        
        Arrays.sort(base);
        
        for (int i = 0, j = 1; i < N; i++, j = 1) {
            current = base[i];
            max = base[i];
            min = base[i];
            Arrays.sort(other, current.slopeOrder()); // sort based on slope, the point to its self is Negative-Inifinity
            while (j < N-2){
                if (current.slopeTo(other[j]) != current.slopeTo(other[j+2])) { // check if there are at least 4 collinear points 
                    j++; 
                    continue;
                }
                for(int offset = 0; offset < N-j && current.slopeTo(other[j]) == current.slopeTo(other[j+offset]); offset++){ // find the endpoint and get the maximal combination
                    if (other[j+offset].compareTo(max) > 0) max = other[j+offset];
                    if (other[j+offset].compareTo(min) < 0) min = other[j+offset];
                }
                
                if (min != current) continue; //check if this combination appeaared before;
                
                LineSegment temp = new LineSegment(min, max);
                if (seg[seg.length-1] != null) changeSize(seg.length, 2*seg.length);
                seg[num++] = temp;
                j++;
            }
        }
    }
    
    private void changeSize(int oldSize, int newSize) {
        LineSegment[] newseg = new LineSegment[newSize];
        for (int i = 0; i < oldSize; i++) {
            newseg[i] = seg[i];
        }
        seg = newseg;
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