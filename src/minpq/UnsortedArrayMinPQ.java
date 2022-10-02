package minpq;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Unsorted array (or {@link ArrayList}) implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class UnsortedArrayMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the item-priority pairs in no specific order.
     */
    private final List<PriorityNode<T>> items;

    /**
     * Constructs an empty instance.
     */
    public UnsortedArrayMinPQ() {
        items = new ArrayList<>();
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        items.add(new PriorityNode<>(item, priority));
    }

    @Override
    public boolean contains(T item) {
        for (PriorityNode<T> pn : this.items) {
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
        PriorityNode<T> min = items.get(0);
        for (PriorityNode<T> pn : items) {
            if (pn.priority() < min.priority()) {
                min = pn;
            }
        }
        return min.item();
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        int min = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).priority() < items.get(min).priority()) {
                min = i;
            }
        }
        PriorityNode<T> minNode = items.remove(min);
        return minNode.item();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        for (PriorityNode<T> pn : items) {
            if (pn.item().equals(item)) {
                pn.setPriority(priority);
            }
        }
    }

    @Override
    public int size() {
        return items.size();
    }
}
