import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class AirlineCrews {
    private FastScanner in;
    private PrintWriter out;
    private int numLeft;
    private int numRight;

    public static void main(String[] args) throws IOException {
        new AirlineCrews().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        List<Integer>[] adj = readData();
        int[] matching = findMatching(adj);
        writeResponse(matching);
        out.close();
    }

    @SuppressWarnings("unchecked")
    List<Integer>[] readData() throws IOException {
        this.numLeft = in.nextInt();
        this.numRight = in.nextInt();
        List<Integer>[] adj = new ArrayList[numLeft];
        for (int i = 0; i < numLeft; ++i) {
            adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < numLeft; ++i) {
            for (int j = 0; j < numRight; ++j) {
                if (in.nextInt() == 1) {
                    adj[i].add(j);
                }
            }
        }
        return adj;
    }

    private int[] findMatching(List<Integer>[] adj) {
        int[] pairU = new int[numLeft];
        int[] pairV = new int[numRight];
        Arrays.fill(pairU, -1);
        Arrays.fill(pairV, -1);

        int[] dist = new int[numLeft];

        while (bfs(adj, pairU, pairV, dist)) {
            for (int u = 0; u < numLeft; u++) {
                if (pairU[u] == -1) {
                    dfs(u, adj, pairU, pairV, dist);
                }
            }
        }
        return pairU;
    }

    private boolean bfs(List<Integer>[] adj, int[] pairU, int[] pairV, int[] dist) {
        Queue<Integer> queue = new LinkedList<>();
        boolean foundPath = false;

        for (int u = 0; u < numLeft; u++) {
            if (pairU[u] == -1) {
                dist[u] = 0;
                queue.add(u);
            } else {
                dist[u] = Integer.MAX_VALUE;
            }
        }

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : adj[u]) {
                int matchedU = pairV[v];
                if (matchedU == -1) {
                    foundPath = true;
                } else if (dist[matchedU] == Integer.MAX_VALUE) {
                    dist[matchedU] = dist[u] + 1;
                    queue.add(matchedU);
                }
            }
        }
        return foundPath;
    }

    private boolean dfs(int u, List<Integer>[] adj, int[] pairU, int[] pairV, int[] dist) {
        for (int v : adj[u]) {
            int matchedU = pairV[v];
            if (matchedU == -1 || (dist[matchedU] == dist[u] + 1 && dfs(matchedU, adj, pairU, pairV, dist))) {
                pairV[v] = u;
                pairU[u] = v;
                return true;
            }
        }
        dist[u] = Integer.MAX_VALUE;
        return false;
    }

    private void writeResponse(int[] matching) {
        for (int i = 0; i < matching.length; ++i) {
            if (i > 0) {
                out.print(" ");
            }
            if (matching[i] == -1) {
                out.print("-1");
            } else {
                out.print(matching[i] + 1);
            }
        }
        out.println();
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
