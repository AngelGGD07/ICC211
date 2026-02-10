import java.util.*;

public class ShortestPaths {

    private static void shortestPaths(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, long[] distance, int[] reachable, int[] shortest) {
        int n = adj.length;
        distance[s] = 0;
        reachable[s] = 1;

        // Relax edges for n-1 iterations to find shortest paths.
        for (int i = 0; i < n - 1; i++) {
            for (int u = 0; u < n; u++) {
                if (distance[u] != Long.MAX_VALUE) {
                    for (int j = 0; j < adj[u].size(); j++) {
                        int v = adj[u].get(j);
                        int w = cost[u].get(j);
                        if (distance[u] + w < distance[v]) {
                            distance[v] = distance[u] + w;
                            reachable[v] = 1;
                        }
                    }
                }
            }
        }

        // n-th iteration to find nodes affected by negative cycles.
        Queue<Integer> negativeCycleQueue = new LinkedList<>();
        for (int u = 0; u < n; u++) {
            if (distance[u] != Long.MAX_VALUE) {
                for (int j = 0; j < adj[u].size(); j++) {
                    int v = adj[u].get(j);
                    int w = cost[u].get(j);
                    if (distance[u] + w < distance[v]) {
                        // A shorter path found implies a negative cycle is on the path from s to v.
                        if (shortest[v] == 1) {
                            negativeCycleQueue.add(v);
                            shortest[v] = 0;
                        }
                    }
                }
            }
        }

        // Propagate the "no shortest path" property to all reachable nodes via BFS.
        while (!negativeCycleQueue.isEmpty()) {
            int u = negativeCycleQueue.poll();
            for (int v : adj[u]) {
                if (shortest[v] == 1) {
                    shortest[v] = 0;
                    negativeCycleQueue.add(v);
                }
            }
        }
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
        int s = scanner.nextInt() - 1;
        long[] distance = new long[n];
        int[] reachable = new int[n];
        int[] shortest = new int[n];
        for (int i = 0; i < n; i++) {
            distance[i] = Long.MAX_VALUE;
            reachable[i] = 0;
            shortest[i] = 1;
        }

        shortestPaths(adj, cost, s, distance, reachable, shortest);

        for (int i = 0; i < n; i++) {
            if (reachable[i] == 0) {
                System.out.println('*');
            } else if (shortest[i] == 0) {
                System.out.println('-');
            } else {
                System.out.println(distance[i]);
            }
        }
        scanner.close();
    }
}
