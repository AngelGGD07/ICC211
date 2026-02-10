import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Toposort {
    private static ArrayList<Integer> toposort(ArrayList<Integer>[] adj) {
        // 0: unvisited, 1: visiting, 2: visited
        int used[] = new int[adj.length];
        ArrayList<Integer> order = new ArrayList<Integer>();
        for (int i = 0; i < adj.length; i++) {
            if (used[i] == 0) {
                if (!dfs(adj, used, order, i)) {
                    // Cycle detected, topological sort not possible
                    return new ArrayList<Integer>();
                }
            }
        }
        Collections.reverse(order);
        return order;
    }

    private static boolean dfs(ArrayList<Integer>[] adj, int[] used, ArrayList<Integer> order, int s) {
        used[s] = 1; // Mark as visiting
        for (int neighbor : adj[s]) {
            if (used[neighbor] == 1) {
                // Cycle detected
                return false;
            }
            if (used[neighbor] == 0) {
                if (!dfs(adj, used, order, neighbor)) {
                    return false;
                }
            }
        }
        used[s] = 2; // Mark as visited
        order.add(s);
        return true;
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
        ArrayList<Integer> order = toposort(adj);
        if (order.isEmpty()) {
            System.out.println("Error: Graph contains a cycle.");
        } else {
            for (int x : order) {
                System.out.print((x + 1) + " ");
            }
        }
    }
}
