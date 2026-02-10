import java.util.*;
import java.io.*;

public class RescheduleExams {

    class Edge {
        int u, v;
        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    // Reuse the 2-SAT logic for the reduction
    class TwoSatSolver {
        int numVars;
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] adjRev;
        boolean[] visited;
        ArrayList<Integer> order;
        int[] component;
        int numComponents;

        TwoSatSolver(int n) {
            numVars = n;
            int size = 2 * n;
            adj = new ArrayList[size];
            adjRev = new ArrayList[size];
            for (int i = 0; i < size; i++) {
                adj[i] = new ArrayList<>();
                adjRev[i] = new ArrayList<>();
            }
        }

        void addEdge(int u, int v) {
            adj[u].add(v);
            adjRev[v].add(u);
        }

        // clause (u OR v)
        void addClause(int uLit, int vLit) {
            int not_u = toNode(-uLit);
            int val_v = toNode(vLit);
            int not_v = toNode(-vLit);
            int val_u = toNode(uLit);
            addEdge(not_u, val_v);
            addEdge(not_v, val_u);
        }

        int toNode(int lit) {
            if (lit > 0) return 2 * (lit - 1);
            else return 2 * (-lit - 1) + 1;
        }

        void dfs1(int u) {
            visited[u] = true;
            for (int v : adj[u]) if (!visited[v]) dfs1(v);
            order.add(u);
        }

        void dfs2(int u, int comp) {
            component[u] = comp;
            for (int v : adjRev[u]) if (component[v] == -1) dfs2(v, comp);
        }

        boolean solve(boolean[] assignment) {
            int size = 2 * numVars;
            visited = new boolean[size];
            order = new ArrayList<>();
            for (int i = 0; i < size; i++) if (!visited[i]) dfs1(i);
            Collections.reverse(order);

            component = new int[size];
            Arrays.fill(component, -1);
            numComponents = 0;
            for (int u : order) if (component[u] == -1) dfs2(u, numComponents++);

            for (int i = 0; i < numVars; i++) {
                if (component[2 * i] == component[2 * i + 1]) return false;
                assignment[i] = component[2 * i] > component[2 * i + 1];
            }
            return true;
        }
    }

    char[] assignNewColors(int n, Edge[] edges, char[] colors) {
        // For each node i, there are 2 possible new colors (out of R, G, B)
        // Let's map the two possibilities to a boolean variable x_i.
        // x_i = 0 (False) -> First available color
        // x_i = 1 (True)  -> Second available color

        char[][] options = new char[n][2];
        String allColors = "RGB";

        for (int i = 0; i < n; i++) {
            char current = colors[i];
            int idx = 0;
            for (char c : allColors.toCharArray()) {
                if (c != current) {
                    options[i][idx++] = c;
                }
            }
        }

        TwoSatSolver sat = new TwoSatSolver(n);

        for (Edge e : edges) {
            int u = e.u - 1; // 0-based index
            int v = e.v - 1;

            // Constraints: options[u][choice_u] != options[v][choice_v]
            // We iterate over all 4 combinations of choices for u and v (0/1)
            // If a combination results in conflict, we add a clause to forbid it.

            for (int choiceU = 0; choiceU <= 1; choiceU++) {
                for (int choiceV = 0; choiceV <= 1; choiceV++) {
                    if (options[u][choiceU] == options[v][choiceV]) {
                        // Conflict! This combination is forbidden.
                        // Forbidden: (x_u == choiceU) AND (x_v == choiceV)
                        // Clause: NOT(x_u == choiceU) OR NOT(x_v == choiceV)

                        // Convert choices to literals:
                        // choice 0 -> literal -(u+1)
                        // choice 1 -> literal +(u+1)
                        // Negation logic:
                        // if choice is 0 (false), we want to forbid false, so clause term is true (x_u)
                        // if choice is 1 (true), we want to forbid true, so clause term is false (-x_u)

                        // Let's verify standard clause mapping:
                        // Clause (L1 OR L2). 
                        // To forbid (u=0, v=0) -> means NOT(u=0 AND v=0) -> (u=1 OR v=1) -> L1=u, L2=v
                        // To forbid (u=1, v=1) -> means NOT(u=1 AND v=1) -> (u=0 OR v=0) -> L1=-u, L2=-v
                        // To forbid (u=0, v=1) -> means NOT(u=0 AND v=1) -> (u=1 OR v=0) -> L1=u, L2=-v

                        int litU = (choiceU == 0) ? (u + 1) : -(u + 1);
                        int litV = (choiceV == 0) ? (v + 1) : -(v + 1);

                        sat.addClause(litU, litV);
                    }
                }
            }
        }

        boolean[] assignment = new boolean[n];
        if (!sat.solve(assignment)) {
            return null;
        }

        char[] result = new char[n];
        for (int i = 0; i < n; i++) {
            result[i] = assignment[i] ? options[i][1] : options[i][0];
        }
        return result;
    }

    void run() {
        Scanner scanner = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        if (!scanner.hasNext()) return;
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        scanner.nextLine();

        String colorsLine = scanner.nextLine();
        char[] colors = colorsLine.toCharArray();

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            edges[i] = new Edge(u, v);
        }

        char[] newColors = assignNewColors(n, edges, colors);

        if (newColors == null) {
            writer.println("Impossible");
        } else {
            writer.println(new String(newColors));
        }

        writer.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new RescheduleExams().run();
    }
}