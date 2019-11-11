import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newFirst = new Node();
        newFirst.item = item;
        if (isEmpty()) {
            first = newFirst;
            last = newFirst;
        }
        else {
            newFirst.next = first;
            first.prev = newFirst;
            first = newFirst;
        }
        size += 1;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newLast = new Node();
        newLast.item = item;
        if (isEmpty()) {
            last = newLast;
            first = newLast;
        }
        else {
            newLast.prev = last;
            last.next = newLast;
            last = newLast;
        }
        size += 1;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item removeItem = first.item;
        size -= 1;
        if (isEmpty()) {
            first = null;
            last = null;
        }
        else {
            first = first.next;
            first.prev = null;
        }
        return removeItem;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item removeItem = last.item;
        size -= 1;
        if (isEmpty()) {
            first = null;
            last = null;
        }
        else {
            last = last.prev;
            last.next = null;
        }
        return removeItem;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        /*
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addFirst(3);
        deque.addFirst(4);
        deque.removeLast();
        //deque.removeFirst();
        System.out.println(deque.isEmpty());
        for (int x : deque) {
            System.out.println(x);
        }
        */
    }
}
