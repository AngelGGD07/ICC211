import java.io.*;
import java.util.*;

public class CircuitDesign {
    private final InputReader reader;
    private final OutputWriter writer;

    public CircuitDesign(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        // Ejecutamos en un nuevo hilo con mayor stack para evitar StackOverflow en grafos profundos
        new Thread(null, new Runnable() {
            public void run() {
                InputReader reader = new InputReader(System.in);
                OutputWriter writer = new OutputWriter(System.out);
                new CircuitDesign(reader, writer).run();
                writer.writer.flush();
            }
        }, "1", 1 << 26).start();
    }

    class TwoSatisfiability {
        int numVars;
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] adjRev;
        boolean[] visited;
        ArrayList<Integer> order;
        int[] component;
        int numComponents;

        TwoSatisfiability(int n, int m) {
            numVars = n;
            int size = 2 * n;
            adj = new ArrayList[size];
            adjRev = new ArrayList[size];
            for (int i = 0; i < size; i++) {
                adj[i] = new ArrayList<>();
                adjRev[i] = new ArrayList<>();
            }
        }

        // Mapea literal a índice de nodo (0-based)
        // lit > 0 (x_i) -> 2*(i-1)
        // lit < 0 (-x_i) -> 2*(|i|-1) + 1
        int toNode(int lit) {
            if (lit > 0) return 2 * (lit - 1);
            else return 2 * (-lit - 1) + 1;
        }

        void addEdge(int u, int v) {
            adj[u].add(v);
            adjRev[v].add(u);
        }

        void dfs1(int u) {
            visited[u] = true;
            for (int v : adj[u]) {
                if (!visited[v]) dfs1(v);
            }
            order.add(u);
        }

        void dfs2(int u, int comp) {
            component[u] = comp;
            for (int v : adjRev[u]) {
                if (component[v] == -1) dfs2(v, comp);
            }
        }

        boolean isSatisfiable(int[] result) {
            int size = 2 * numVars;
            visited = new boolean[size];
            order = new ArrayList<>();

            // Paso 1 de Kosaraju
            for (int i = 0; i < size; i++) {
                if (!visited[i]) dfs1(i);
            }
            Collections.reverse(order);

            // Paso 2 de Kosaraju
            component = new int[size];
            Arrays.fill(component, -1);
            numComponents = 0;
            for (int u : order) {
                if (component[u] == -1) {
                    dfs2(u, numComponents++);
                }
            }

            for (int i = 0; i < numVars; i++) {
                int trueNode = 2 * i;
                int falseNode = 2 * i + 1;

                if (component[trueNode] == component[falseNode]) {
                    return false; // x y -x en la misma componente
                }

                // En el orden topológico inverso de Kosaraju:
                // Si la componente de x aparece DESPUÉS de la componente de -x (ID mayor),
                // significa que -x -> ... -> x, por lo tanto x debe ser VERDADERO.
                if (component[trueNode] > component[falseNode]) {
                    result[i] = 1;
                } else {
                    result[i] = 0;
                }
            }
            return true;
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        TwoSatisfiability twoSat = new TwoSatisfiability(n, m);
        for (int i = 0; i < m; ++i) {
            int u = reader.nextInt();
            int v = reader.nextInt();
            // Cláusula (u OR v) es equivalente a (-u -> v) Y (-v -> u)
            int not_u = twoSat.toNode(-u);
            int val_v = twoSat.toNode(v);

            int not_v = twoSat.toNode(-v);
            int val_u = twoSat.toNode(u);

            twoSat.addEdge(not_u, val_v);
            twoSat.addEdge(not_v, val_u);
        }

        int[] result = new int[n];
        if (twoSat.isSatisfiable(result)) {
            writer.printf("SATISFIABLE\n");
            for (int i = 1; i <= n; ++i) {
                if (result[i-1] == 1) {
                    writer.printf("%d", i);
                } else {
                    writer.printf("%d", -i);
                }
                if (i < n) {
                    writer.printf(" ");
                } else {
                    writer.printf("\n");
                }
            }
        } else {
            writer.printf("UNSATISFIABLE\n");
        }
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    String line = reader.readLine();
                    if (line == null) return null;
                    tokenizer = new StringTokenizer(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }

    static class OutputWriter {
        public PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        public void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}