package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> comp;

    public MaxArrayDeque(Comparator<T> c) {
        comp = c;
    }

    public T max() {
        if (this.isEmpty())
            return null;

        T max = this.get(0);

        for (int i = 0; i < size(); i++) {
            T item = get(i);
            if (comp.compare(item, max) > 0)
                item = max;
        }

        return max;
    }

    public T max(Comparator<T> c) {
        if (this.isEmpty())
            return null;

        T max = this.get(0);

        for (int i = 0; i < size(); i++) {
            T item = get(i);
            if (c.compare(item, max) > 0)
                item = max;
        }

        return max;
    }
}
