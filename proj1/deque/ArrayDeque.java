package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> {

    private int size, head, tail;
    private T[] items;


    public ArrayDeque() {
        items = (T[]) new Object[16];
        // Use head & tail to mark the
        // Start and end position
        // Init at position 0
        size = head = tail = 0;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];

        if (head <= tail) {
            System.arraycopy(items, head, a, 0, size);
        } else {
            int num_head = items.length - head;
            System.arraycopy(items, head, a, 0, num_head);
            System.arraycopy(items, 0, a, num_head, size - num_head);
        }
        head = 0;
        tail = size - 1;
        items = a;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length)
            resize(size * 2);

        // Corner case
        if (size == 0)
            items[0] = item;
        else {
            // Wrap around
            if (head == 0)
                head = items.length;

            items[head - 1] = item;
            head -= 1;
        }

        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length)
            resize(size * 2);

        // Corner case
        if (size == 0)
            items[0] = item;
        else {
            // Wrap around
            if (tail == items.length - 1)
                tail = -1;

            items[tail + 1] = item;
            tail += 1;
        }

        size += 1;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (size == 0)
            return;
        if (head <= tail) {
            for (int i = head; i < tail + 1; i++) {
                System.out.print(items[i] + " ");
            }
        } else {
            for (int i = head; i < items.length; i++)
                System.out.print(items[i] + " ");
            for (int i = 0; i < tail + 1; i++)
                System.out.print(items[i] + " ");
        }
        System.out.println();
    }


    @Override
    public T removeFirst() {
        if (size == 0)
            return null;

        if (items.length > 16 && size < items.length / 4)
            resize(items.length / 4);

        T ret = items[head];

        items[head] = null;
        if (head == items.length - 1)
            head = -1;
        head += 1;

        size -= 1;
        if (size == 0)
            head = tail = 0;
        return ret;
    }

    @Override
    public T removeLast() {
        if (size == 0)
            return null;

        if (items.length > 16 && size < items.length / 4)
            resize(items.length / 4);

        T ret = items[tail];

        items[tail] = null;
        if (tail == 0)
            tail = items.length;
        tail -= 1;

        size -= 1;
        if (size == 0)
            head = tail = 0;
        return ret;
    }

    @Override
    public T get(int index) {
        if (index > size - 1)
            return null;

        int i;
        if (head <= tail)
            i = head + index;
        else {
            if (index < items.length - head)
                i = head + index;
            else
                i = index - (items.length - head);
        }
        return items[i];
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;

        public ArrayDequeIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size;
        }

        public T next() {
            T returnItem = items[wizPos];
            wizPos += 1;
            return returnItem;
        }
    }


    public boolean equals(Object o) {
        if (o == this) return true;
        if (o.getClass() != this.getClass())
            return false;
        ArrayDeque<T> AD = (ArrayDeque<T>) o;
        if (AD.size() != this.size())
            return false;
        for (int i = 0; i < size(); i++) {
            if (AD.get(i) != this.get(i))
                return false;
        }
        return true;
    }
}
