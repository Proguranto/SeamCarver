package minpq;

public class test {
    public static void main(String[] args) {
        ExtrinsicMinPQ<String> pq = new OptimizedHeapMinPQ<>();
        pq.add("1", 1.0);
        pq.add("2", 2.0);
        pq.add("3", 3.0);
        pq.add("4", 4.0);
        pq.add("5", 5.0);
        pq.add("6", 6.0);

// Call methods to evaluate behavior.
        pq.changePriority("6", 1.0);
        pq.changePriority("1", 7.0);
        while (!pq.isEmpty()) {
            System.out.println(pq.removeMin());
        }
    }
}
