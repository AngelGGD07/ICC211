import java.util.*;
import java.io.*;
import java.util.zip.CheckedInputStream;

public class SuffixTree {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    // Build a suffix tree of the string text and return a list
    // with all of the labels of its edges (the corresponding
    // substrings of the text) in any order.
    public List<String> computeSuffixTreeEdges(String text) {
        List<String> result = new ArrayList<String>();
        
        class Node {
            Map<Character, Node> children = new HashMap<>();
            int start;
            int length;
            
            Node(int start, int length) {
                this.start = start;
                this.length = length;
            }
        }
        
        Node root = new Node(-1, 0);
        
        for (int i = 0; i < text.length(); i++) {
            Node currentNode = root;
            int currentSuffixIndex = i;
            
            while (currentSuffixIndex < text.length()) {
                char currentChar = text.charAt(currentSuffixIndex);
                
                if (currentNode.children.containsKey(currentChar)) {
                    Node child = currentNode.children.get(currentChar);
                    int matchLength = 0;
                    
                    // Traverse the edge
                    while (matchLength < child.length && currentSuffixIndex + matchLength < text.length()) {
                        if (text.charAt(child.start + matchLength) != text.charAt(currentSuffixIndex + matchLength)) {
                            break;
                        }
                        matchLength++;
                    }
                    
                    if (matchLength == child.length) {
                        // Full edge match, continue from child
                        currentNode = child;
                        currentSuffixIndex += matchLength;
                    } else {
                        // Partial match, split the edge
                        Node splitNode = new Node(child.start, matchLength);
                        Node newLeaf = new Node(currentSuffixIndex + matchLength, text.length() - (currentSuffixIndex + matchLength));
                        
                        // Update child to be a child of splitNode
                        child.start += matchLength;
                        child.length -= matchLength;
                        
                        splitNode.children.put(text.charAt(child.start), child);
                        splitNode.children.put(text.charAt(newLeaf.start), newLeaf);
                        
                        currentNode.children.put(currentChar, splitNode);
                        
                        // We are done with this suffix
                        break;
                    }
                } else {
                    // No edge starts with this character, create a new leaf
                    Node newLeaf = new Node(currentSuffixIndex, text.length() - currentSuffixIndex);
                    currentNode.children.put(currentChar, newLeaf);
                    break;
                }
            }
        }
        
        // Collect edges
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            for (Node child : node.children.values()) {
                result.add(text.substring(child.start, child.start + child.length));
                queue.add(child);
            }
        }

        return result;
    }


    static public void main(String[] args) throws IOException {
        new SuffixTree().run();
    }

    public void print(List<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        List<String> edges = computeSuffixTreeEdges(text);
        print(edges);
    }
}
