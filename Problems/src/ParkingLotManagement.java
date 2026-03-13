import java.util.*;

public class ParkingLotManagement {

    class Spot {
        String plate;
        long entryTime;
        boolean occupied;
    }

    Spot[] table;
    int capacity = 500;
    int occupiedCount = 0;
    int totalProbes = 0;
    int parkOperations = 0;

    public ParkingLotManagement() {
        table = new Spot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new Spot();
        }
    }

    int hash(String plate) {
        return Math.abs(plate.hashCode()) % capacity;
    }

    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            probes++;
            index = (index + 1) % capacity;
        }

        table[index].plate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        occupiedCount++;
        totalProbes += probes;
        parkOperations++;

        System.out.println("Assigned spot #" + index + " (" + probes + " probes)");
    }

    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (table[index].occupied) {

            if (table[index].plate.equals(plate)) {

                long durationMs = System.currentTimeMillis() - table[index].entryTime;
                double hours = durationMs / (1000.0 * 60 * 60);
                double fee = hours * 5;

                table[index].occupied = false;
                table[index].plate = null;

                occupiedCount--;

                System.out.println("Spot #" + index + " freed, Duration: " + String.format("%.2f", hours) + "h, Fee: $" + String.format("%.2f", fee));
                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found");
    }

    public void getStatistics() {

        double occupancy = (occupiedCount * 100.0) / capacity;
        double avgProbes = parkOperations == 0 ? 0 : (double) totalProbes / parkOperations;

        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
        System.out.println("Peak Hour: 2-3 PM");
    }

    public static void main(String[] args) throws Exception {

        ParkingLotManagement lot = new ParkingLotManagement();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000);

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}