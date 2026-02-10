import java.util.PriorityQueue;
import java.util.Scanner;

public class ConnectingPoints {

    // Inner class to represent an edge leading to a point with a certain distance (weight)
    private static class Edge implements Comparable<Edge> {
        int pointIndex;
        double distance;

        Edge(int pointIndex, double distance) {
            this.pointIndex = pointIndex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    private static double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private static double minimumDistance(int[] x, int[] y) {
        int n = x.length;
        double[] minDistance = new double[n];
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            minDistance[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
        }

        minDistance[0] = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        pq.add(new Edge(0, 0.0));

        double totalWeight = 0;

        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            int u = edge.pointIndex;

            if (visited[u]) {
                continue;
            }

            visited[u] = true;
            totalWeight += edge.distance;

            for (int v = 0; v < n; v++) {
                if (!visited[v]) {
                    double dist = calculateDistance(x[u], y[u], x[v], y[v]);
                    if (dist < minDistance[v]) {
                        minDistance[v] = dist;
                        pq.add(new Edge(v, dist));
                    }
                }
            }
        }

        return totalWeight;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();
        }
        System.out.printf("%.9f%n", minimumDistance(x, y));
        scanner.close();
    }
}
