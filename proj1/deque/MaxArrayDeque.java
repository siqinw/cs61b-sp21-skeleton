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

        for (T i : this) {
            if (comp.compare(i, max) > 0)
                i = max;
        }

        return max;
    }

    public T max(Comparator<T> c) {
        if (this.isEmpty())
            return null;

        T max = this.get(0);

        for (T i : this) {
            if (c.compare(i, max) > 0)
                i = max;
        }

        return max;
    }
}
