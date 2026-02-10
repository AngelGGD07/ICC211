import java.io.*;
import java.util.*;

class Vertex {
    Vertex() {
        this.weight = 0;
        this.children = new ArrayList<Integer>();
    }

    int weight;
    ArrayList<Integer> children;
}

class PlanParty {
    static Vertex[] ReadTree() throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.nextToken();
        int vertices_count = (int) tokenizer.nval;

        Vertex[] tree = new Vertex[vertices_count];

        for (int i = 0; i < vertices_count; ++i) {
            tree[i] = new Vertex();
            tokenizer.nextToken();
            tree[i].weight = (int) tokenizer.nval;
        }

        for (int i = 1; i < vertices_count; ++i) {
            tokenizer.nextToken();
            int from = (int) tokenizer.nval;
            tokenizer.nextToken();
            int to = (int) tokenizer.nval;
            tree[from - 1].children.add(to - 1);
            tree[to - 1].children.add(from - 1);
        }

        return tree;
    }

    // dp[u][0] = Max weight of subtree at u, excluding u
    // dp[u][1] = Max weight of subtree at u, including u
    static long[][] dp;
    static boolean[] visited;

    static void dfs(Vertex[] tree, int u, int parent) {
        // Base case initialization
        dp[u][1] = tree[u].weight;
        dp[u][0] = 0;

        for (int v : tree[u].children) {
            if (v != parent) {
                dfs(tree, v, u);

                // If we include u, we cannot include any child v
                dp[u][1] += dp[v][0];

                // If we exclude u, we can either include or exclude v, whichever is better
                dp[u][0] += Math.max(dp[v][0], dp[v][1]);
            }
        }
    }

    static int MaxWeightIndependentTreeSubset(Vertex[] tree) {
        int size = tree.length;
        if (size == 0)
            return 0;

        dp = new long[size][2];
        visited = new boolean[size];

        dfs(tree, 0, -1);

        return (int) Math.max(dp[0][0], dp[0][1]);
    }

    public static void main(String[] args) throws IOException {
        // This is to avoid stack overflow issues
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    new PlanParty().run();
                } catch(IOException e) {
                }
            }
        }, "1", 1 << 26).start();
    }

    public void run() throws IOException {
        Vertex[] tree = ReadTree();
        int weight = MaxWeightIndependentTreeSubset(tree);
        System.out.println(weight);
    }
}