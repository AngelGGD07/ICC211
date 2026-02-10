import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class CleaningApartment {
    private final InputReader reader;
    private final OutputWriter writer;

    public CleaningApartment(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new CleaningApartment(reader, writer).run();
        writer.writer.flush();
    }

    class Edge {
        int from;
        int to;
    }

    class ConvertHampathToSat {
        int numVertices;
        Edge[] edges;
        boolean[][] adjMatrix;

        ConvertHampathToSat(int n, int m) {
            numVertices = n;
            edges = new Edge[m];
            adjMatrix = new boolean[n][n];
            for (int i = 0; i < m; ++i) {
                edges[i] = new Edge();
            }
        }

        // Helper to get variable ID for "Vertex i is at Path Position j"
        // i and j are 0-indexed here
        int varnum(int i, int j) {
            return (i * numVertices) + j + 1;
        }

        void printEquisatisfiableSatFormula() {
            // Build adjacency matrix for O(1) lookups
            for(Edge e : edges) {
                adjMatrix[e.from-1][e.to-1] = true;
                adjMatrix[e.to-1][e.from-1] = true;
            }

            List<String> clauses = new ArrayList<>();

            // 1. Each vertex i must appear in the path
            for (int i = 0; i < numVertices; i++) {
                StringBuilder clause = new StringBuilder();
                for (int j = 0; j < numVertices; j++) {
                    clause.append(varnum(i, j)).append(" ");
                }
                clause.append("0");
                clauses.add(clause.toString());
            }

            // 2. No vertex i appears twice in the path
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    for (int k = j + 1; k < numVertices; k++) {
                        clauses.add("-" + varnum(i, j) + " -" + varnum(i, k) + " 0");
                    }
                }
            }

            // 3. Each position j in the path must be occupied
            for (int j = 0; j < numVertices; j++) {
                StringBuilder clause = new StringBuilder();
                for (int i = 0; i < numVertices; i++) {
                    clause.append(varnum(i, j)).append(" ");
                }
                clause.append("0");
                clauses.add(clause.toString());
            }

            // 4. No two vertices occupy the same position j
            for (int j = 0; j < numVertices; j++) {
                for (int i = 0; i < numVertices; i++) {
                    for (int k = i + 1; k < numVertices; k++) {
                        clauses.add("-" + varnum(i, j) + " -" + varnum(k, j) + " 0");
                    }
                }
            }

            // 5. Successive vertices in path must be connected by an edge
            // If u is at pos j and v is at pos j+1, there MUST be an edge.
            // Contrapositive: If there is NO edge between u and v, they cannot be adjacent.
            // Clause: NOT(u at j) OR NOT(v at j+1)
            for (int j = 0; j < numVertices - 1; j++) {
                for (int u = 0; u < numVertices; u++) {
                    for (int v = 0; v < numVertices; v++) {
                        if (u == v) continue; // Same node cannot be adjacent to itself (handled by uniqueness constraints anyway)

                        if (!adjMatrix[u][v]) {
                            clauses.add("-" + varnum(u, j) + " -" + varnum(v, j + 1) + " 0");
                        }
                    }
                }
            }

            int totalVars = numVertices * numVertices;
            writer.printf("%d %d\n", clauses.size(), totalVars);
            for (String c : clauses) {
                writer.printf("%s\n", c);
            }
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertHampathToSat converter = new ConvertHampathToSat(n, m);
        for (int i = 0; i < m; ++i) {
            converter.edges[i].from = reader.nextInt();
            converter.edges[i].to = reader.nextInt();
        }

        converter.printEquisatisfiableSatFormula();
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
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
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