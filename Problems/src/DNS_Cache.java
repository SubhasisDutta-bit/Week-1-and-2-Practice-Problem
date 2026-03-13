import java.util.*;

public class DNS_Cache {

    class DNSEntry {
        String domain;
        String ip;
        long expiry;

        DNSEntry(String domain, String ip, long ttl) {
            this.domain = domain;
            this.ip = ip;
            this.expiry = System.currentTimeMillis() + ttl * 1000;
        }
    }

    private final int capacity = 5;

    private LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                    return size() > capacity;
                }
            };

    private int hits = 0;
    private int misses = 0;

    public synchronized String resolve(String domain) {

        long now = System.currentTimeMillis();

        if (cache.containsKey(domain)) {
            DNSEntry e = cache.get(domain);

            if (now < e.expiry) {
                hits++;
                return "Cache HIT → " + e.ip;
            } else {
                cache.remove(domain);
            }
        }

        misses++;

        String ip = queryUpstream(domain);
        cache.put(domain, new DNSEntry(domain, ip, 5));

        return "Cache MISS → " + ip;
    }

    private String queryUpstream(String domain) {
        return "172.217.14." + new Random().nextInt(255);
    }

    public String getCacheStats() {
        int total = hits + misses;
        double rate = total == 0 ? 0 : (hits * 100.0) / total;
        return "Hit Rate: " + rate + "%";
    }

    public static void main(String[] args) throws Exception {

        DNS_Cache dns = new DNS_Cache();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(6000);

        System.out.println(dns.resolve("google.com"));

        System.out.println(dns.getCacheStats());
    }
}