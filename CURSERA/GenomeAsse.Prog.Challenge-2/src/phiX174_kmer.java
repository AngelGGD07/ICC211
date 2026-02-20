import java.util.*;
import java.io.*;

public class phiX174_kmer {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<String> kmers = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            kmers.add(line.trim());
        }

        if (kmers.isEmpty()) return;

        // Construct De Bruijn Graph
        // Nodes are prefixes and suffixes of length k-1
        // Edge from prefix -> suffix
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, Integer> outDegree = new HashMap<>();
        Set<String> nodes = new HashSet<>();

        for (String kmer : kmers) {
            String prefix = kmer.substring(0, kmer.length() - 1);
            String suffix = kmer.substring(1);
            
            nodes.add(prefix);
            nodes.add(suffix);
            
            adj.computeIfAbsent(prefix, k -> new ArrayList<>()).add(suffix);
            outDegree.put(prefix, outDegree.getOrDefault(prefix, 0) + 1);
            inDegree.put(suffix, inDegree.getOrDefault(suffix, 0) + 1);
            
            // Ensure all nodes are in degree maps
            outDegree.putIfAbsent(suffix, 0);
            inDegree.putIfAbsent(prefix, 0);
        }

        // Find Eulerian Path
        // For a path (not cycle), start node has out - in = 1, end node has in - out = 1
        // For a cycle, all in == out.
        // The problem usually implies a circular genome or a linear one. 
        // If it's a circular genome (like phi X174), we look for a cycle.
        // If linear, we look for start/end.
        // Given "phi X174", it is a circular genome. However, standard assembly problems often ask for the linear string or assume Eulerian Path.
        // Let's check for start node.
        
        String startNode = null;
        for (String node : nodes) {
            if (outDegree.getOrDefault(node, 0) > inDegree.getOrDefault(node, 0)) {
                startNode = node;
                break;
            }
        }
        
        // If no unbalanced node, pick any node with outgoing edges (Eulerian Cycle)
        if (startNode == null) {
            startNode = adj.keySet().iterator().next();
        }

        Deque<String> stack = new ArrayDeque<>();
        List<String> path = new ArrayList<>();
        stack.push(startNode);

        while (!stack.isEmpty()) {
            String u = stack.peek();
            if (adj.containsKey(u) && !adj.get(u).isEmpty()) {
                List<String> neighbors = adj.get(u);
                String v = neighbors.remove(neighbors.size() - 1);
                stack.push(v);
            } else {
                path.add(stack.pop());
            }
        }
        
        Collections.reverse(path);
        
        // Reconstruct String
        // Path is a sequence of (k-1)-mers.
        // String = first_node + last_char_of_second + last_char_of_third ...
        // Note: If it's a cycle for circular genome, the problem might ask for the unique string.
        // Usually for circular genomes, the k-mers cover the circle.
        // If the path length matches the number of edges + 1, we have a full traversal.
        
        StringBuilder sb = new StringBuilder();
        if (!path.isEmpty()) {
            sb.append(path.get(0));
            for (int i = 1; i < path.size(); i++) {
                String node = path.get(i);
                sb.append(node.substring(node.length() - 1));
            }
        }
        
        // For circular genome reconstruction, we might need to trim the overlap at the end if the path wraps around.
        // But standard Eulerian path output is usually just the string.
        // If the problem is specifically "Eulerian Cycle" on k-mers, the last k-1 characters should match the first k-1.
        // Let's output the assembled string.
        
        // Check if circular (first k-1 chars == last k-1 chars) and trim if necessary?
        // Usually, for "String Reconstruction from k-mers", we just output the path collapsed.
        // However, if it's a circular genome, the k-mers wrap around.
        // Let's assume standard string reconstruction.
        
        String result = sb.toString();
        
        // If it is a circular genome, the prefix of length k-1 might be repeated at the end.
        // Let's check the problem context. Phi X174 is circular.
        // If the path is a cycle, the start and end nodes are the same.
        // The resulting string will have the first k-1 chars repeated at the end.
        // We should remove the last k-1 characters to get the unique genome sequence.
        
        int k = kmers.get(0).length();
        if (path.size() > 1 && path.get(0).equals(path.get(path.size() - 1))) {
             // It's a cycle, remove the repetition at the end (which is length k-1)
             result = result.substring(0, result.length() - (k - 1));
        }
        
        System.out.println(result);
    }
}
