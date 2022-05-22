package deque;

// Circular Linked List Implementation
public class LinkedListDeque<T> {

    private int size;
    private node<T> sentinel;

    private static class node<T> {
        T item;
        node<T> prev;
        node<T> next;

        public node(T i, node<T> p, node<T> n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    public LinkedListDeque() {
        sentinel = new node<T>(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        node<T> n = new node<>(item, sentinel, sentinel.next);
        sentinel.next.prev = n;
        sentinel.next = n;
        size += 1;
    }

    public void addLast(T item) {
        node<T> n = new node<>(item, sentinel.prev, sentinel);
        sentinel.prev.next = n;
        sentinel.prev = n;
        size += 1;
    }

    public boolean isEmpty() {
        return sentinel.next == sentinel;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        node<T> n = sentinel.next;
        while (n != sentinel) {
            System.out.print(n.item + " ");
            n = n.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (isEmpty())
            return null;

        // node to remove
        node<T> n = sentinel.next;
        T item = n.item;

        sentinel.next = n.next;
        n.next.prev = sentinel;

        size -= 1;
        return item;
    }

    public T removeLast() {
        if (isEmpty())
            return null;

        // node to remove
        node<T> n = sentinel.prev;
        T item = n.item;

        sentinel.prev = n.prev;
        n.prev.next = sentinel;

        size -= 1;
        return item;
    }

    public T get(int index) {
        if (index > size() - 1)
            return null;
        node<T> n = sentinel.next;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        return n.item;
    }

    private T getRecursive(node<T> n, int i) {
        if (i == 0)
            return n.item;

        return getRecursive(n.next, i - 1);
    }

    public T getRecursive(int index) {
        if (index > size() - 1)
            return null;
        return getRecursive(sentinel.next, index);
    }

    public boolean equals(Object o) {
        if (!(o instanceof LinkedListDeque))
            return false;
        if (((LinkedListDeque<T>) o).size() != this.size())
            return false;
//        boolean equal = true;
//        int s = size();
        for (int i = 0; i < size(); i++) {
            if (((LinkedListDeque<T>) o).get(i) != this.get(i))
                return false;
        }
        return true;
    }

    /*
    public Iterator<T> iterator(){

    }

     */
}
