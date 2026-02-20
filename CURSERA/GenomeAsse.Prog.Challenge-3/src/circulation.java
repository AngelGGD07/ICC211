import java.io.*;
import java.util.*;

public class circulation {
    static class Edge {
        int to, rev;
        int cap, flow;
        int lowerBound;

        public Edge(int to, int rev, int cap, int lowerBound) {
            this.to = to;
            this.rev = rev;
            this.cap = cap;
            this.lowerBound = lowerBound;
            this.flow = 0;
        }
    }

    static List<List<Edge>> adj;
    static int[] level;
    static int[] ptr;
    static int N, M;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        if (!st.hasMoreTokens()) return;

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        adj = new ArrayList<>(N + 2);
        for (int i = 0; i < N + 2; i++) {
            adj.add(new ArrayList<>());
        }

        int[] demand = new int[N];
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken()) - 1;
            int v = Integer.parseInt(st.nextToken()) - 1;
            int l = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());

            addEdge(u, v, l, c);
            edges.add(adj.get(u).get(adj.get(u).size() - 1));
            demand[u] -= l;
            demand[v] += l;
        }

        int S = N;
        int T = N + 1;

        for (int i = 0; i < N; i++) {
            if (demand[i] > 0) {
                addEdge(S, i, 0, demand[i]);
            } else if (demand[i] < 0) {
                addEdge(i, T, 0, -demand[i]);
            }
        }

        dinic(S, T);

        boolean possible = true;
        for (Edge e : adj.get(S)) {
            if (e.flow < e.cap) {
                possible = false;
                break;
            }
        }

        if (possible) {
            System.out.println("YES");
            for (Edge e : edges) {
                System.out.println(e.lowerBound + e.flow);
            }
        } else {
            System.out.println("NO");
        }
    }

    static void addEdge(int from, int to, int lower, int cap) {
        Edge forward = new Edge(to, adj.get(to).size(), cap - lower, lower);
        Edge backward = new Edge(from, adj.get(from).size(), 0, 0);
        adj.get(from).add(forward);
        adj.get(to).add(backward);
    }

    static void dinic(int s, int t) {
        level = new int[adj.size()];
        ptr = new int[adj.size()];
        while (bfs(s, t)) {
            Arrays.fill(ptr, 0);
            while (dfs(s, t, Integer.MAX_VALUE) > 0) ;
        }
    }

    static boolean bfs(int s, int t) {
        Arrays.fill(level, -1);
        level[s] = 0;
        Queue<Integer> q = new ArrayDeque<>();
        q.offer(s);
        while (!q.isEmpty()) {
            int v = q.poll();
            for (Edge e : adj.get(v)) {
                if (e.cap - e.flow > 0 && level[e.to] == -1) {
                    level[e.to] = level[v] + 1;
                    q.offer(e.to);
                }
            }
        }
        return level[t] != -1;
    }

    static int dfs(int v, int t, int pushed) {
        if (pushed == 0) return 0;
        if (v == t) return pushed;
        for (; ptr[v] < adj.get(v).size(); ptr[v]++) {
            Edge e = adj.get(v).get(ptr[v]);
            if (level[v] + 1 != level[e.to] || e.cap - e.flow == 0) continue;
            int tr = e.to;
            int push = dfs(tr, t, Math.min(pushed, e.cap - e.flow));
            if (push == 0) continue;
            e.flow += push;
            adj.get(tr).get(e.rev).flow -= push;
            return push;
        }
        return 0;
    }
}
