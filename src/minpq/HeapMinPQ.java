package minpq;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * {@link PriorityQueue} implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class HeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link PriorityQueue} storing {@link PriorityNode} objects representing each item-priority pair.
     */
    private final PriorityQueue<PriorityNode<T>> pq;

    /**
     * Constructs an empty instance.
     */
    public HeapMinPQ() {
        pq = new PriorityQueue<>(Comparator.comparingDouble(PriorityNode::priority));
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        pq.add(new PriorityNode<>(item, priority));
    }

    @Override
    public boolean contains(T item) {
        for (PriorityNode<T> pn : pq) {
            if (pn.item().equals(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode <T> temp = pq.remove();
        pq.add(temp);
        return temp.item();
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return pq.remove().item();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        // don't need to look through just use remove and add
        for (PriorityNode<T> pn : pq) {
            if (pn.item().equals(item)) {
                pq.remove(pn);
                pn.setPriority(priority);
                pq.add(pn);
                break;
            }
        }
    }

    @Override
    public int size() {
        return pq.size();
    }
}
