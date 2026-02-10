import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class NegativeCycle {
    private static int negativeCycle(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost) {
        int n = adj.length;
        long[] dist = new long[n];
        // Initialize distances to 0. This is equivalent to having a "super source"
        // connected to all vertices with 0-weight edges.
        Arrays.fill(dist, 0);

        // Relax all edges n-1 times.
        for (int i = 0; i < n - 1; i++) {
            for (int u = 0; u < n; u++) {
                for (int j = 0; j < adj[u].size(); j++) {
                    int v = adj[u].get(j);
                    int w = cost[u].get(j);
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                    }
                }
            }
        }

        // Perform one final relaxation. If any distance is updated, a negative cycle exists.
        for (int u = 0; u < n; u++) {
            for (int j = 0; j < adj[u].size(); j++) {
                int v = adj[u].get(j);
                int w = cost[u].get(j);
                if (dist[u] + w < dist[v]) {
                    return 1; // Negative cycle found
                }
            }
        }

        return 0; // No negative cycle
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
            cost[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
        }
        System.out.println(negativeCycle(adj, cost));
        scanner.close();
    }
}
