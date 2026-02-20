import java.io.*;
import java.util.*;

public class tip_removal {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> reads = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                reads.add(line);
            }
        }

        if (reads.isEmpty()) return;

        int k = 15;
        Set<String> kmers = new HashSet<>();
        for (String read : reads) {
            for (int i = 0; i <= read.length() - k; i++) {
                kmers.add(read.substring(i, i + k));
            }
        }

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, List<String>> adjRev = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, Integer> outDegree = new HashMap<>();
        Set<String> nodes = new HashSet<>();

        // Generate the bidirectional graph mappings
        for (String kmer : kmers) {
            String prefix = kmer.substring(0, k - 1);
            String suffix = kmer.substring(1);

            nodes.add(prefix);
            nodes.add(suffix);

            adj.computeIfAbsent(prefix, x -> new ArrayList<>()).add(suffix);
            adjRev.computeIfAbsent(suffix, x -> new ArrayList<>()).add(prefix);

            outDegree.put(prefix, outDegree.getOrDefault(prefix, 0) + 1);
            inDegree.put(suffix, inDegree.getOrDefault(suffix, 0) + 1);
        }

        for (String node : nodes) {
            adj.putIfAbsent(node, new ArrayList<>());
            adjRev.putIfAbsent(node, new ArrayList<>());
            inDegree.putIfAbsent(node, 0);
            outDegree.putIfAbsent(node, 0);
        }

        // A queue to iteratively peel off dead end nodes backward and forward
        int removedEdges = 0;
        Queue<String> q = new ArrayDeque<>();
        for (String node : nodes) {
            if (inDegree.get(node) == 0 || outDegree.get(node) == 0) {
                q.add(node);
            }
        }

        // Iteratively process branch stripping until only the core (cycles) remain
        while (!q.isEmpty()) {
            String node = q.poll();

            // Process an outgoing tip (terminating node)
            if (outDegree.get(node) == 0 && inDegree.get(node) > 0) {
                for (String prev : new ArrayList<>(adjRev.get(node))) {
                    if (adj.get(prev).contains(node)) {
                        adj.get(prev).remove(node);
                        adjRev.get(node).remove(prev);
                        outDegree.put(prev, outDegree.get(prev) - 1);
                        inDegree.put(node, inDegree.get(node) - 1);
                        removedEdges++;

                        // If the previous node has become a dead end, add it to the peeling queue
                        if (outDegree.get(prev) == 0 || inDegree.get(prev) == 0) {
                            q.add(prev);
                        }
                    }
                }
            }

            // Process an incoming tip (source node)
            if (inDegree.get(node) == 0 && outDegree.get(node) > 0) {
                for (String next : new ArrayList<>(adj.get(node))) {
                    if (adjRev.get(next).contains(node)) {
                        adj.get(node).remove(next);
                        adjRev.get(next).remove(node);
                        outDegree.put(node, outDegree.get(node) - 1);
                        inDegree.put(next, inDegree.get(next) - 1);
                        removedEdges++;

                        // If the next node has become a dead end, add it to the peeling queue
                        if (inDegree.get(next) == 0 || outDegree.get(next) == 0) {
                            q.add(next);
                        }
                    }
                }
            }
        }

        System.out.println(removedEdges);
    }
}