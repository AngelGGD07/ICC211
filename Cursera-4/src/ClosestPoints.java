import java.io.*;
import java.util.*;
import static java.lang.Math.*;

public class ClosestPoints {

    static class Point implements Comparable<Point> {
        long x, y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point o) {
            return o.y == y ? Long.compare(x, o.x) : Long.compare(y, o.y);
        }
    }

    static double minimalDistance(int[] x, int y[]) {
        Point[] points = new Point[x.length];
        for(int i=0; i<x.length; i++){
            points[i] = new Point(x[i], y[i]);
        }
        Arrays.sort(points, (p1, p2) -> Long.compare(p1.x, p2.x));
        return minimalDistance(points, 0, points.length - 1);
    }

    static double minimalDistance(Point[] points, int l, int r){
        if(l >= r){
            return Double.POSITIVE_INFINITY;
        }
        int mid = l + (r - l) / 2;
        double d1 = minimalDistance(points, l, mid);
        double d2 = minimalDistance(points, mid + 1, r);
        double d = Math.min(d1, d2);

        List<Point> strip = new ArrayList<>();
        for(int i=l; i<=r; i++){
            if(Math.abs(points[i].x - points[mid].x) < d){
                strip.add(points[i]);
            }
        }
        Collections.sort(strip);
        for(int i=0; i<strip.size(); i++){
            for(int j=i+1; j<strip.size() && (strip.get(j).y - strip.get(i).y) < d; j++){
                d = Math.min(d, dist(strip.get(i), strip.get(j)));
            }
        }
        return d;
    }

    static double dist(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    public static void main(String[] args) throws Exception {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new PrintWriter(System.out);
        int n = nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = nextInt();
            y[i] = nextInt();
        }
        System.out.println(minimalDistance(x, y));
        writer.close();
    }

    static BufferedReader reader;
    static PrintWriter writer;
    static StringTokenizer tok = new StringTokenizer("");


    static String next() {
        while (!tok.hasMoreTokens()) {
            String w = null;
            try {
                w = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (w == null)
                return null;
            tok = new StringTokenizer(w);
        }
        return tok.nextToken();
    }

    static int nextInt() {
        return Integer.parseInt(next());
    }
}