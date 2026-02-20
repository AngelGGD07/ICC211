import java.util.*;
import java.io.*;

public class puzzle {
    static class Piece {
        int id;
        String top, left, bottom, right;
        String original;

        public Piece(int id, String top, String left, String bottom, String right, String original) {
            this.id = id;
            this.top = top;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
            this.original = original;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        List<Piece> pieces = new ArrayList<>();
        int idCounter = 0;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            line = line.trim();
            String content = line;
            if (line.startsWith("(") && line.endsWith(")")) {
                content = line.substring(1, line.length() - 1);
            }
            String[] colors = content.split(",");
            if (colors.length == 4) {
                pieces.add(new Piece(idCounter++, colors[0].trim(), colors[1].trim(), colors[2].trim(), colors[3].trim(), content));
            }
        }

        if (pieces.isEmpty()) return;

        int size = pieces.size();
        int n = (int) Math.round(Math.sqrt(size));
        if (n * n != size) return;

        Piece[][] grid = new Piece[n][n];
        boolean[] used = new boolean[size];

        if (solve(0, 0, n, grid, pieces, used)) {
            for (int i = 0; i < n; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < n; j++) {
                    sb.append("(").append(grid[i][j].original).append(")");
                    if (j < n - 1) {
                        sb.append(";");
                    }
                }
                System.out.println(sb.toString());
            }
        } else {
            // No solution found
        }
    }

    static boolean solve(int row, int col, int n, Piece[][] grid, List<Piece> pieces, boolean[] used) {
        if (row == n) return true;

        int nextRow = col == n - 1 ? row + 1 : row;
        int nextCol = col == n - 1 ? 0 : col + 1;

        for (int i = 0; i < pieces.size(); i++) {
            if (!used[i]) {
                Piece p = pieces.get(i);

                // Check constraints
                // Top constraint
                if (row == 0) {
                    if (!p.top.equals("black")) continue;
                } else {
                    if (!p.top.equals(grid[row - 1][col].bottom)) continue;
                }

                // Left constraint
                if (col == 0) {
                    if (!p.left.equals("black")) continue;
                } else {
                    if (!p.left.equals(grid[row][col - 1].right)) continue;
                }

                // Bottom constraint (only for last row)
                if (row == n - 1) {
                    if (!p.bottom.equals("black")) continue;
                }

                // Right constraint (only for last col)
                if (col == n - 1) {
                    if (!p.right.equals("black")) continue;
                }

                // Place piece
                grid[row][col] = p;
                used[i] = true;

                if (solve(nextRow, nextCol, n, grid, pieces, used)) return true;

                // Backtrack
                used[i] = false;
                grid[row][col] = null;
            }
        }
        return false;
    }
}
