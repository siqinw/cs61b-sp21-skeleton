package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int numBuckets = 16;
    private int numItems;
    private double maxLoad = 0.75;

    /**
     * Constructors
     */
    public MyHashMap() {
        buckets = createTable(numBuckets);
    }

    public MyHashMap(int initialSize) {
        numBuckets = initialSize;
        buckets = createTable(numBuckets);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        numBuckets = initialSize;
        maxLoad = maxLoad;
        buckets = createTable(numBuckets);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] c = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            c[i] = createBucket();
        }
        return c;
    }

    @Override
    public void clear() {
        for (Collection<Node> b : buckets) {
            Iterator<Node> iterator = b.iterator();
            while (iterator.hasNext()) {
                Node n = iterator.next();
                iterator.remove();
            }
            numItems = 0;
        }
    }

    private int hashFunction(K key) {
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    @Override
    public boolean containsKey(K key) {
        int hash = hashFunction(key);
        for (Node n : buckets[hash]) {
            if (key.equals(n.key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int hash = hashFunction(key);
        for (Node n : buckets[hash]) {
            if (key.equals(n.key)) {
                return n.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return numItems;
    }


    private void resize() {
        Collection<Node>[] newBuckets = createTable(numBuckets * 2);
        for (int i = 0; i < numBuckets; i++) {
            for (Node n : buckets[i]) {
                put(n.key, n.value, newBuckets);
            }
        }
        buckets = newBuckets;
        numBuckets *= 2;
    }

    private void put(K key, V value, Collection<Node>[] Buckets) {
        int hash = hashFunction(key);
        Node newNode = createNode(key, value);
        Buckets[hash].add(newNode);
    }

    @Override
    public void put(K key, V value) {
        int hash = hashFunction(key);
        // Replace value if key match
        for (Node n : buckets[hash]) {
            if (key.equals(n.key)) {
                n.value = value;
                return;
            }
        }
        // Resize
        if ((double) numItems / numBuckets > maxLoad) {
            resize();
        }

        put(key, value, buckets);
        numItems += 1;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<K>();
        for (Collection<Node> b : buckets) {
            for (Node n : b) {
                keySet.add(n.key);
            }
        }
        return keySet;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
