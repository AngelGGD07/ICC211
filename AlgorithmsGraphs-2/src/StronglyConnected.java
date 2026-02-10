import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class StronglyConnected {
    private static int numberOfStronglyConnectedComponents(ArrayList<Integer>[] adj) {
        // Step 1: Compute post-order of the reversed graph
        ArrayList<Integer>[] reversedAdj = reverseGraph(adj);
        Stack<Integer> postOrder = new Stack<>();
        boolean[] visited = new boolean[adj.length];
        for (int i = 0; i < reversedAdj.length; i++) {
            if (!visited[i]) {
                dfs(reversedAdj, i, visited, postOrder);
            }
        }

        // Step 2: Traverse the original graph in post-order to find SCCs
        int sccCount = 0;
        visited = new boolean[adj.length];
        while (!postOrder.isEmpty()) {
            int v = postOrder.pop();
            if (!visited[v]) {
                explore(adj, v, visited);
                sccCount++;
            }
        }
        return sccCount;
    }

    private static ArrayList<Integer>[] reverseGraph(ArrayList<Integer>[] adj) {
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] reversed = (ArrayList<Integer>[]) new ArrayList[adj.length];
        for (int i = 0; i < adj.length; i++) {
            reversed[i] = new ArrayList<>();
        }
        for (int i = 0; i < adj.length; i++) {
            for (int neighbor : adj[i]) {
                reversed[neighbor].add(i);
            }
        }
        return reversed;
    }

    private static void dfs(ArrayList<Integer>[] adj, int v, boolean[] visited, Stack<Integer> postOrder) {
        visited[v] = true;
        for (int neighbor : adj[v]) {
            if (!visited[neighbor]) {
                dfs(adj, neighbor, visited, postOrder);
            }
        }
        postOrder.push(v);
    }

    private static void explore(ArrayList<Integer>[] adj, int v, boolean[] visited) {
        visited[v] = true;
        for (int neighbor : adj[v]) {
            if (!visited[neighbor]) {
                explore(adj, neighbor, visited);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
        }
        System.out.println(numberOfStronglyConnectedComponents(adj));
    }
}
