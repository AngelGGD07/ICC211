import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class GSMNetwork {
    private final InputReader reader;
    private final OutputWriter writer;

    public GSMNetwork(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new GSMNetwork(reader, writer).run();
        writer.writer.flush();
    }

    class Edge {
        int from;
        int to;
    }

    class ConvertGSMNetworkProblemToSat {
        int numVertices;
        Edge[] edges;

        ConvertGSMNetworkProblemToSat (int n, int m) {
            numVertices = n;
            edges = new Edge[m];
            for (int i = 0; i < m; ++i) {
                edges[i] = new Edge();
            }
        }

        void printEquisatisfiableSatFormula() {
            List<String> clauses = new ArrayList<>();

            // Formula Logic:
            // For each vertex i, we have 3 variables: i_1, i_2, i_3 (colors 1, 2, 3)
            // Variable ID mapping: (vertexIndex * 3) + colorIndex
            // where vertexIndex is 0..(n-1) and colorIndex is 1..3

            int numVars = numVertices * 3;

            // Constraint 1: Every vertex must have EXACTLY one color
            for (int i = 0; i < numVertices; i++) {
                int r = (i * 3) + 1; // Red
                int g = (i * 3) + 2; // Green
                int b = (i * 3) + 3; // Blue

                // At least one color
                clauses.add(r + " " + g + " " + b + " 0");

                // At most one color (Mutually exclusive)
                clauses.add("-" + r + " -" + g + " 0");
                clauses.add("-" + r + " -" + b + " 0");
                clauses.add("-" + g + " -" + b + " 0");
            }

            // Constraint 2: Neighbors cannot have the same color
            for (Edge edge : edges) {
                // edge.from and edge.to are 1-based from input, convert to 0-based for calculation
                int u = edge.from - 1;
                int v = edge.to - 1;

                // If u is Red, v cannot be Red
                clauses.add("-" + ((u * 3) + 1) + " -" + ((v * 3) + 1) + " 0");

                // If u is Green, v cannot be Green
                clauses.add("-" + ((u * 3) + 2) + " -" + ((v * 3) + 2) + " 0");

                // If u is Blue, v cannot be Blue
                clauses.add("-" + ((u * 3) + 3) + " -" + ((v * 3) + 3) + " 0");
            }

            // Output C V
            writer.printf("%d %d\n", clauses.size(), numVars);
            for (String clause : clauses) {
                writer.printf("%s\n", clause);
            }
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertGSMNetworkProblemToSat converter = new ConvertGSMNetworkProblemToSat(n, m);
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