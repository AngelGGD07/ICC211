import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class HashChains {

    private FastScanner in;
    private PrintWriter out;
    // store chains in array of lists
    private List<String>[] elems;
    // for hash function
    private int bucketCount;
    private int prime = 1000000007;
    private int multiplier = 263;

    public static void main(String[] args) throws IOException {
        new HashChains().processQueries();
    }

    private int hashFunc(String s) {
        long hash = 0;
        for (int i = s.length() - 1; i >= 0; --i)
            hash = (hash * multiplier + s.charAt(i)) % prime;
        return (int)hash % bucketCount;
    }

    private Query readQuery() throws IOException {
        String type = in.next();
        if (!type.equals("check")) {
            String s = in.next();
            return new Query(type, s);
        } else {
            int i = in.nextInt();
            return new Query(type, i);
        }
    }

    private void writeSearchResult(boolean wasFound) {
        out.println(wasFound ? "yes" : "no");
    }

    private void processQuery(Query query) {
        int hash = -1;
        if (query.s != null) {
            hash = hashFunc(query.s);
        }

        switch (query.type) {
            case "add":
                if (!elems[hash].contains(query.s))
                    // Add to the front of the list
                    elems[hash].add(0, query.s);
                break;
            case "del":
                if (elems[hash].contains(query.s))
                    elems[hash].remove(query.s);
                break;
            case "find":
                writeSearchResult(elems[hash].contains(query.s));
                break;
            case "check":
                if (query.ind < elems.length) {
                    for (String cur : elems[query.ind])
                        out.print(cur + " ");
                }
                out.println();
                break;
            default:
                throw new RuntimeException("Unknown query: " + query.type);
        }
    }

    public void processQueries() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        bucketCount = in.nextInt();
        // Fix: Initialize as an array of List, not ArrayList
        elems = new List[bucketCount];
        for(int i = 0; i < bucketCount; i++){
            // Using LinkedList for efficient additions to the front
            elems[i] = new LinkedList<>();
        }
        int queryCount = in.nextInt();
        for (int i = 0; i < queryCount; ++i) {
            processQuery(readQuery());
        }
        out.close();
    }

    static class Query {
        String type;
        String s;
        int ind;

        public Query(String type, String s) {
            this.type = type;
            this.s = s;
        }

        public Query(String type, int ind) {
            this.type = type;
            this.ind = ind;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}