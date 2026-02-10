import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Reachability {
    private static int reach(ArrayList<Integer>[] adj, int x, int y) {
        boolean[] visited = new boolean[adj.length];
        dfs(adj, x, visited);
        return visited[y] ? 1 : 0;
    }

    private static void dfs(ArrayList<Integer>[] adj, int v, boolean[] visited) {
        visited[v] = true;
        for (int neighbor : adj[v]) {
            if (!visited[neighbor]) {
                dfs(adj, neighbor, visited);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        int n = Integer.parseInt(tokenizer.nextToken());
        int m = Integer.parseInt(tokenizer.nextToken());
        
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        
        for (int i = 0; i < m; i++) {
            tokenizer = new StringTokenizer(reader.readLine());
            int u, v;
            u = Integer.parseInt(tokenizer.nextToken());
            v = Integer.parseInt(tokenizer.nextToken());
            adj[u - 1].add(v - 1);
            adj[v - 1].add(u - 1);
        }
        
        tokenizer = new StringTokenizer(reader.readLine());
        int x = Integer.parseInt(tokenizer.nextToken()) - 1;
        int y = Integer.parseInt(tokenizer.nextToken()) - 1;
        
        System.out.println(reach(adj, x, y));
    }
}
