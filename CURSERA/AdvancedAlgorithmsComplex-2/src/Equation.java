import java.io.IOException;
import java.util.Scanner;

class Equation {
    double a[][];
    double b[];

    Equation(double a[][], double b[]) {
        this.a = a;
        this.b = b;
    }

    static Equation ReadEquation() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();

        double a[][] = new double[size][size];
        double b[] = new double[size];
        for (int raw = 0; raw < size; ++raw) {
            for (int column = 0; column < size; ++column)
                a[raw][column] = scanner.nextInt();
            b[raw] = scanner.nextInt();
        }
        scanner.close();
        return new Equation(a, b);
    }

    static double[] SolveEquation(Equation equation) {
        double[][] a = equation.a;
        double[] b = equation.b;
        int size = a.length;

        // Phase 1: Forward Elimination (to create an upper triangular matrix)
        for (int step = 0; step < size; ++step) {
            // Find pivot row for numerical stability (Partial Pivoting)
            int pivotRow = step;
            for (int i = step + 1; i < size; i++) {
                if (Math.abs(a[i][step]) > Math.abs(a[pivotRow][step])) {
                    pivotRow = i;
                }
            }

            // Swap current row with pivot row
            if (pivotRow != step) {
                double[] tempA = a[step];
                a[step] = a[pivotRow];
                a[pivotRow] = tempA;

                double tempB = b[step];
                b[step] = b[pivotRow];
                b[pivotRow] = tempB;
            }

            // Eliminate entries below the pivot
            double pivotValue = a[step][step];
            for (int i = step + 1; i < size; i++) {
                double factor = a[i][step] / pivotValue;
                b[i] -= factor * b[step];
                for (int j = step + 1; j < size; j++) {
                    a[i][j] -= factor * a[step][j];
                }
                a[i][step] = 0;
            }
        }

        // Phase 2: Back Substitution
        double[] solution = new double[size];
        for (int i = size - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < size; j++) {
                sum += a[i][j] * solution[j];
            }
            solution[i] = (b[i] - sum) / a[i][i];
        }

        return solution;
    }

    static void PrintColumn(double column[]) {
        int size = column.length;
        for (int raw = 0; raw < size; ++raw)
            System.out.printf("%.20f\n", column[raw]);
    }

    public static void main(String[] args) throws IOException {
        Equation equation = ReadEquation();
        double[] solution = SolveEquation(equation);
        PrintColumn(solution);
    }
}
