import java.io.*;
import java.util.*;

public class Diet {

    BufferedReader br;
    PrintWriter out;
    StringTokenizer st;
    boolean eof;

    private void pivot(double[][] tableau, int[] basis, int pivotRow, int pivotCol) {
        int n = tableau.length - 1;
        int m = tableau[0].length - n - 1;

        basis[pivotRow] = pivotCol;
        double pivotElement = tableau[pivotRow][pivotCol];

        for (int j = 0; j < m + n + 1; j++) {
            tableau[pivotRow][j] /= pivotElement;
        }

        for (int i = 0; i < n + 1; i++) {
            if (i != pivotRow) {
                double factor = tableau[i][pivotCol];
                if (Math.abs(factor) > 1e-9) {
                    for (int j = 0; j < m + n + 1; j++) {
                        tableau[i][j] -= factor * tableau[pivotRow][j];
                    }
                }
            }
        }
    }

    int solveDietProblem(int n, int m, double A[][], double[] b, double[] c, double[] x) {
        double[][] tableau = new double[n + 1][m + n + 1];
        int[] basis = new int[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                tableau[i][j] = A[i][j];
            }
            tableau[i][m + i] = 1;
            basis[i] = m + i;
            tableau[i][m + n] = b[i];
        }

        for (int j = 0; j < m; j++) {
            tableau[n][j] = -c[j];
        }

        while (true) {
            int pivotRow = -1;
            double minB = -1e-9;
            for (int i = 0; i < n; i++) {
                if (tableau[i][m + n] < minB) {
                    minB = tableau[i][m + n];
                    pivotRow = i;
                }
            }

            if (pivotRow != -1) { // Infeasible, perform DUAL pivot
                int pivotCol = -1;
                double minRatio = Double.POSITIVE_INFINITY;
                for (int j = 0; j < m + n; j++) {
                    if (tableau[pivotRow][j] < -1e-9) {
                        double ratio = tableau[n][j] / -tableau[pivotRow][j];
                        if (ratio < minRatio) {
                            minRatio = ratio;
                            pivotCol = j;
                        }
                    }
                }
                if (pivotCol == -1) return -1; // Infeasible
                pivot(tableau, basis, pivotRow, pivotCol);
                continue;
            }

            int pivotCol = -1;
            double minCoeff = -1e-9;
            for (int j = 0; j < m + n; j++) {
                if (tableau[n][j] < minCoeff) {
                    minCoeff = tableau[n][j];
                    pivotCol = j;
                }
            }

            if (pivotCol == -1) break; // Optimal

            pivotRow = -1;
            double minRatio = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                if (tableau[i][pivotCol] > 1e-9) {
                    double ratio = tableau[i][m + n] / tableau[i][pivotCol];
                    if (ratio < minRatio) {
                        minRatio = ratio;
                        pivotRow = i;
                    }
                }
            }
            if (pivotRow == -1) return 1; // Unbounded
            pivot(tableau, basis, pivotRow, pivotCol);
        }

        Arrays.fill(x, 0.0);
        for (int i = 0; i < n; i++) {
            if (basis[i] < m) {
                x[basis[i]] = tableau[i][m + n];
            }
        }

        return 0;
    }

    void solve() throws IOException {
        int n = nextInt();
        int m = nextInt();
        double[][] A = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = nextInt();
            }
        }
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = nextInt();
        }
        double[] c = new double[m];
        for (int i = 0; i < m; i++) {
            c[i] = nextInt();
        }
        double[] ansx = new double[m];
        int anst = solveDietProblem(n, m, A, b, c, ansx);
        if (anst == -1) {
            out.printf("No solution\n");
            return;
        }
        if (anst == 0) {
            out.printf("Bounded solution\n");
            for (int i = 0; i < m; i++) {
                out.printf("%.18f%c", ansx[i], i + 1 == m ? '\n' : ' ');
            }
            return;
        }
        if (anst == 1) {
            out.printf("Infinity\n");
            return;
        }
    }

    Diet() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        solve();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        new Diet();
    }

    String nextToken() {
        while (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (Exception e) {
                eof = true;
                return null;
            }
        }
        return st.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }
}
