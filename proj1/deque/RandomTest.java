package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class RandomTest {
    

    @Test
    public void testRandomCombined() {
        ArrayDeque<Integer> AD = new ArrayDeque<>();
        LinkedListDeque<Integer> LLD = new LinkedListDeque<>();

        int N = 10000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                AD.addLast(randVal);
                LLD.addLast(randVal);
//                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                AD.addFirst(randVal);
                LLD.addFirst(randVal);
            } else if (operationNumber == 2 && AD.size() > 0 && LLD.size() > 0) {
                //removeFirst
                int retA = AD.removeFirst();
                int retL = LLD.removeFirst();
                assertEquals(retL, retA);
            } else if (operationNumber == 3 && AD.size() > 0 && LLD.size() > 0) {
                //removeLast
                int retA = AD.removeLast();
                int retL = LLD.removeLast();
                assertEquals(retL, retA);
            } else if (operationNumber == 4 && AD.size() > 0 && LLD.size() > 0) {
                // size
                int sizeL = AD.size();
                int sizeB = LLD.size();
                assertEquals(sizeL, sizeB);

                //get
                int randVal = StdRandom.uniform(0, sizeB);
                int getL = AD.get(randVal);
                int getB = LLD.get(randVal);
                assertEquals(getB, getL);
            }
        }
    }


}
