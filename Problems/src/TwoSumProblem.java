import java.util.*;

public class TwoSumProblem {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        long time;
        String account;

        Transaction(int id, int amount, String merchant, long time, String account) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.time = time;
            this.account = account;
        }
    }

    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<int[]> findTwoSum(int target) {
        HashMap<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, t.id});
            }

            map.put(t.amount, t);
        }

        return result;
    }

    public List<int[]> findTwoSumWithinTime(int target, long windowMs) {
        HashMap<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction prev = map.get(complement);

                if (Math.abs(t.time - prev.time) <= windowMs) {
                    result.add(new int[]{prev.id, t.id});
                }
            }

            map.put(t.amount, t);
        }

        return result;
    }

    public List<List<Integer>> findKSum(int k, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(0, k, target, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int start, int k, int target, List<Integer> path, List<List<Integer>> result) {
        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }

        if (k == 0) return;

        for (int i = start; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            path.add(t.id);
            backtrack(i + 1, k - 1, target - t.amount, path, result);
            path.remove(path.size() - 1);
        }
    }

    public void detectDuplicates() {
        HashMap<String, Set<String>> map = new HashMap<>();

        for (Transaction t : transactions) {
            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new HashSet<>());
            map.get(key).add(t.account);
        }

        for (Map.Entry<String, Set<String>> e : map.entrySet()) {
            if (e.getValue().size() > 1) {
                System.out.println("{amount:" + e.getKey().split("-")[0] +
                        ", merchant:" + e.getKey().split("-")[1] +
                        ", accounts:" + e.getValue() + "}");
            }
        }
    }

    public static void main(String[] args) {

        TwoSumProblem system = new TwoSumProblem();

        system.addTransaction(new Transaction(1, 500, "StoreA", System.currentTimeMillis(), "acc1"));
        system.addTransaction(new Transaction(2, 300, "StoreB", System.currentTimeMillis(), "acc2"));
        system.addTransaction(new Transaction(3, 200, "StoreC", System.currentTimeMillis(), "acc3"));
        system.addTransaction(new Transaction(4, 500, "StoreA", System.currentTimeMillis(), "acc4"));

        List<int[]> pairs = system.findTwoSum(500);

        for (int[] p : pairs) {
            System.out.println("(" + p[0] + "," + p[1] + ")");
        }

        system.detectDuplicates();

        List<List<Integer>> ksum = system.findKSum(3, 1000);

        for (List<Integer> l : ksum) {
            System.out.println(l);
        }
    }
}