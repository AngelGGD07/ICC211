import java.io.*;
import java.util.Random;
import java.util.StringTokenizer;

public class SubstringEquality {
	public class Solver {
		private String s;
		private long[] h1;
		private long[] h2;
		private long[] p1;
		private long[] p2;
		private long m1 = 1000000007;
		private long m2 = 1000000009;
		private long x;

		public Solver(String s) {
			this.s = s;
			Random random = new Random();
			x = random.nextInt((int) m1 - 2) + 1;
			h1 = new long[s.length() + 1];
			h2 = new long[s.length() + 1];
			p1 = new long[s.length() + 1];
			p2 = new long[s.length() + 1];
			p1[0] = 1;
			p2[0] = 1;

			for (int i = 1; i <= s.length(); i++) {
				h1[i] = (h1[i - 1] * x + s.charAt(i - 1)) % m1;
				h2[i] = (h2[i - 1] * x + s.charAt(i - 1)) % m2;
				p1[i] = (p1[i - 1] * x) % m1;
				p2[i] = (p2[i - 1] * x) % m2;
			}
		}

		public boolean ask(int a, int b, int l) {
			long hashA1 = (h1[a + l] - (p1[l] * h1[a]) % m1 + m1) % m1;
			long hashB1 = (h1[b + l] - (p1[l] * h1[b]) % m1 + m1) % m1;

			long hashA2 = (h2[a + l] - (p2[l] * h2[a]) % m2 + m2) % m2;
			long hashB2 = (h2[b + l] - (p2[l] * h2[b]) % m2 + m2) % m2;

			return hashA1 == hashB1 && hashA2 == hashB2;
		}
	}

	public void run() throws IOException {
		FastScanner in = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);
		String s = in.next();
		int q = in.nextInt();
		Solver solver = new Solver(s);
		for (int i = 0; i < q; i++) {
			int a = in.nextInt();
			int b = in.nextInt();
			int l = in.nextInt();
			out.println(solver.ask(a, b, l) ? "Yes" : "No");
		}
		out.close();
	}

	static public void main(String[] args) throws IOException {
	    new SubstringEquality().run();
	}

	class FastScanner {
		StringTokenizer tok;
		BufferedReader br;

		FastScanner() {
			br = new BufferedReader(new InputStreamReader(System.in));
			tok = new StringTokenizer("");
		}

		String next() throws IOException {
			while (!tok.hasMoreElements())
				tok = new StringTokenizer(br.readLine());
			return tok.nextToken();
		}

		int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
	}
}