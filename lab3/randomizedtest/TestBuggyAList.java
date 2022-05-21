package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testRandom() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 10000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
//                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1 && L.size() > 0 && B.size() > 0) {
                //removeLast
                int retL = L.removeLast();
                int retB = B.removeLast();
                assertEquals(retB, retL);
            } else if (operationNumber == 2 && L.size() > 0 && B.size() > 0) {
                // getLast
                int retL = L.getLast();
                int retB = B.getLast();
                assertEquals(retB, retL);
            } else {
                //size
                assertEquals(L.size(), B.size());
            }
        }
    }
}
