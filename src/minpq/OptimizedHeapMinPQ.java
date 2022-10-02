package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class OptimizedHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of item-priority pairs.
     */
    private final List<PriorityNode<T>> items;
    /**
     * {@link Map} of each item to its associated index in the {@code items} heap.
     */
    private final Map<T, Integer> itemToIndex;
    /**
     * The number of elements in the heap.
     */
    private int size;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        items = new ArrayList<>();
        itemToIndex = new HashMap<>();
        // list starts at 1
        items.add(null);
        size = 0;
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        size += 1;
        this.items.add(new PriorityNode<>(item, priority));
        swim(size);
        // for (int i = 1; i <= size; i++) {
        //     this.itemToIndex.put(items.get(i).item(), i);    // will override previous values
        // }
    }

    @Override
    public boolean contains(T item) {
        return this.itemToIndex.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        return this.items.get(1).item();
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        swap(1, size);
        PriorityNode<T> result = this.items.remove(size);
        size -= 1;
        this.itemToIndex.remove(result.item());
        sink(1);
        // update map
        // for (int i = 1; i <= size; i++) {
        //     itemToIndex.put(this.items.get(i).item(), i);
        // }
        return result.item();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        int index = this.itemToIndex.get(item);
        // sink and swim to update new position
        this.items.get(index).setPriority(priority);
        swim(index);
        sink(index);
        // update map
        // for (int i = 1; i <= size; i++) {
        //     itemToIndex.put(this.items.get(i).item(), i);
        // }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * takes in two indexes, and swaps the priority nodes from both indexes
     */
    private void swap(int index1, int index2) {
        // no need to update map, since it's not sorted yet
        PriorityNode<T> temp = this.items.get(index1);
        this.items.set(index1, items.get(index2));
        this.items.set(index2, temp);
        itemToIndex.put(items.get(index1).item(), index1);
        itemToIndex.put(items.get(index2).item(), index2);
    }

    /**
     * continuous swap with parent, if parent is larger in value than child
     * assume: items is not empty
     */
    private void swim(int nodeIndex) {
        int index = nodeIndex;
        while (index > 1) {
            // if larger swap, or stop
            PriorityNode<T> parent = this.items.get(index / 2);
            PriorityNode<T> child = this.items.get(index);
            if (child.priority() < parent.priority()) {
                swap(index, index / 2);
            } else {
                // edge case: if it doesn't swap
                itemToIndex.put(items.get(nodeIndex).item(), nodeIndex);
                break;
            }
            index = index / 2;
        }
    }

    /**
     * recursive swap with left child, if parent has larger priority
     * assume: items is not empty
     * @param index index of node to start sinking
     */
    private void sink(int index) {
        // didn't consider edge case where there is only one left child
        if (index * 2 < size) {
            // see which is less left or right child
            PriorityNode<T> parent = this.items.get(index);
            PriorityNode<T> leftChild = this.items.get(index * 2);
            PriorityNode<T> rightChild = this.items.get(index * 2 + 1);
            if (leftChild.priority() < rightChild.priority()) {
                if (leftChild.priority() < parent.priority()) {
                    swap(index, index * 2);
                    sink(index * 2);
                }
            } else {
                if (rightChild.priority() < parent.priority()) {
                    swap(index, index * 2 + 1);
                    sink(index * 2 + 1);
                }
            }
            itemToIndex.put(items.get(index).item(), index);
        }
    }

}
