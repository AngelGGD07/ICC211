import java.util.ArrayList;
import java.util.Scanner;

public class Acyclicity {
    private static int acyclic(ArrayList<Integer>[] adj) {
        int[] visited = new int[adj.length]; // 0: unvisited, 1: visiting, 2: visited
        for (int i = 0; i < adj.length; i++) {
            if (visited[i] == 0) {
                if (dfs(adj, i, visited)) {
                    return 1; // Cycle detected
                }
            }
        }
        return 0; // No cycle detected
    }

    private static boolean dfs(ArrayList<Integer>[] adj, int u, int[] visited) {
        visited[u] = 1; // Mark as visiting

        for (int v : adj[u]) {
            if (visited[v] == 1) {
                return true; // Cycle detected (back edge)
            }
            if (visited[v] == 0) {
                if (dfs(adj, v, visited)) {
                    return true;
                }
            }
        }

        visited[u] = 2; // Mark as visited
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
        }
        System.out.println(acyclic(adj));
    }
}
