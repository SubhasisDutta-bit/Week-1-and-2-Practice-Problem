import java.util.*;

public class RealTimeAnalytics {

    HashMap<String, Integer> pageViews = new HashMap<>();
    HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
    HashMap<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(String url, String userId, String source) {

        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    public void getDashboard() {

        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("Top Pages:");

        int rank = 1;

        for (Map.Entry<String, Integer> e : list) {

            if (rank > 10) break;

            String url = e.getKey();
            int views = e.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url + " - " + views + " views (" + unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        for (Map.Entry<String, Integer> e : trafficSources.entrySet()) {
            System.out.println(e.getKey() + " - " + e.getValue());
        }
    }

    public static void main(String[] args) {

        RealTimeAnalytics system = new RealTimeAnalytics();

        system.processEvent("/article/breaking-news", "user_123", "google");
        system.processEvent("/article/breaking-news", "user_456", "facebook");
        system.processEvent("/sports/championship", "user_111", "direct");
        system.processEvent("/sports/championship", "user_222", "google");
        system.processEvent("/sports/championship", "user_333", "google");

        system.getDashboard();
    }
}