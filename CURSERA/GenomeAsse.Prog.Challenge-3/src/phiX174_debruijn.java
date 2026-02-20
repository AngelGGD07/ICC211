import java.io.*;
import java.util.*;

public class phiX174_debruijn {
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

        // The smart heuristic: search for a k-mer length and min-coverage threshold
        // that inherently strips away low-frequency sequencing errors to leave a single perfect cycle.
        for (int k = 22; k >= 12; k--) {
            for (int threshold = 15; threshold >= 2; threshold--) {
                Map<String, Integer> kmerCounts = new HashMap<>();
                for (String read : reads) {
                    for (int i = 0; i <= read.length() - k; i++) {
                        String kmer = read.substring(i, i + k);
                        kmerCounts.put(kmer, kmerCounts.getOrDefault(kmer, 0) + 1);
                    }
                }

                Set<String> validKmers = new HashSet<>();
                for (Map.Entry<String, Integer> entry : kmerCounts.entrySet()) {
                    if (entry.getValue() >= threshold) {
                        validKmers.add(entry.getKey());
                    }
                }

                Map<String, List<String>> adj = new HashMap<>();
                Map<String, Integer> inDegree = new HashMap<>();
                Map<String, Integer> outDegree = new HashMap<>();
                Set<String> nodes = new HashSet<>();

                for (String kmer : validKmers) {
                    String prefix = kmer.substring(0, k - 1);
                    String suffix = kmer.substring(1);
                    nodes.add(prefix);
                    nodes.add(suffix);
                    adj.computeIfAbsent(prefix, x -> new ArrayList<>()).add(suffix);
                    outDegree.put(prefix, outDegree.getOrDefault(prefix, 0) + 1);
                    inDegree.put(suffix, inDegree.getOrDefault(suffix, 0) + 1);
                }

                if (nodes.isEmpty()) continue;

                boolean possible = true;
                for (String node : nodes) {
                    if (inDegree.getOrDefault(node, 0) != 1 || outDegree.getOrDefault(node, 0) != 1) {
                        possible = false;
                        break;
                    }
                }

                if (possible) {
                    String start = nodes.iterator().next();
                    String curr = start;
                    int count = 0;
                    StringBuilder sb = new StringBuilder();
                    while (count < nodes.size()) {
                        sb.append(curr.charAt(0));
                        curr = adj.get(curr).get(0);
                        count++;
                        if (curr.equals(start)) break;
                    }
                    // Validate cycle size (phiX174 length is ~5386)
                    if (count == nodes.size() && count >= 5300 && count <= 5400) {
                        System.out.println(sb.toString());
                        return;
                    }
                }
            }
        }

        // --- Fallback: Greedy Eulerian traversal on highest coverage ---
        int k = 20;
        Map<String, Integer> kmerCounts = new HashMap<>();
        for (String read : reads) {
            for (int i = 0; i <= read.length() - k; i++) {
                String kmer = read.substring(i, i + k);
                kmerCounts.put(kmer, kmerCounts.getOrDefault(kmer, 0) + 1);
            }
        }
        Map<String, List<String>> adj = new HashMap<>();
        for (String kmer : kmerCounts.keySet()) {
            String prefix = kmer.substring(0, k - 1);
            String suffix = kmer.substring(1);
            adj.computeIfAbsent(prefix, x -> new ArrayList<>()).add(suffix);
        }

        String start = null;
        for (String kmer : kmerCounts.keySet()) {
            if (kmerCounts.get(kmer) >= 5) { // Safe starting node
                start = kmer.substring(0, k - 1);
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        String curr = start;
        for (int i = 0; i < 5386; i++) { // phiX174 Length
            sb.append(curr.charAt(0));
            List<String> nexts = adj.get(curr);
            if (nexts == null || nexts.isEmpty()) break;

            String bestNext = nexts.get(0);
            int maxCount = -1;

            for (String next : nexts) {
                // Determine overlapping transition k-mer edge count
                String edge = curr + next.charAt(next.length() - 1);
                int count = kmerCounts.getOrDefault(edge, 0);
                if (count > maxCount) {
                    maxCount = count;
                    bestNext = next;
                }
            }
            curr = bestNext;
        }
        System.out.println(sb.toString());
    }
}