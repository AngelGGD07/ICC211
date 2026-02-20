import java.io.*;
import java.util.*;

public class bubble_detection {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) return;

        StringTokenizer st = new StringTokenizer(line);
        int k = Integer.parseInt(st.nextToken());
        int t = Integer.parseInt(st.nextToken());

        List<String> reads = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                reads.add(line);
            }
        }

        // Keep unique k-mers to avoid duplicate edges
        Set<String> kmers = new HashSet<>();
        for (String read : reads) {
            for (int i = 0; i <= read.length() - k; i++) {
                kmers.add(read.substring(i, i + k));
            }
        }

        Map<String, List<String>> adj = new HashMap<>();
        for (String kmer : kmers) {
            String prefix = kmer.substring(0, k - 1);
            String suffix = kmer.substring(1);
            adj.computeIfAbsent(prefix, x -> new ArrayList<>()).add(suffix);
        }

        int bubbles = 0;

        for (String v : adj.keySet()) {
            if (adj.get(v).size() >= 2) {
                // To avoid simultaneous pair searches blowing up, we just find all valid
                // simple paths <= t starting from v and store their internal vertices.
                Map<String, List<Set<String>>> pathsByEnd = new HashMap<>();
                dfs(v, v, new HashSet<>(), 0, t, adj, pathsByEnd);

                // Then group paths by endpoint w, and check pairs to see if they are disjoint.
                for (List<Set<String>> paths : pathsByEnd.values()) {
                    for (int i = 0; i < paths.size(); i++) {
                        for (int j = i + 1; j < paths.size(); j++) {
                            if (Collections.disjoint(paths.get(i), paths.get(j))) {
                                bubbles++;
                            }
                        }
                    }
                }
            }
        }
        System.out.println(bubbles);
    }

    static void dfs(String start, String current, Set<String> internalNodes, int depth, int t,
                    Map<String, List<String>> adj, Map<String, List<Set<String>>> pathsByEnd) {

        // If we moved past the start node, record the path to our current endpoint
        if (depth > 0 && !current.equals(start)) {
            pathsByEnd.computeIfAbsent(current, x -> new ArrayList<>())
                    .add(new HashSet<>(internalNodes));
        }

        if (depth == t) return;

        for (String next : adj.getOrDefault(current, Collections.emptyList())) {
            // Guarantee simple paths (no self-loops inside the path and it can't cross 'start' again)
            if (!next.equals(start) && !internalNodes.contains(next)) {
                boolean added = false;
                if (!current.equals(start)) {
                    internalNodes.add(current);
                    added = true;
                }
                dfs(start, next, internalNodes, depth + 1, t, adj, pathsByEnd);
                if (added) {
                    internalNodes.remove(current);
                }
            }
        }
    }
}