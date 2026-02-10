import java.io.*;
import java.util.*;

public class AdAllocation {

    BufferedReader br;
    PrintWriter out;
    StringTokenizer st;
    boolean eof;

    final double EPS = 1e-9;

    private void pivot(double[][] tableau, int nRows, int nCols, int[] basis, int pivotRow, int pivotCol) {
        double pivotElement = tableau[pivotRow][pivotCol];
        basis[pivotRow] = pivotCol;

        for (int j = 0; j < nCols; j++) {
            tableau[pivotRow][j] /= pivotElement;
        }

        for (int i = 0; i < nRows; i++) {
            if (i != pivotRow && Math.abs(tableau[i][pivotCol]) > EPS) {
                double factor = tableau[i][pivotCol];
                for (int j = 0; j < nCols; j++) {
                    tableau[i][j] -= factor * tableau[pivotRow][j];
                }
            }
        }
    }

    int runSimplex(double[][] tableau, int n, int nRows, int nCols, int[] basis, int objRow) {
        while (true) {
            int pivotCol = -1;
            for (int j = 0; j < nCols - 1; j++) {
                if (tableau[objRow][j] < -EPS) {
                    pivotCol = j;
                    break;
                }
            }

            if (pivotCol == -1) return 0;

            int pivotRow = -1;
            double minRatio = Double.POSITIVE_INFINITY;

            for (int i = 0; i < n; i++) {
                if (tableau[i][pivotCol] > EPS) {
                    double ratio = tableau[i][nCols - 1] / tableau[i][pivotCol];
                    if (ratio >= -EPS && ratio < minRatio) {
                        minRatio = ratio;
                        pivotRow = i;
                    }
                }
            }

            if (pivotRow == -1) return 1;

            pivot(tableau, nRows, nCols, basis, pivotRow, pivotCol);
        }
    }

    int allocateAds(int n, int m, double A[][], double[] b, double[] c, double[] x) {
        boolean needsPhase1 = false;
        for (int i = 0; i < n; i++) {
            if (b[i] < -EPS) {
                needsPhase1 = true;
                break;
            }
        }

        if (!needsPhase1) {
            int nCols = m + n + 1;
            double[][] tab = new double[n + 1][nCols];
            int[] basis = new int[n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) tab[i][j] = A[i][j];
                tab[i][m + i] = 1.0;
                tab[i][nCols - 1] = b[i];
                basis[i] = m + i;
            }

            for (int j = 0; j < m; j++) tab[n][j] = -c[j];

            if (runSimplex(tab, n, n + 1, nCols, basis, n) == 1) return 1;

            Arrays.fill(x, 0.0);
            for (int i = 0; i < n; i++) {
                if (basis[i] < m) x[basis[i]] = tab[i][nCols - 1];
            }
            return 0;
        }

        int nCols = m + n + n + 1;
        double[][] tab = new double[n + 2][nCols];
        int[] basis = new int[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) tab[i][j] = A[i][j];
            tab[i][m + i] = 1.0;
            tab[i][m + n + i] = 1.0;
            tab[i][nCols - 1] = b[i];
            basis[i] = m + n + i;
        }

        for (int i = 0; i < n; i++) {
            tab[n + 1][m + n + i] = -1.0;
        }

        for (int j = 0; j < m; j++) tab[n][j] = -c[j];

        for (int i = 0; i < n; i++) {
            double factor = tab[n + 1][basis[i]];
            if (Math.abs(factor) > EPS) {
                for (int j = 0; j < nCols; j++) {
                    tab[n + 1][j] -= factor * tab[i][j];
                }
            }
        }

        runSimplex(tab, n, n + 2, nCols, basis, n + 1);

        if (tab[n + 1][nCols - 1] < -EPS) return -1;

        if (runSimplex(tab, n, n + 2, nCols, basis, n) == 1) return 1;

        Arrays.fill(x, 0.0);
        for (int i = 0; i < n; i++) {
            if (basis[i] < m) x[basis[i]] = tab[i][nCols - 1];
        }
        return 0;
    }

    void solve() throws IOException {
        int n = nextInt(), m = nextInt();
        double[][] A = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                A[i][j] = nextInt();
        double[] b = new double[n];
        for (int i = 0; i < n; i++) b[i] = nextInt();
        double[] c = new double[m];
        for (int i = 0; i < m; i++) c[i] = nextInt();

        double[] x = new double[m];
        int result = allocateAds(n, m, A, b, c, x);

        if (result == -1) out.println("No solution");
        else if (result == 1) out.println("Infinity");
        else {
            out.println("Bounded solution");
            for (int i = 0; i < m; i++)
                out.printf("%.18f%c", x[i], i == m - 1 ? '\n' : ' ');
        }
    }

    AdAllocation() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        solve();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        new AdAllocation();
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