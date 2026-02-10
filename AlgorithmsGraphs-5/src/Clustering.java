import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Clustering {

    private static class Edge implements Comparable<Edge> {
        int u, v;
        double weightSq; // Use squared weight for comparison to avoid sqrt

        Edge(int u, int v, double weightSq) {
            this.u = u;
            this.v = v;
            this.weightSq = weightSq;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.weightSq, other.weightSq);
        }
    }

    private static class DSU {
        private int[] parent;
        private int[] rank;
        private int count;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            count = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]); // Path compression
        }

        void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                // Union by rank
                if (rank[rootI] < rank[rootJ]) {
                    parent[rootI] = rootJ;
                } else if (rank[rootI] > rank[rootJ]) {
                    parent[rootJ] = rootI;
                } else {
                    parent[rootJ] = rootI;
                    rank[rootI]++;
                }
                count--;
            }
        }

        int getCount() {
            return count;
        }
    }

    private static double clustering(int[] x, int[] y, int k) {
        int n = x.length;
        if (n < k) {
            return -1.0;
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Use squared Euclidean distance to avoid sqrt in comparisons
                long dx = (long) x[i] - x[j];
                long dy = (long) y[i] - y[j];
                double distSq = dx * dx + dy * dy;
                edges.add(new Edge(i, j, distSq));
            }
        }

        Collections.sort(edges);

        DSU dsu = new DSU(n);

        for (Edge edge : edges) {
            if (dsu.find(edge.u) != dsu.find(edge.v)) {
                if (dsu.getCount() == k) {
                    return Math.sqrt(edge.weightSq);
                }
                dsu.union(edge.u, edge.v);
            }
        }

        // This case is typically reached when k=1. The problem is about the distance
        // that separates clusters. If there's only one cluster, this is ill-defined.
        // Returning -1.0 as per the original snippet's default.
        return -1.0;
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
        int k = scanner.nextInt();
        System.out.printf("%.9f%n", clustering(x, y, k));
        scanner.close();
    }
}
