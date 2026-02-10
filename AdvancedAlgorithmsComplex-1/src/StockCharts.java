import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class StockCharts {
    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new StockCharts().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        int[][] stockData = readData();
        int result = minCharts(stockData);
        writeResponse(result);
        out.close();
    }

    int[][] readData() throws IOException {
        int numStocks = in.nextInt();
        int numPoints = in.nextInt();
        int[][] stockData = new int[numStocks][numPoints];
        for (int i = 0; i < numStocks; ++i)
            for (int j = 0; j < numPoints; ++j)
                stockData[i][j] = in.nextInt();
        return stockData;
    }

    private int minCharts(int[][] stockData) {
        int numStocks = stockData.length;
        if (numStocks == 0) {
            return 0;
        }

        // Build the DAG: u -> v if stock u is strictly below stock v.
        // This will be treated as the adjacency list for our bipartite graph.
        List<Integer>[] adj = new ArrayList[numStocks];
        for (int i = 0; i < numStocks; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int i = 0; i < numStocks; i++) {
            for (int j = 0; j < numStocks; j++) {
                if (i == j) continue;
                if (compare(stockData[i], stockData[j])) {
                    adj[i].add(j);
                }
            }
        }

        // The minimum path cover in a DAG is n - |M|, where |M| is the
        // size of the maximum matching in the corresponding bipartite graph.
        int maxMatchingSize = findMaxMatching(adj, numStocks);

        return numStocks - maxMatchingSize;
    }

    private int findMaxMatching(List<Integer>[] adj, int numStocks) {
        int[] matchR = new int[numStocks];
        Arrays.fill(matchR, -1);
        int result = 0;
        for (int u = 0; u < numStocks; u++) {
            boolean[] visited = new boolean[numStocks];
            if (dfs(u, adj, matchR, visited)) {
                result++;
            }
        }
        return result;
    }

    private boolean dfs(int u, List<Integer>[] adj, int[] matchR, boolean[] visited) {
        for (int v : adj[u]) {
            if (!visited[v]) {
                visited[v] = true;
                if (matchR[v] < 0 || dfs(matchR[v], adj, matchR, visited)) {
                    matchR[v] = u;
                    return true;
                }
            }
        }
        return false;
    }

    boolean compare(int[] stock1, int[] stock2) {
        for (int i = 0; i < stock1.length; ++i)
            if (stock1[i] >= stock2[i])
                return false;
        return true;
    }

    private void writeResponse(int result) {
        out.println(result);
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
