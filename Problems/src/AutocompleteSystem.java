import java.util.*;

public class AutocompleteSystem {

    HashMap<String, Integer> freq = new HashMap<>();

    public void addQuery(String query) {
        freq.put(query, freq.getOrDefault(query, 0) + 1);
    }

    public List<String> search(String prefix) {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> e : freq.entrySet()) {

            if (e.getKey().startsWith(prefix)) {

                pq.offer(e);

                if (pq.size() > 10) {
                    pq.poll();
                }
            }
        }

        List<String> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            Map.Entry<String, Integer> e = pq.poll();
            result.add(e.getKey() + " (" + e.getValue() + " searches)");
        }

        Collections.reverse(result);
        return result;
    }

    public void updateFrequency(String query) {
        freq.put(query, freq.getOrDefault(query, 0) + 1);
        System.out.println("Frequency: " + freq.get(query));
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java download");
        system.addQuery("java tutorial");
        system.addQuery("java tutorial");
        system.addQuery("java 21 features");

        System.out.println(system.search("jav"));

        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
    }
}