import java.util.*;

public class Dijkstra {

    // Helper class for the PriorityQueue
    static class Node implements Comparator<Node> {
        public int node;
        public long cost;

        public Node() {
        }

        public Node(int node, long cost) {
            this.node = node;
            this.cost = cost;
        }

        @Override
        public int compare(Node node1, Node node2) {
            return Long.compare(node1.cost, node2.cost);
        }
    }

    private static long distance(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, int t) {
        int n = adj.length;
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[s] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>(n, new Node());
        pq.add(new Node(s, 0));

        while (!pq.isEmpty()) {
            Node uNode = pq.poll();
            int u = uNode.node;

            // If we have found a shorter path to u already, skip.
            if (uNode.cost > dist[u]) {
                continue;
            }
            
            // If the destination is reached, return its distance
            if (u == t) {
                if (dist[t] == Long.MAX_VALUE) {
                    return -1;
                }
                return dist[t];
            }

            for (int i = 0; i < adj[u].size(); i++) {
                int v = adj[u].get(i);
                int weight = cost[u].get(i);

                if (dist[u] != Long.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.add(new Node(v, dist[v]));
                }
            }
        }
        
        if (dist[t] == Long.MAX_VALUE) {
            return -1;
        }

        return dist[t];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
            cost[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
        }
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(distance(adj, cost, x, y));
        scanner.close();
    }
}
