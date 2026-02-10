import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Evacuation {
    private static FastScanner in;

    public static void main(String[] args) throws IOException {
        in = new FastScanner();

        FlowGraph graph = readGraph();
        System.out.println(maxFlow(graph, 0, graph.size() - 1));
    }

    private static int maxFlow(FlowGraph graph, int from, int to) {
        int flow = 0;
        while (true) {
            // Find an augmenting path using BFS (Edmonds-Karp)
            Queue<Integer> queue = new LinkedList<>();
            queue.add(from);

            // path[i] stores the edge ID used to reach vertex i
            int[] path = new int[graph.size()];
            Arrays.fill(path, -1);
            path[from] = -2; // Mark source as visited

            boolean pathFound = false;
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int id : graph.getIds(u)) {
                    Edge e = graph.getEdge(id);
                    if (path[e.to] == -1 && e.capacity - e.flow > 0) {
                        path[e.to] = id;
                        queue.add(e.to);
                        if (e.to == to) {
                            pathFound = true;
                            break;
                        }
                    }
                }
                if (pathFound) {
                    break;
                }
            }

            if (!pathFound) {
                break; // No more augmenting paths
            }

            // Find the bottleneck capacity of the path
            int pathFlow = Integer.MAX_VALUE;
            int currentNode = to;
            while (currentNode != from) {
                int edgeId = path[currentNode];
                Edge e = graph.getEdge(edgeId);
                pathFlow = Math.min(pathFlow, e.capacity - e.flow);
                currentNode = e.from;
            }

            // Augment the flow along the path
            flow += pathFlow;
            currentNode = to;
            while (currentNode != from) {
                int edgeId = path[currentNode];
                graph.addFlow(edgeId, pathFlow);
                Edge e = graph.getEdge(edgeId);
                currentNode = e.from;
            }
        }
        return flow;
    }

    static FlowGraph readGraph() throws IOException {
        int vertex_count = in.nextInt();
        int edge_count = in.nextInt();
        FlowGraph graph = new FlowGraph(vertex_count);

        for (int i = 0; i < edge_count; ++i) {
            int from = in.nextInt() - 1, to = in.nextInt() - 1, capacity = in.nextInt();
            graph.addEdge(from, to, capacity);
        }
        return graph;
    }

    static class Edge {
        int from, to, capacity, flow;

        public Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

    /* This class implements a bit unusual scheme to store the graph edges, in order
     * to retrieve the backward edge for a given edge quickly. */
    static class FlowGraph {
        /* List of all - forward and backward - edges */
        private List<Edge> edges;

        /* These adjacency lists store only indices of edges from the edges list */
        private List<Integer>[] graph;

        public FlowGraph(int n) {
            this.graph = (ArrayList<Integer>[])new ArrayList[n];
            for (int i = 0; i < n; ++i)
                this.graph[i] = new ArrayList<>();
            this.edges = new ArrayList<>();
        }

        public void addEdge(int from, int to, int capacity) {
            /* Note that we first append a forward edge and then a backward edge,
             * so all forward edges are stored at even indices (starting from 0),
             * whereas backward edges are stored at odd indices. */
            Edge forwardEdge = new Edge(from, to, capacity);
            Edge backwardEdge = new Edge(to, from, 0);
            graph[from].add(edges.size());
            edges.add(forwardEdge);
            graph[to].add(edges.size());
            edges.add(backwardEdge);
        }

        public int size() {
            return graph.length;
        }

        public List<Integer> getIds(int from) {
            return graph[from];
        }

        public Edge getEdge(int id) {
            return edges.get(id);
        }

        public void addFlow(int id, int flow) {
            /* To get a backward edge for a true forward edge (i.e id is even), we should get id + 1
             * due to the described above scheme. On the other hand, when we have to get a "backward"
             * edge for a backward edge (i.e. get a forward edge for backward - id is odd), id - 1
             * should be taken.
             *
             * It turns out that id ^ 1 works for both cases. Think this through! */
            edges.get(id).flow += flow;
            edges.get(id ^ 1).flow -= flow;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
