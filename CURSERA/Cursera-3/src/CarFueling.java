import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CarFueling {

    static int computeMinRefills(int dist, int tank, int[] stops) {
        int numRefills = 0;
        int currentPosition = 0;
        int n = stops.length;
        int[] allStops = new int[n + 2];
        allStops[0] = 0;
        System.arraycopy(stops, 0, allStops, 1, n);
        allStops[n + 1] = dist;

        if (dist <= tank) {
            return 0;
        }

        int i = 0;
        while (currentPosition < dist) {
            int lastPossibleStop = -1;
            while (i < allStops.length && allStops[i] - currentPosition <= tank) {
                lastPossibleStop = allStops[i];
                i++;
            }

            if (lastPossibleStop == -1 || lastPossibleStop == currentPosition) {
                return -1; // Cannot move forward
            }

            currentPosition = lastPossibleStop;
            if (currentPosition < dist) {
                numRefills++;
            }
        }

        return numRefills;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String distStr = br.readLine();
        if (distStr == null) return;
        int dist = Integer.parseInt(distStr);
        int tank = Integer.parseInt(br.readLine());
        int n = Integer.parseInt(br.readLine());
        int[] stops = new int[n];
        if (n > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                stops[i] = Integer.parseInt(st.nextToken());
            }
        }

        System.out.println(computeMinRefills(dist, tank, stops));
    }
}
