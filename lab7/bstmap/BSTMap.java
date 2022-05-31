package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private BSTNode<K, V> root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    private static class BSTNode<K, V> {
        K key;
        V value;

        BSTNode<K, V> left;
        BSTNode<K, V> right;

        BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            left = right = null;
        }
    }


    private void clear(BSTNode<K, V> N) {
        if (N == null) {
            return;
        }
        clear(N.left);
        clear(N.right);
        N.left = null;
        N.right = null;
    }

    /**
     * Removes all of the mappings from this map.
     */
    public void clear() {
        clear(root);
        root = null;
        size = 0;
    }

    private boolean containsKey(BSTNode<K, V> N, K key) {
        if (N == null) {
            return false;
        }
        if (key.compareTo(N.key) < 0) {
            return containsKey(N.left, key);
        } else if (key.compareTo(N.key) > 0) {
            return containsKey(N.right, key);
        } else {
            return true;
        }
    }

    //    @Override
    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }


    private V get(BSTNode<K, V> N, K key) {
        if (N == null) {
            return null;
        }
        if (key.compareTo(N.key) < 0) {
            return get(N.left, key);
        } else if (key.compareTo(N.key) > 0) {
            return get(N.right, key);
        } else {
            return N.value;
        }
    }

    //    @Override
    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        return get(root, key);
    }

    //    @Override
    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    private BSTNode<K, V> put(BSTNode<K, V> N, K key, V value) {
        if (N == null) {
            BSTNode<K, V> node = new BSTNode<>(key, value);
            if (size == 0) {
                root = node;
            }
            size += 1;
            return node;
        }
        if (key.compareTo(N.key) < 0) {
            N.left = put(N.left, key, value);
        } else if (key.compareTo(N.key) > 0) {
            N.right = put(N.right, key, value);
        } else {
            N.value = value;
            return N;
        }
        return N;
    }

    //    @Override
    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        put(root, key, value);
    }

    private void keySet(BSTNode<K, V> N, Set<K> keySet) {
        if (N == null) {
            return;
        }

        keySet(N.left, keySet);
        keySet.add(N.key);
        keySet(N.right, keySet);
    }

    @Override
    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        keySet(root, keySet);
        return keySet;
    }

    private BSTNode<K, V> remove(BSTNode<K, V> N, K key) {
        if (N == null) {
            return null;
        }
        if (key == N.key) {
            if (N.left == null && N.right == null) {
                if (root == N) {
                    root = null;
                }
                return null;
            } else if (N.left == null) {
                if (root == N) {
                    root = N.right;
                }
                return N.right;
            } else if (N.right == null) {
                if (root == N) {
                    root = N.left;
                }
                return N.left;
            } else {
                BSTNode<K, V> leftMax = findMax(N.left);
                leftMax.right = N.right;
                if (root == N) {
                    root = leftMax;
                }
                return leftMax;
            }
        } else if (key.compareTo(N.key) < 0) {
            N.left = remove(N.left, key);
        } else {
            N.right = remove(N.right, key);
        }
        return N;
    }


    private BSTNode<K, V> findMax(BSTNode<K, V> N) {
        if (N.right == null) {
            return N;
        }
        return findMax(N.right);
    }


    @Override
    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        V val = get(key);
        if (val == null) {
            return null;
        }
        remove(root, key);
        size -= 1;
        return val;
    }


    @Override
    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        V val = get(key);
        if (val == null || val != value) {
            return null;
        }
        remove(root, key);
        size -= 1;
        return value;
    }

    //    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("");
    }
}
