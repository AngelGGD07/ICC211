import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.StringTokenizer;

public class SchoolBus {
    private static FastScanner in;
    private static int INF = 1000 * 1000 * 1000;

    public static void main(String[] args) {
        in = new FastScanner();
        try {
            printAnswer(SchoolBus(readData()));
        } catch (IOException exception) {
            System.err.print("Error during reading: " + exception.toString());
        }
    }

    static Answer SchoolBus(int[][] graph) {
        int n = graph.length;
        // dp[mask][i] stores the min distance to visit set 'mask', ending at city 'i'
        // mask is a bitmask where the j-th bit is set if city j has been visited
        // We always start at city 0 (bit 0 is always set)
        int[][] dp = new int[1 << n][n];

        // Initialize DP table
        for (int[] row : dp) Arrays.fill(row, INF);

        // Base case: starting at city 0, mask is just {0} (value 1)
        dp[1][0] = 0;

        // Iterate through all masks size 2 to N
        // We assume mask always includes bit 0 (start city)
        for (int mask = 1; mask < (1 << n); mask += 2) {
            // Try to extend to next city 'next'
            for (int u = 0; u < n; u++) {
                // If u is in the current set (mask) and we have a valid path to u
                if (((mask >> u) & 1) != 0 && dp[mask][u] != INF) {

                    // Try to move to v
                    for (int v = 0; v < n; v++) {
                        // If v is NOT in the mask
                        if (((mask >> v) & 1) == 0) {
                            int nextMask = mask | (1 << v);
                            int dist = dp[mask][u] + graph[u][v];
                            if (dist < dp[nextMask][v]) {
                                dp[nextMask][v] = dist;
                            }
                        }
                    }
                }
            }
        }

        // Find the best tour returning to 0
        int minCost = INF;
        int lastNode = -1;
        int fullMask = (1 << n) - 1;

        for (int i = 1; i < n; i++) {
            if (dp[fullMask][i] != INF && graph[i][0] != INF) {
                int tourCost = dp[fullMask][i] + graph[i][0];
                if (tourCost < minCost) {
                    minCost = tourCost;
                    lastNode = i;
                }
            }
        }

        if (minCost == INF)
            return new Answer(-1, new ArrayList<>());

        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        int currMask = fullMask;
        int currNode = lastNode;

        // The path ends at currNode, then goes to 0 (which we print implied at start/end)
        // But the format asks for 1..n order. 
        // We backtrack from the end.

        while (currNode != 0) {
            path.add(0, currNode + 1); // Add to front, 1-based
            int prevMask = currMask ^ (1 << currNode);
            int bestPrev = -1;

            for (int prev = 0; prev < n; prev++) {
                if (((prevMask >> prev) & 1) != 0) {
                    if (dp[prevMask][prev] + graph[prev][currNode] == dp[currMask][currNode]) {
                        bestPrev = prev;
                        break;
                    }
                }
            }
            currMask = prevMask;
            currNode = bestPrev;
        }
        path.add(0, 1); // Add start node

        return new Answer(minCost, path);
    }

    private static int[][] readData() throws IOException {
        int n = in.nextInt();
        int m = in.nextInt();
        int[][] graph = new int[n][n];

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                graph[i][j] = INF;

        for (int i = 0; i < m; ++i) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            int weight = in.nextInt();
            graph[u][v] = graph[v][u] = weight;
        }
        return graph;
    }

    private static void printAnswer(final Answer answer) {
        System.out.println(answer.weight);
        if (answer.weight == -1)
            return;
        for (int x : answer.path)
            System.out.print(x + " ");
        System.out.println();
    }

    static class Answer {
        int weight;
        List<Integer> path;

        public Answer(int weight, List<Integer> path) {
            this.weight = weight;
            this.path = path;
        }
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