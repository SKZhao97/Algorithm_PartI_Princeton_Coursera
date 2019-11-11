import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size = 0;
    private Item[] rmQueue;

    public RandomizedQueue() {
        rmQueue = (Item[]) new Object[2];
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    private void resize(int newSize) {
        Item[] temp = (Item[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            temp[i] = rmQueue[i];
        }
        rmQueue = temp;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (size == rmQueue.length) {
            resize(2 * size);
        }
        rmQueue[size] = item;
        size += 1;
    }

    public Item dequeue() {
        if (size == 0)
            throw new java.util.NoSuchElementException();
        int ranNum = StdRandom.uniform(0, size);
        Item deItem = rmQueue[ranNum];
        rmQueue[ranNum] = rmQueue[size - 1];
        size -= 1;
        if ((size > 0) && (size == 1 / 4 * rmQueue.length)) {
            resize(1 / 2 * rmQueue.length);
        }
        return deItem;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int ranNum = StdRandom.uniform(0, size);
        Item samItem = rmQueue[ranNum];
        return samItem;
    }

    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private int index = 0;

        private final int[] randomQueue = StdRandom.permutation(size);

        public boolean hasNext() {
            return index < size;
        }

        public Item next() {
            if (index == size) {
                throw new java.util.NoSuchElementException();
            }
            Item nextItem = rmQueue[randomQueue[index]];
            index += 1;
            return nextItem;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
    /*
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(11);
        queue.enqueue(22);
        queue.enqueue(33);
        queue.enqueue(111);
        queue.enqueue(222);
        queue.enqueue(333);
        System.out.println(queue.isEmpty());
        System.out.println(queue.size());
        System.out.println(queue.dequeue());
        System.out.println(queue.sample());
        for (int x : queue) {
            System.out.println(x);
        }
      */
    }
}
