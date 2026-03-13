import java.util.*;

public class MultiLevelCache {

    static class Video {
        String id;
        String data;

        Video(String id, String data) {
            this.id = id;
            this.data = data;
        }
    }

    LinkedHashMap<String, Video> L1 = new LinkedHashMap<>(10000, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry<String, Video> e) {
            return size() > 10000;
        }
    };

    LinkedHashMap<String, Video> L2 = new LinkedHashMap<>(100000, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry<String, Video> e) {
            return size() > 100000;
        }
    };

    HashMap<String, Video> L3 = new HashMap<>();
    HashMap<String, Integer> accessCount = new HashMap<>();

    int l1Hits = 0, l2Hits = 0, l3Hits = 0;

    public Video getVideo(String id) {

        if (L1.containsKey(id)) {
            l1Hits++;
            return L1.get(id);
        }

        if (L2.containsKey(id)) {
            l2Hits++;
            Video v = L2.get(id);
            promoteToL1(v);
            return v;
        }

        if (L3.containsKey(id)) {
            l3Hits++;
            Video v = L3.get(id);
            L2.put(id, v);
            accessCount.put(id, 1);
            return v;
        }

        return null;
    }

    void promoteToL1(Video v) {
        int count = accessCount.getOrDefault(v.id, 0) + 1;
        accessCount.put(v.id, count);

        if (count > 2) {
            L1.put(v.id, v);
        }
    }

    void addVideoToDatabase(String id, String data) {
        L3.put(id, new Video(id, data));
    }

    void invalidate(String id) {
        L1.remove(id);
        L2.remove(id);
        L3.remove(id);
        accessCount.remove(id);
    }

    void getStatistics() {
        int total = l1Hits + l2Hits + l3Hits;

        double l1Rate = total == 0 ? 0 : (l1Hits * 100.0 / total);
        double l2Rate = total == 0 ? 0 : (l2Hits * 100.0 / total);
        double l3Rate = total == 0 ? 0 : (l3Hits * 100.0 / total);

        System.out.println("L1 Hit Rate: " + l1Rate + "%");
        System.out.println("L2 Hit Rate: " + l2Rate + "%");
        System.out.println("L3 Hit Rate: " + l3Rate + "%");
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        cache.addVideoToDatabase("video_123", "Movie A");
        cache.addVideoToDatabase("video_999", "Movie B");

        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_123"));

        System.out.println(cache.getVideo("video_999"));

        cache.getStatistics();
    }
}