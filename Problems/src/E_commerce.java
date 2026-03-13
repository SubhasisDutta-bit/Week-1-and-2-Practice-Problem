import java.util.*;

public class E_commerce {

    HashMap<String, Integer> stock = new HashMap<>();
    HashMap<String, LinkedHashMap<Integer, Boolean>> waitingList = new HashMap<>();

    public synchronized String checkStock(String productId) {
        int count = stock.getOrDefault(productId, 0);
        return count + " units available";
    }

    public synchronized String purchaseItem(String productId, int userId) {

        int count = stock.getOrDefault(productId, 0);

        if (count > 0) {
            stock.put(productId, count - 1);
            return "Success, " + (count - 1) + " units remaining";
        } else {

            waitingList.putIfAbsent(productId, new LinkedHashMap<>());
            LinkedHashMap<Integer, Boolean> queue = waitingList.get(productId);

            queue.put(userId, true);

            int position = queue.size();

            return "Added to waiting list, position #" + position;
        }
    }

    public void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
    }

    public static void main(String[] args) {

        E_commerce system = new E_commerce();

        system.addProduct("IPHONE15_256GB", 100);

        System.out.println(system.checkStock("IPHONE15_256GB"));

        System.out.println(system.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(system.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 100; i++) {
            system.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(system.purchaseItem("IPHONE15_256GB", 99999));
    }
}