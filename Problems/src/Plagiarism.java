import java.util.*;

public class Plagiarism {

    HashMap<String, Set<String>> index = new HashMap<>();
    HashMap<String, List<String>> docNgrams = new HashMap<>();
    int N = 5;

    public List<String> generateNgrams(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            grams.add(sb.toString().trim());
        }

        return grams;
    }

    public void addDocument(String docId, String text) {
        List<String> grams = generateNgrams(text);
        docNgrams.put(docId, grams);

        for (String g : grams) {
            index.putIfAbsent(g, new HashSet<>());
            index.get(g).add(docId);
        }
    }

    public void analyzeDocument(String docId) {

        List<String> grams = docNgrams.get(docId);
        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String g : grams) {
            Set<String> docs = index.get(g);

            if (docs != null) {
                for (String d : docs) {
                    if (!d.equals(docId)) {
                        matchCount.put(d, matchCount.getOrDefault(d, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Extracted " + grams.size() + " n-grams");

        for (Map.Entry<String, Integer> e : matchCount.entrySet()) {
            int matches = e.getValue();
            double similarity = (matches * 100.0) / grams.size();

            System.out.println("Found " + matches + " matching n-grams with \"" + e.getKey() + "\"");
            System.out.println("Similarity: " + similarity + "%");

            if (similarity > 60) {
                System.out.println("PLAGIARISM DETECTED");
            } else if (similarity > 10) {
                System.out.println("Suspicious similarity");
            }
        }
    }

    public static void main(String[] args) {

        Plagiarism system = new Plagiarism();

        String doc1 = "java programming language is widely used for enterprise software development and backend systems";
        String doc2 = "java programming language is widely used for enterprise software and backend systems";
        String doc3 = "machine learning and artificial intelligence are transforming modern technology";

        system.addDocument("essay_089.txt", doc1);
        system.addDocument("essay_092.txt", doc2);
        system.addDocument("essay_123.txt", doc3);

        system.analyzeDocument("essay_092.txt");
    }
}