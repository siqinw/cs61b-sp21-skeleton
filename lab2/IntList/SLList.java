package IntList;

// Linked list with sentinel node
public class SLList {
    private static class intNode {
        int item;
        intNode next;

        public intNode(int x, intNode n) {
            item = x;
            next = n;
        }
    }

    private intNode sentinel;
//    private int size;

    public SLList() {
//        size = 0;
        sentinel = new intNode(-1, null);
    }

    public void addFirst(int x) {
        sentinel.next = new intNode(x, sentinel.next);
//        size += 1;
    }

    public void addLast(int x) {
        intNode p = sentinel;
        while (p.next != null) {
            p = p.next;
        }
        p.next = new intNode(x, null);
//        size += 1;
    }

    private static int size(intNode p) {
        if (p.next == null)
            return 1;
        return 1 + size(p.next);
    }

    public int size() {
        return size(sentinel) - 1;
    }

    public void printList() {
        System.out.println("Size: " + size());
        intNode p = sentinel.next;
        while (p != null) {
            System.out.println(p.item + " ");
            p = p.next;
        }

    }

    public static void main(String[] args) {
        SLList L = new SLList();
        L.addLast(5);
        L.addFirst(10);
        L.addFirst(15);
        L.addLast(20);

        L.printList();
    }
}
