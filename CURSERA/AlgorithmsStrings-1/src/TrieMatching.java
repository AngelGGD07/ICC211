import java.io.*;
import java.util.*;

public class TrieMatching implements Runnable {
    static class Node
    {
        public static final int Letters =  4;
        public static final int NA      = -1;
        public int next [];
        public boolean patternEnd;

        Node ()
        {
            next = new int [Letters];
            Arrays.fill (next, NA);
            patternEnd = false;
        }
    }

    int letterToIndex (char letter)
    {
        switch (letter)
        {
            case 'A': return 0;
            case 'C': return 1;
            case 'G': return 2;
            case 'T': return 3;
            default: assert (false); return Node.NA;
        }
    }

    List <Integer> solve (String text, int n, List <String> patterns) {
        List <Integer> result = new ArrayList <Integer> ();
        List<Node> trie = new ArrayList<>();
        trie.add(new Node()); // Root

        // Build the Trie
        for (String pattern : patterns) {
            int currentNodeIndex = 0;
            for (int i = 0; i < pattern.length(); i++) {
                char currentSymbol = pattern.charAt(i);
                int index = letterToIndex(currentSymbol);
                if (index != Node.NA) {
                    if (trie.get(currentNodeIndex).next[index] != Node.NA) {
                        currentNodeIndex = trie.get(currentNodeIndex).next[index];
                    } else {
                        trie.add(new Node());
                        int newNodeIndex = trie.size() - 1;
                        trie.get(currentNodeIndex).next[index] = newNodeIndex;
                        currentNodeIndex = newNodeIndex;
                    }
                }
            }
            trie.get(currentNodeIndex).patternEnd = true;
        }

        // Search in Text
        for (int i = 0; i < text.length(); i++) {
            int currentNodeIndex = 0;
            for (int j = i; j < text.length(); j++) {
                char currentSymbol = text.charAt(j);
                int index = letterToIndex(currentSymbol);
                
                if (index != Node.NA && trie.get(currentNodeIndex).next[index] != Node.NA) {
                    currentNodeIndex = trie.get(currentNodeIndex).next[index];
                    if (trie.get(currentNodeIndex).patternEnd) {
                        result.add(i);
                        break; // Found a pattern starting at i
                    }
                } else {
                    break;
                }
            }
        }

        return result;
    }

    public void run () {
        try {
            BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
            String text = in.readLine ();
            int n = Integer.parseInt (in.readLine ());
            List <String> patterns = new ArrayList <String> ();
            for (int i = 0; i < n; i++) {
                patterns.add (in.readLine ());
            }

            List <Integer> ans = solve (text, n, patterns);

            for (int j = 0; j < ans.size (); j++) {
                System.out.print ("" + ans.get (j));
                System.out.print (j + 1 < ans.size () ? " " : "\n");
            }
        }
        catch (Throwable e) {
            e.printStackTrace ();
            System.exit (1);
        }
    }

    public static void main (String [] args) {
        new Thread (new TrieMatching ()).start ();
    }
}
