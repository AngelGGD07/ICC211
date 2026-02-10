import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class PhoneBook {

    public static void main(String[] args) {
        new PhoneBook().processQueries();
    }

    public void processQueries() {
        FastScanner in = new FastScanner();
        // Use an array as a direct-address hash table.
        // The phone number is the index, the name is the value.
        String[] phoneBook = new String[10000000];
        int queryCount = in.nextInt();
        for (int i = 0; i < queryCount; i++) {
            String queryType = in.next();
            int number = in.nextInt();
            if (queryType.equals("add")) {
                String name = in.next();
                phoneBook[number] = name;
            } else if (queryType.equals("del")) {
                phoneBook[number] = null;
            } else { // find
                String name = phoneBook[number];
                if (name == null) {
                    System.out.println("not found");
                } else {
                    System.out.println(name);
                }
            }
        }
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}