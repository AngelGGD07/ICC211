import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class BudgetAllocation {
    private final InputReader reader;
    private final OutputWriter writer;

    public BudgetAllocation(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new BudgetAllocation(reader, writer).run();
        writer.writer.flush();
    }

    class ConvertILPToSat {
        int[][] A;
        int[] b;
        int numVariables;
        int numInequalities;

        ConvertILPToSat(int n, int m) {
            numInequalities = n;
            numVariables = m;
            A = new int[n][m];
            b = new int[n];
        }

        void printEquisatisfiableSatFormula() {
            List<String> clauses = new ArrayList<>();

            // For each inequality (row in A)
            for (int i = 0; i < numInequalities; i++) {

                // 1. Identify non-zero variables in this inequality
                List<Integer> nonZeroIndices = new ArrayList<>();
                for (int j = 0; j < numVariables; j++) {
                    if (A[i][j] != 0) {
                        nonZeroIndices.add(j);
                    }
                }

                int k = nonZeroIndices.size();

                // If there are no variables (e.g., 0 <= 5 or 0 <= -1)
                if (k == 0) {
                    if (0 > b[i]) {
                        // Impossible constraint (0 <= negative). 
                        // Output a trivial unsatisfiable formula.
                        writer.printf("2 1\n");
                        writer.printf("1 0\n");
                        writer.printf("-1 0\n");
                        return;
                    }
                    continue; // Constraint satisfied, move to next
                }

                // 2. Check all 2^k permutations
                int limit = 1 << k; // 2^k
                for (int mask = 0; mask < limit; mask++) {
                    long lhs = 0;

                    // Calculate LHS for this permutation
                    for (int bit = 0; bit < k; bit++) {
                        if ((mask & (1 << bit)) != 0) {
                            // If bit is 1, add coefficient
                            lhs += A[i][nonZeroIndices.get(bit)];
                        }
                    }

                    // 3. If permutation violates inequality (LHS > RHS), add a clause forbidding it
                    if (lhs > b[i]) {
                        StringBuilder clause = new StringBuilder();
                        for (int bit = 0; bit < k; bit++) {
                            int varIdx = nonZeroIndices.get(bit); // 0-based index
                            int varNum = varIdx + 1; // 1-based SAT variable

                            // If in this mask the bit was 1, we must force it to NOT be 1 (add -var)
                            // If in this mask the bit was 0, we must force it to NOT be 0 (add var)
                            // This effectively forbids this specific combination.
                            if ((mask & (1 << bit)) != 0) {
                                clause.append("-").append(varNum).append(" ");
                            } else {
                                clause.append(varNum).append(" ");
                            }
                        }
                        clause.append("0");
                        clauses.add(clause.toString());
                    }
                }
            }

            if (clauses.isEmpty()) {
                // If no constraints generated clauses, problem is trivially satisfiable.
                // Output a trivial satisfiable formula (x1 or -x1)
                writer.printf("1 1\n");
                writer.printf("1 -1 0\n");
            } else {
                writer.printf("%d %d\n", clauses.size(), numVariables);
                for (String c : clauses) {
                    writer.printf("%s\n", c);
                }
            }
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertILPToSat converter = new ConvertILPToSat(n, m);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                converter.A[i][j] = reader.nextInt();
            }
        }
        for (int i = 0; i < n; ++i) {
            converter.b[i] = reader.nextInt();
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