import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Bipartite {
    private static int bipartite(ArrayList<Integer>[] adj) {
        int[] colors = new int[adj.length];
        Arrays.fill(colors, 0); 

        for (int i = 0; i < adj.length; i++) {
            if (colors[i] == 0) {
                Queue<Integer> queue = new LinkedList<>();
                queue.add(i);
                colors[i] = 1;

                while (!queue.isEmpty()) {
                    int u = queue.poll();

                    for (int v : adj[u]) {
                        if (colors[v] == 0) {
                            colors[v] = -colors[u];
                            queue.add(v);
                        } else if (colors[v] == colors[u]) {
                            return 0;
                        }
                    }
                }
            }
        }
        return 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adj[y - 1].add(x - 1);
        }
        System.out.println(bipartite(adj));
    }
}
