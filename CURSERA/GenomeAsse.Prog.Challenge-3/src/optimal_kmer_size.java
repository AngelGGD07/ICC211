import java.io.*;
import java.util.*;

public class optimal_kmer_size {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> reads = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            reads.add(line.trim());
        }

        if (reads.isEmpty()) return;

        // Try different k values starting from the maximum possible read length
        for (int k = reads.get(0).length(); k > 1; k--) {
            // First, isolate unique k-mers to avoid artificial parallel edges from coverage
            Set<String> kmers = new HashSet<>();
            for (String read : reads) {
                for (int i = 0; i <= read.length() - k; i++) {
                    kmers.add(read.substring(i, i + k));
                }
            }

            Map<String, Integer> inDegree = new HashMap<>();
            Map<String, Integer> outDegree = new HashMap<>();
            Set<String> nodes = new HashSet<>();
            Map<String, String> nextNode = new HashMap<>();

            // Build the de Bruijn graph based strictly on unique k-mers
            for (String kmer : kmers) {
                String prefix = kmer.substring(0, k - 1);
                String suffix = kmer.substring(1);

                outDegree.put(prefix, outDegree.getOrDefault(prefix, 0) + 1);
                inDegree.put(suffix, inDegree.getOrDefault(suffix, 0) + 1);
                nextNode.put(prefix, suffix);

                nodes.add(prefix);
                nodes.add(suffix);
            }

            // Check if every node has exactly 1 incoming edge and 1 outgoing edge
            boolean possible = true;
            for (String node : nodes) {
                if (inDegree.getOrDefault(node, 0) != 1 || outDegree.getOrDefault(node, 0) != 1) {
                    possible = false;
                    break;
                }
            }

            if (possible && !nodes.isEmpty()) {
                // Ensure all nodes map to a SINGLE connected cycle
                String start = nodes.iterator().next();
                String curr = start;
                int count = 0;
                while (count < nodes.size()) {
                    curr = nextNode.get(curr);
                    count++;
                    if (curr.equals(start)) break;
                }

                // If the loop length matches total unique nodes, we found our optimal k
                if (count == nodes.size()) {
                    System.out.println(k);
                    return;
                }
            }
        }
    }
}