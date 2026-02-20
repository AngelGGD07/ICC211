import java.io.*;
import java.util.*;

public class NonSharedSubstring implements Runnable {
    class Node {
        Map<Character, Node> children = new HashMap<>();
        int start;
        int length;
        int suffixIndex = -1; // Only for leaves
        boolean hasQ = false; // True if subtree has a suffix starting in q part

        Node(int start, int length) {
            this.start = start;
            this.length = length;
        }
    }

    String text;
    Node root;

    void buildSuffixTree(String s) {
        text = s;
        root = new Node(-1, 0);
        
        for (int i = 0; i < text.length(); i++) {
            Node currentNode = root;
            int currentSuffixIndex = i;
            
            while (true) {
                if (currentSuffixIndex >= text.length()) break;
                
                char currentChar = text.charAt(currentSuffixIndex);
                
                if (currentNode.children.containsKey(currentChar)) {
                    Node child = currentNode.children.get(currentChar);
                    int matchLength = 0;
                    
                    while (matchLength < child.length && currentSuffixIndex + matchLength < text.length()) {
                        if (text.charAt(child.start + matchLength) != text.charAt(currentSuffixIndex + matchLength)) {
                            break;
                        }
                        matchLength++;
                    }
                    
                    if (matchLength == child.length) {
                        currentNode = child;
                        currentSuffixIndex += matchLength;
                    } else {
                        Node splitNode = new Node(child.start, matchLength);
                        Node newLeaf = new Node(currentSuffixIndex + matchLength, text.length() - (currentSuffixIndex + matchLength));
                        newLeaf.suffixIndex = i;
                        
                        child.start += matchLength;
                        child.length -= matchLength;
                        
                        splitNode.children.put(text.charAt(child.start), child);
                        splitNode.children.put(text.charAt(newLeaf.start), newLeaf);
                        
                        currentNode.children.put(currentChar, splitNode);
                        break;
                    }
                } else {
                    Node newLeaf = new Node(currentSuffixIndex, text.length() - currentSuffixIndex);
                    newLeaf.suffixIndex = i;
                    currentNode.children.put(currentChar, newLeaf);
                    break;
                }
            }
        }
    }

    void markHasQ(Node node, int pLength) {
        if (node.children.isEmpty()) {
            if (node.suffixIndex > pLength) {
                node.hasQ = true;
            }
        } else {
            for (Node child : node.children.values()) {
                markHasQ(child, pLength);
                if (child.hasQ) {
                    node.hasQ = true;
                }
            }
        }
    }

    String solve (String p, String q) {
        String s = p + "#" + q + "$";
        buildSuffixTree(s);
        markHasQ(root, p.length());
        
        String result = p;
        int minLen = Integer.MAX_VALUE;
        
        Queue<Pair> queue = new LinkedList<>();
        queue.add(new Pair(root, ""));
        
        while(!queue.isEmpty()) {
            Pair current = queue.poll();
            Node u = current.node;
            String pathStr = current.path;
            
            if (pathStr.length() >= minLen && minLen != Integer.MAX_VALUE) continue;

            for (Map.Entry<Character, Node> entry : u.children.entrySet()) {
                Node v = entry.getValue();
                char edgeChar = entry.getKey();
                
                // Skip special characters to ensure we stay within p
                if (edgeChar == '#' || edgeChar == '$') continue;
                
                if (!v.hasQ) {
                    String candidate = pathStr + edgeChar;
                    if (candidate.length() < minLen) {
                        minLen = candidate.length();
                        result = candidate;
                    }
                } else {
                    String edgeLabel = text.substring(v.start, v.start + v.length);
                    // Check if edge label contains # or $ (it shouldn't if we constructed correctly and #/$ are unique)
                    // But to be safe:
                    if (edgeLabel.indexOf('#') != -1 || edgeLabel.indexOf('$') != -1) {
                        // If edge contains #, we can take the part before #?
                        // Actually, if v has Q, it means it leads to Q.
                        // If edge has #, it leads to Q part.
                        // But we are looking for non-shared.
                        // If we are here, it means v has Q.
                        // So we must continue.
                        // But if edge has #, we can't traverse past #.
                        // So we stop this branch.
                        continue;
                    }

                    if (pathStr.length() + edgeLabel.length() < minLen) {
                         queue.add(new Pair(v, pathStr + edgeLabel));
                    }
                }
            }
        }
        
        return result;
    }
    
    class Pair {
        Node node;
        String path;
        Pair(Node n, String p) { node = n; path = p; }
    }

    public void run () {
        try {
            BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
            String p = in.readLine ();
            String q = in.readLine ();

            String ans = solve (p, q);

            System.out.println (ans);
        }
        catch (Throwable e) {
            e.printStackTrace ();
            System.exit (1);
        }
    }

    public static void main (String [] args) {
        new Thread (new NonSharedSubstring ()).start ();
    }
}
